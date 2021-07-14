//Created by Glenn

package sg.edu.np.spendid;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

//Create transaction

public class AddRecordActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private TextView selectedCat, selectWallet, recordCur;
    private EditText title, description, amt;
    private FloatingActionButton fab;
    private TextInputLayout title_layout;
    private HashMap<String, Boolean> checkValues;
    private String walletCurrency;
    private String baseCurrency;
    private byte[] imageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
        dbHandler = new DBHandler(this, null, null, 1);
        selectedCat = findViewById(R.id.newRecordCat_textView);
        selectWallet = findViewById(R.id.newRecordWallet_textView);
        title = findViewById(R.id.newRecordTitle_editText);
        amt = findViewById(R.id.newRecordAmt_editText);
        description = findViewById(R.id.newRecordDes_editText);
        fab = findViewById(R.id.newRecord_fab);
        title_layout = findViewById(R.id.newRecordTitle_layout);
        recordCur = findViewById(R.id.newRecordCur_textView);
        TextView selImage = findViewById(R.id.newRecordImage_textView);
        ImageView imageStatus = findViewById(R.id.newRecordImage_imageView);
        baseCurrency = getString(R.string.baseCurrency);
        imageData = null;

        initToolbar(); //set toolbar

        checkValues = initCheckValues(); //set values for record validation

        //retrieve wallet name and wallet currency
        Intent intent = getIntent();
        selectWallet.setText(intent.getStringExtra("walletName"));
        walletCurrency = intent.getStringExtra("walletCurrency");
        recordCur.setText(baseCurrency);
        promptConversion(); //if wallet currency not sgd prompt exchange

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
                                renderImage(result, imageStatus, selImage);
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

        //create transaction when button is clicked
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title_txt = checkTitle(); //validate title
                String des_txt = description.getText().toString(); //get description
                String cat = checkCat(); //validate category
                double amount = checkAmt(); //validate amount
                //get current date and time
                Calendar currentTime = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                String date = dateFormat.format(currentTime.getTime());
                String time = timeFormat.format(currentTime.getTime());

                //create transaction if record is valid
                if (validRecord()){
                    dbHandler.addRecord(new Record(title_txt, des_txt, amount, cat, date, time, imageData, intent.getIntExtra("walletId", 0)));
                    Toast.makeText(getApplicationContext(), "Transaction added", Toast.LENGTH_SHORT).show();
                    finish();
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

    //prompt currency exchange dialog if wallet currency is not in SGD
    private void promptConversion(){
        if (!walletCurrency.equals(baseCurrency)){
            CurrencyConvertDialog currencyConvertDialog = new CurrencyConvertDialog(this, walletCurrency.toLowerCase());
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

    private void renderImage(Uri uri, ImageView image, TextView text) throws Exception {
            InputStream iStream = getContentResolver().openInputStream(uri);
            int maxSize = 8388608; //8Mb
            byte[] imageBytes = getBytes(iStream);
            if (imageBytes.length < maxSize){
                imageData = imageBytes;
                text.setText("Image Attached");
                image.setImageResource(R.drawable.ic_clear_24);
                removeImg(image, text);
            }
            else{
                Log.v("TAG", "File too Large");
                Toast.makeText(getApplicationContext(), "File too large: exceeded 8Mb", Toast.LENGTH_SHORT).show();
            }
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
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