//Created by Glenn

package sg.edu.np.spendid.Records;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import sg.edu.np.spendid.Dialogs.MyAlertDialog;
import sg.edu.np.spendid.Models.Wallet;
import sg.edu.np.spendid.Records.Adapters.CatSliderAdapter;
import sg.edu.np.spendid.Dialogs.CurrencyConvertDialog;
import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Models.Record;
import sg.edu.np.spendid.Utils.LimitNotification;
import sg.edu.np.spendid.Utils.Permissions.RequestReadPermission;
import sg.edu.np.spendid.Utils.WalletNameList;

//Create transaction

public class AddRecordActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private TextView selectedCat, recordCur;
    private EditText title, description, amt;
    private FloatingActionButton fab;
    private TextInputLayout title_layout;
    private HashMap<String, Boolean> checkValues;
    private Calendar currentTime = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private Spinner selectWallet;
    private String baseCurrency;
    private byte[] imageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
        dbHandler = new DBHandler(this, null, null, 1);
        selectedCat = findViewById(R.id.newRecordCat_textView);
        selectWallet = findViewById(R.id.newRecordWallet_spinner);
        title = findViewById(R.id.newRecordTitle_editText);
        amt = findViewById(R.id.newRecordAmt_editText);
        description = findViewById(R.id.newRecordDes_editText);
        fab = findViewById(R.id.newRecord_fab);
        title_layout = findViewById(R.id.newRecordTitle_layout);
        recordCur = findViewById(R.id.newRecordCur_textView);
        TextView selImage = findViewById(R.id.newRecordImage_textView);
        ImageView imageStatus = findViewById(R.id.newRecordImage_imageView);
        baseCurrency = getString(R.string.baseCurrency);
        recordCur.setText(baseCurrency);
        imageData = null;

        initToolbar(); //set toolbar

        checkValues = initCheckValues(); //set values for record validation

        //initiate spinner to select wallet to export or import with walletList
        ArrayList<Wallet> walletArrayList = dbHandler.getWallets();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                new WalletNameList(walletArrayList).getList());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectWallet.setAdapter(adapter);

        //reassign selected wallet when spinner index change
        selectWallet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                promptConversion(baseCurrency, walletArrayList.get(position).getCurrency());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // pass
            }
        });

        //category slider
        RecyclerView catRV = findViewById(R.id.newRecordCat_RV);
        CatSliderAdapter myCatAdapter = new CatSliderAdapter(dbHandler.getCategories(), selectedCat);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        catRV.setLayoutManager(myLayoutManager);
        myLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        catRV.setItemAnimator(new DefaultItemAnimator());
        catRV.setAdapter(myCatAdapter);

        //Open file picker
        ActivityResultLauncher<String> getFile = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        //import the file if result of file is chosen
                        if (result != null){
                            try {
                                //get image bytes
                                imageData = new ProcessImage(AddRecordActivity.this).render(result);

                                //validate image
                                if (imageData != null){

                                    //set image and change message
                                    selImage.setText("Image Attached");
                                    imageStatus.setImageResource(R.drawable.ic_clear_24);
                                    removeImg(imageStatus, selImage);
                                }
                            }
                            //error handling
                            catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Unable to save image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        selImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new RequestReadPermission(AddRecordActivity.this).checkPermission()){
                    getFile.launch("image/*"); //initiate file picker with CSV format
                }
            }
        });

        //create transaction when button is clicked
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title_txt = checkTitle(); //validate title
                String des_txt = description.getText().toString(); //get description
                String cat = checkCat(); //validate category
                double amount = checkAmt(); //validate amount
                //get current date and time
                String date = dateFormat.format(currentTime.getTime());
                String time = timeFormat.format(currentTime.getTime());

                //create transaction if record is valid
                if (validRecord()) {
                    Record record = new Record(title_txt, des_txt, amount, cat, date, time, imageData, walletArrayList.get(selectWallet.getSelectedItemPosition()).getWalletId());
                    boolean isExpense = dbHandler.catIsExpense(record.getCategory());
                    //initialise limit notification
                    LimitNotification limit = new LimitNotification(getApplicationContext(), dbHandler, amount);

                    //check if reached limit set and record is expense
                    if (limit.checkExceedLimit(isExpense)) {

                        //show dialog to warn limit will exceed
                        notifyLimit(record);
                    }
                    else {

                        //check if limit is reaching from percentage set
                        limit.checkExceedWarning(isExpense);

                        //continue to add record
                        addRecord(record);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please enter details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    //prompt currency exchange dialog if wallet currency is not in SGD
    private void promptConversion(String baseCurrency, String walletCurrency){
        if (!walletCurrency.equals(baseCurrency)){
            CurrencyConvertDialog currencyConvertDialog = new CurrencyConvertDialog(this, walletCurrency.toLowerCase(), dbHandler);
            currencyConvertDialog.setAmt(amt);
            currencyConvertDialog.show();
        }
    }

    //values used for checking if record is valid
    private HashMap<String, Boolean> initCheckValues(){
        HashMap<String, Boolean> m = new HashMap<String, Boolean>();
        m.put("amount", false);
        m.put("title", false);
        m.put("category", false);
        return m;
    }

    //check if amount is valid
    private double checkAmt(){
        String amt_txt = amt.getText().toString();
        //set checkValues to true if valid
        if (amt_txt.length() > 0){
            checkValues.put("amount", true);
            return Double.parseDouble(amt_txt);
        }
        return 0;
    }

    //check if title is valid
    private String checkTitle(){
        String title_txt = title.getText().toString();
        //set checkValues to true title is not more than 15 char and not 0
        if (title_txt.length() > 15){
            title_layout.setError("Character limit exceeded");
            return null;
        }
        else if (title_txt.length() == 0){
            title_layout.setError("Please enter a title");
            return null;
        }
        else{
            checkValues.put("title", true);
            return title_txt;
        }
    }

    //check if category is selected
    private String checkCat(){
        String cat = selectedCat.getText().toString();
        //set checkValues to true when category is selected
        if (!cat.equals("Select a Category")){
            checkValues.put("category", true);
            return cat;
        }
        else{
            return null;
        }
    }

    //check all values in checkValues to validate inputs
    private boolean validRecord(){
        boolean valid = true;
        for (String key : checkValues.keySet()) {
            if (!checkValues.get(key)){
                valid = false;
            }
        }
        return valid;
    }

    //remove selected image and change message
    private void removeImg(ImageView image, TextView text){
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageData = null;
                text.setText("Select an Image");
                image.setImageResource(R.drawable.ic_image_24);
            }
        });
    }

    private void notifyLimit(Record record){
        MyAlertDialog dialog = new MyAlertDialog(AddRecordActivity.this);
        dialog.setTitle("Limit Exceeded");
        dialog.setBody("You will exceed your daily limit.\nDo you want to proceed?");

        dialog.setPositive().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecord(record);
                dialog.dismiss();
            }
        });

        dialog.setNegative().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void addRecord(Record record){
        dbHandler.addRecord(record);
        Toast.makeText(getApplicationContext(), "Transaction added", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void initToolbar(){
        //Tool bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        activityTitle.setText("Add Transaction");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}