package sg.edu.np.spendid.RecurringEntry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Dialogs.CurrencyConvertDialog;
import sg.edu.np.spendid.Models.Record;
import sg.edu.np.spendid.Models.Recurring;
import sg.edu.np.spendid.Models.Wallet;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.RecurringEntry.Adapters.RecurCatSliderAdapter;

public class AddRecurringEntry extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private DBHandler dbHandler;
    private TextView selectedCat, selectWallet, recurringCur;
    private EditText title, description, amt;
    private FloatingActionButton fab;
    private TextInputLayout title_layout;
    private HashMap<String, Boolean> checkValues;
    private String walletCurrency;
    private String baseCurrency;
    TextView selectDate;
    private Wallet wallet;
    private ArrayList<Wallet> walletArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recurring_entry);
        dbHandler = new DBHandler(this, null, null, 1);
        selectedCat = findViewById(R.id.newRecurringCat_textView);
        title = findViewById(R.id.newRecurringTitle_editText);
        amt = findViewById(R.id.newRecurringAmt_editText);
        description = findViewById(R.id.newRecurringDes_editText);
        fab = findViewById(R.id.newRecurring_fab);
        title_layout = findViewById(R.id.newRecurringTitle_layout);
        recurringCur = findViewById(R.id.newRecurringCur_textView);
        baseCurrency = "SGD";
        selectDate = findViewById(R.id.newRecurringDate_textView);
        recurringCur.setText(baseCurrency);
        walletArrayList = dbHandler.getWallets();
        String[] walletList = getWalletList();
        initToolbar(); //set toolbar
        //promptConversion();

        checkValues = initCheckValues();


        //category slider
        RecyclerView catRV = findViewById(R.id.newRecurringCat_RV);
        RecurCatSliderAdapter myCatAdapter = new RecurCatSliderAdapter(dbHandler.getCategories(), selectedCat);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        catRV.setLayoutManager(myLayoutManager);
        myLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        catRV.setItemAnimator(new DefaultItemAnimator());
        catRV.setAdapter(myCatAdapter);

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sg.edu.np.spendid.RecurringEntry.DatePicker mDatePickerDialogFragment;
                mDatePickerDialogFragment = new sg.edu.np.spendid.RecurringEntry.DatePicker();
                mDatePickerDialogFragment.show(getSupportFragmentManager(), "Pick Date");

            }
        });

        Spinner spinner = findViewById(R.id.recurring_wallet_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, walletList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if (walletArrayList.size() > 0) {
            wallet = walletArrayList.get(spinner.getSelectedItemPosition());
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    wallet = walletArrayList.get(spinner.getSelectedItemPosition());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // pass
                }
            });
        } else {
            wallet = null;
            Toast.makeText(getApplicationContext(), "No wallets available", Toast.LENGTH_SHORT).show();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title_txt = checkTitle(); //validate title
                String des_txt = description.getText().toString(); //get description
                String cat = checkCat(); //validate category
                double amount = checkAmt(); //validate amount
                String Date = checkDate();

                //create transaction if record is valid
                if (validRecord()) {
                    dbHandler.addRecurring(new Recurring(title_txt, des_txt, amount, cat, Date, null, null, wallet.getWalletId()));
                    Toast.makeText(getApplicationContext(), "Recurring Entry added", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter details", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initToolbar() {
        //Tool bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        activityTitle.setText("Recurring Details");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar mCalender = Calendar.getInstance();
        mCalender.set(Calendar.YEAR, year);
        mCalender.set(Calendar.MONTH, month);
        mCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(mCalender.getTime());
        selectDate.setText(selectedDate);
    }

    private String[] getWalletList() {
        String[] walletList = new String[walletArrayList.size()];
        for (int i = 0; i < walletArrayList.size(); i++) {
            walletList[i] = walletArrayList.get(i).getName();
        }
        return walletList;
    }

    private double checkAmt() {
        String amt_txt = amt.getText().toString();
        //set checkValues to true if valid
        if (amt_txt.length() > 0) {
            checkValues.put("amount", true);
            return Double.parseDouble(amt_txt);
        }
        return 0;
    }

    //check if title is valid
    private String checkTitle() {
        String title_txt = title.getText().toString();
        //set checkValues to true title is not more than 15 char and not 0
        if (title_txt.length() > 15) {
            title_layout.setError("Character limit exceeded");
            return null;
        } else if (title_txt.length() == 0) {
            title_layout.setError("Please enter a title");
            return null;
        } else {
            checkValues.put("title", true);
            return title_txt;
        }
    }

    //check if category is selected
    private String checkCat() {
        String cat = selectedCat.getText().toString();
        //set checkValues to true when category is selected
        if (!cat.equals("Select a Category")) {
            checkValues.put("category", true);
            return cat;
        } else {
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

    private String checkDate(){
        String date = selectDate.getText().toString();
        if (date == null){
            return null;
        }
        else{
            checkValues.put("date", true);
            return date;
        }
    }

    private HashMap<String, Boolean> initCheckValues(){
        HashMap<String, Boolean> m = new HashMap<String, Boolean>();
        m.put("amount", false);
        m.put("title", false);
        m.put("category", false);
        m.put("date", false);
        return m;
    }

}

