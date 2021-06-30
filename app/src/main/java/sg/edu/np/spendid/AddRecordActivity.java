//Created by Glenn

package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddRecordActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private TextView selectedCat, selectWallet, recordCur;
    private EditText title, description, amt;
    private FloatingActionButton fab;
    private TextInputLayout title_layout;
    private HashMap<String, Boolean> checkValues;
    private String walletCurrency;

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

        checkValues = initCheckValues();
        Intent intent = getIntent();
        selectWallet.setText(intent.getStringExtra("walletName"));
        walletCurrency = intent.getStringExtra("walletCurrency");
        recordCur.setText("SGD");
        promptConversion();

        RecyclerView catRV = findViewById(R.id.newRecordCat_RV);
        CatSliderAdapter myCatAdapter = new CatSliderAdapter(dbHandler.getCategories(), selectedCat);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        catRV.setLayoutManager(myLayoutManager);
        myLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        catRV.setItemAnimator(new DefaultItemAnimator());
        catRV.setAdapter(myCatAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title_txt = checkTitle();
                String des_txt = description.getText().toString();
                String cat = checkCat();
                double amount = checkAmt();
                Calendar currentTime = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                String date = dateFormat.format(currentTime.getTime());
                String time = timeFormat.format(currentTime.getTime());

                if (validRecord()){
                    dbHandler.addRecord(new Record(title_txt, des_txt, amount, cat, date, time, intent.getIntExtra("walletId", 0)));
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


    private void promptConversion(){
        if (!walletCurrency.equals("SGD")){
            CurrencyConvertDialog currencyConvertDialog = new CurrencyConvertDialog(this, walletCurrency.toLowerCase());
            currencyConvertDialog.setAmt(amt);
            currencyConvertDialog.show();
        }
    }

    private HashMap<String, Boolean> initCheckValues(){
        HashMap<String, Boolean> m = new HashMap<String, Boolean>();
        m.put("amount", false);
        m.put("title", false);
        m.put("category", false);
        return m;
    }

    private double checkAmt(){
        String amt_txt = amt.getText().toString();
        if (amt_txt.length() > 0){
            checkValues.put("amount", true);
            return Double.parseDouble(amt_txt);
        }
        return 0;
    }

    private String checkTitle(){
        String title_txt = title.getText().toString();
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

    private String checkCat(){
        String cat = selectedCat.getText().toString();
        if (!cat.equals("Select a Category")){
            checkValues.put("category", true);
            return cat;
        }
        else{
            return null;
        }
    }

    private boolean validRecord(){
        boolean valid = true;
        for (String key : checkValues.keySet()) {
            if (!checkValues.get(key)){
                valid = false;
            }
        }
        return valid;
    }


}