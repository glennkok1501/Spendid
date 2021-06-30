package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.util.HashMap;

public class EditRecordActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private Wallet wallet;
    private Record record;
    private TextView selectedCat, selectWallet, recordCur;
    private EditText title, description, amt;
    private FloatingActionButton fab;
    private TextInputLayout title_layout;
    private HashMap<String, Boolean> checkValues;
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
        checkValues = initCheckValues();
        Intent intent = getIntent();
        record  = dbHandler.getRecord(intent.getIntExtra("recordId", 0));
        wallet = dbHandler.getWallet(record.getWalletId());
        recordCur.setText("SGD");
        selectWallet.setText(wallet.getName());
        title.setText(record.getTitle());
        description.setText(record.getDescription());
        amt.setText(df2.format(record.getAmount()));
        selectedCat.setText(record.getCategory());

        RecyclerView catRV = findViewById(R.id.editRecordCat_RV);
        CatSliderAdapter myCatAdapter = new CatSliderAdapter(dbHandler.getCategories(), selectedCat);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        catRV.setLayoutManager(myLayoutManager);
        myLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        catRV.setItemAnimator(new DefaultItemAnimator());
        catRV.setAdapter(myCatAdapter);

        promptConversion();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title_txt = checkTitle();
                String des_txt = description.getText().toString();
                String cat = checkCat();
                double amount = checkAmt();

                if (validRecord()){
                    dbHandler.updateRecord(new Record(record.getId(), title_txt, des_txt, amount, cat, record.getDateCreated(), record.getTimeCreated(), record.getWalletId()));
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


    private void promptConversion(){
        if (!wallet.getCurrency().equals("SGD")){
            CurrencyConvertDialog currencyConvertDialog = new CurrencyConvertDialog(this, wallet.getCurrency().toLowerCase());
            currencyConvertDialog.setAmt(amt);
            currencyConvertDialog.setForFixedAmt(record.getAmount());
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

}