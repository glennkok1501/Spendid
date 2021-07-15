package sg.edu.np.spendid.Records;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.util.HashMap;

import sg.edu.np.spendid.Dialogs.AlertDialog;
import sg.edu.np.spendid.Records.Adapters.CatSliderAdapter;
import sg.edu.np.spendid.Dialogs.CurrencyConvertDialog;
import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Models.Record;
import sg.edu.np.spendid.Models.Wallet;

//edit and delete transaction
public class EditRecordActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private Wallet wallet;
    private Record record;
    private TextView selectedCat, selectWallet, recordCur;
    private EditText title, description, amt;
    private FloatingActionButton fab;
    private TextInputLayout title_layout;
    private HashMap<String, Boolean> checkValues;
    private String baseCurrency;
    private byte[] imageData;
    private final DecimalFormat df2 = new DecimalFormat("#0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_record);
        dbHandler = new DBHandler(this, null, null, 1);
        selectedCat = findViewById(R.id.editRecordCat_textView);
        selectWallet = findViewById(R.id.editRecordWallet_textView);
        title = findViewById(R.id.editRecordTitle_editText);
        amt = findViewById(R.id.editRecordAmt_editText);
        description = findViewById(R.id.editRecordDes_editText);
        fab = findViewById(R.id.editRecord_fab);
        title_layout = findViewById(R.id.editRecordTitle_layout);
        recordCur = findViewById(R.id.editRecordCur_textView);
        ImageView imageStatus = findViewById(R.id.editRecordImage_imageView);
        TextView selImage = findViewById(R.id.editRecordImage_textView);
        baseCurrency = getString(R.string.baseCurrency);
        initToolbar(); //set toolbar

        checkValues = initCheckValues(); //set values to check for fields

        //retrieve record id
        Intent intent = getIntent();
        record  = dbHandler.getRecord(intent.getIntExtra("recordId", 0));
        wallet = dbHandler.getWallet(record.getWalletId()); //get wallet by record Id
        recordCur.setText(baseCurrency);
        selectWallet.setText(wallet.getName());
        title.setText(record.getTitle());
        description.setText(record.getDescription());
        amt.setText(df2.format(record.getAmount()));
        selectedCat.setText(record.getCategory());
        checkImage(imageStatus, selImage);

        RecyclerView catRV = findViewById(R.id.editRecordCat_RV);
        CatSliderAdapter myCatAdapter = new CatSliderAdapter(dbHandler.getCategories(), selectedCat);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        catRV.setLayoutManager(myLayoutManager);
        myLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        catRV.setItemAnimator(new DefaultItemAnimator());
        catRV.setAdapter(myCatAdapter);

        promptConversion(); //if wallet currency not sgd prompt exchange

        //Open file picker
        ActivityResultLauncher<String> getFile = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        //import the file if result of file is chosen
                        if (result != null){
                            try {
                                imageData = new ProcessImage(EditRecordActivity.this).render(result);
                                if (imageData != null){
                                    selImage.setText("Image Attached");
                                    imageStatus.setImageResource(R.drawable.ic_clear_24);
                                    removeImg(imageStatus, selImage);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Unable to save image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        selImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFile.launch("image/*"); //initiate filer picker with any file type
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title_txt = checkTitle(); //validate title
                String des_txt = description.getText().toString(); //get description
                String cat = checkCat(); //validate category
                double amount = checkAmt(); //validate amount
                //update transaction if record is valid
                if (validRecord()){
                    dbHandler.updateRecord(new Record(record.getId(), title_txt, des_txt, amount, cat, record.getDateCreated(), record.getTimeCreated(), imageData, record.getWalletId()));
                    Toast.makeText(getApplicationContext(), "Transaction Saved", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please fill in", Toast.LENGTH_SHORT).show();
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

    //prompt currency exchange dialog if wallet currency is not in SGD
    private void promptConversion(){
        if (!wallet.getCurrency().equals(baseCurrency)){
            CurrencyConvertDialog currencyConvertDialog = new CurrencyConvertDialog(this, wallet.getCurrency().toLowerCase());
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

    //initiate alert dialog for deletion of transaction
    private void deleteDialog(){
        AlertDialog alert = new AlertDialog(this);
        alert.setTitle("Delete Transaction");
        alert.setBody("Are you sure you want to permanently delete this transaction?");
        alert.setPositive().setText("Delete");
        alert.setPositive().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecord();
                alert.dismiss();
            }
        });
        alert.setNegative().setText("Cancel");
        alert.setNegative().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    private void deleteRecord(){
        if (dbHandler.deleteRecord(record.getId())){
            Toast.makeText(getApplicationContext(), "Transaction Deleted", Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(), "Unknown Transaction", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkImage(ImageView image, TextView text){
        if (record.getImage() != null){
            imageData = record.getImage();
            text.setText("Image Attached");
            image.setImageResource(R.drawable.ic_clear_24);
            removeImg(image, text);
        }
        else{
            imageData = null;
        }
    }

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

    private void initToolbar(){
        //Tool bar
        TextView activityTitle = findViewById(R.id.mainToolbarTitle_textView);
        ImageView backArrow = findViewById(R.id.mainToolbarMenu_imageView);
        ImageView trash = findViewById(R.id.mainToolbarMore_imageView);
        backArrow.setImageResource(R.drawable.ic_clear_32);
        trash.setImageResource(R.drawable.ic_delete_32);
        activityTitle.setText("Edit Transaction");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog();
            }
        });
    }
}