package sg.edu.np.spendid.RecurringEntry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.ArrayList;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import sg.edu.np.spendid.Dialogs.MyAlertDialog;
import sg.edu.np.spendid.Records.Adapters.CatSliderAdapter;
import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Models.Wallet;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Dialogs.MyAlertDialog;
import sg.edu.np.spendid.Models.Recurring;
import sg.edu.np.spendid.Models.Wallet;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Records.Adapters.CatSliderAdapter;

public class EditRecurringEntry extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private DBHandler dbHandler;
    private Recurring recurring;
    private Wallet wallet;
    private TextView selectedCat, recordCur, date,selectDate, walletName;
    private EditText title, description, amt;
    private FloatingActionButton fab;
    private TextInputLayout title_layout;
    private HashMap<String, Boolean> checkValues;
    private String baseCurrency, frequency;
    private final DecimalFormat df2 = new DecimalFormat("#0.00");
    private ArrayList<String> frequencyArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recurring_entry);
        dbHandler = new DBHandler(this, null, null, 1);
        selectedCat = findViewById(R.id.editRecurringCat_textView);
        title = findViewById(R.id.editRecurringTitle_editText);
        amt = findViewById(R.id.editRecurringAmt_editText);
        description = findViewById(R.id.editRecurringDes_editText);
        fab = findViewById(R.id.editRecurring_fab);
        title_layout = findViewById(R.id.editRecurringTitle_layout);
        recordCur = findViewById(R.id.editRecurringCur_textView);
        selectDate = findViewById(R.id.editRecurringDate_textView);
        walletName = findViewById(R.id.editRecurringWallet_textView);

        baseCurrency = "SGD";
        initToolbar(); //set toolbar
        checkValues = initCheckValues();

        Intent intent = getIntent();

        recurring = dbHandler.getRecurring(intent.getIntExtra("recurID",0));
        wallet = dbHandler.getWallet(recurring.getWalletId());
        walletName.setText(wallet.getName());
        recordCur.setText(baseCurrency);
        title.setText(recurring.getRecurringtitle());
        description.setText(recurring.getRecurringdescription());
        amt.setText(df2.format(recurring.getAmount()));
        selectedCat.setText(recurring.getCategory());
        selectDate.setText(recurring.getRecurringstartDate());

        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateupdate = dateFormat.format(currentTime.getTime());

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sg.edu.np.spendid.RecurringEntry.DatePicker mDatePickerDialogFragment;
                mDatePickerDialogFragment = new sg.edu.np.spendid.RecurringEntry.DatePicker();
                mDatePickerDialogFragment.show(getSupportFragmentManager(), "Pick Date");
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title_txt = checkTitle(); //validate title
                String des_txt = description.getText().toString(); //get description
                String cat = checkCat(); //validate category
                double amount = checkAmt(); //validate amount
                String date;
                try {
                    date = checkDate();
                } catch (ParseException e) {
                    e.printStackTrace();
                    date = recurring.getRecurringstartDate();
                }


                if (validRecord()) {
                    dbHandler.UpdateRecurring(new Recurring(recurring.getRecurringId(),title_txt, des_txt, amount, cat, date, null, dateupdate, recurring.getWalletId(),recurring.getFrequency()));
                    Toast.makeText(getApplicationContext(), "Recurring Entry Updated", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RecyclerView catRV = findViewById(R.id.editRecurringCat_RV);
        CatSliderAdapter myCatAdapter = new CatSliderAdapter(dbHandler.getCategories(), selectedCat);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        catRV.setLayoutManager(myLayoutManager);
        myLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        catRV.setItemAnimator(new DefaultItemAnimator());
        catRV.setAdapter(myCatAdapter);

    }
    private void initToolbar(){
        //Tool bar
        TextView activityTitle = findViewById(R.id.toolbarTitle_textView);
        ImageView btn1 = findViewById(R.id.toolbarBtn_imageView1);
        ImageView btn2 = findViewById(R.id.toolbarBtn_imageView2);
        btn1.setImageResource(R.drawable.ic_back_arrow_32);
        activityTitle.setText("Edit Recurring Entry");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initPopupMenu(btn2);
    }


    private void initPopupMenu(ImageView moreBtn){
        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(EditRecurringEntry.this, moreBtn);
                popupMenu.getMenu().add("Stop");
                popupMenu.getMenu().add("Delete");

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getTitle().toString()){
                            case "Delete":
                                deleteDialog();
                                break;
                            case "Stop":
                                stopDate();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    private void stopDate(){
        MyAlertDialog alert = new MyAlertDialog(this);
        alert.setTitle("Stop Recurring Entry");
        alert.setBody("Are you sure you want to stop this Recurring Entry?");
        alert.setPositive().setText("Stop");

        alert.setPositive().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateupdate = dateFormat.format(currentTime.getTime());
                dbHandler.UpdateRecurring(new Recurring(recurring.getRecurringId(),recurring.getRecurringtitle(), recurring.getRecurringdescription(), recurring.getAmount(), recurring.getCategory(), recurring.getRecurringstartDate(), dateupdate, dateupdate, recurring.getWalletId(), recurring.getFrequency()));
                Toast.makeText(getApplicationContext(), "Recurring Entry Stopped", Toast.LENGTH_SHORT).show();
                alert.dismiss();
                finish();
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

    private void deleteDialog(){
        MyAlertDialog alert = new MyAlertDialog(this);
        alert.setTitle("Delete Recurring Entry");
        alert.setBody("Are you sure you want to delete this Recurring Entry?");
        alert.setPositive().setText("Delete");
        alert.setPositive().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler.deleteRecurring(recurring.getRecurringId());
                Toast.makeText(getApplicationContext(), "Recurring Entry Deleted", Toast.LENGTH_SHORT).show();
                alert.dismiss();
                finish();
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


    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        Calendar mCalender = Calendar.getInstance();
        mCalender.set(Calendar.YEAR, year);
        mCalender.set(Calendar.MONTH, month);
        mCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = DateFormat.getDateInstance().format(mCalender.getTime());
        selectDate.setText(selectedDate);
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

    private String checkDate() throws ParseException {
        String date = selectDate.getText().toString();
        checkValues.put("date", true);
        Log.v("TESTTESTTEST", "" + formatDate(date));
        return formatDate(date);
    }

    private HashMap<String, Boolean> initCheckValues(){
        HashMap<String, Boolean> m = new HashMap<String, Boolean>();
        m.put("amount", false);
        m.put("title", false);
        m.put("category", false);
        m.put("date", false);
        return m;
    }

    private String formatDate(String d) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat initialFormat = new SimpleDateFormat("MMM d, yyyy");
        Date date = initialFormat.parse(d);
        return dateFormat.format(date);
    }

}