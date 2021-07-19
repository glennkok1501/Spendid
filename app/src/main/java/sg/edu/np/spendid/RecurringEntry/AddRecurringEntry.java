package sg.edu.np.spendid.RecurringEntry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.DatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Dialogs.CurrencyConvertDialog;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.RecurringEntry.Adapters.RecurCatSliderAdapter;

public class AddRecurringEntry extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private DBHandler dbHandler;
    private TextView selectedCat, selectWallet, recurringCur;
    private EditText title, description, amt;
    private FloatingActionButton fab;
    private TextInputLayout title_layout;
    private HashMap<String, Boolean> checkValues;
    private String walletCurrency;
    private String baseCurrency;
    TextView selectDate;

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
        initToolbar(); //set toolbar
        //promptConversion();

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

    }

    private void initToolbar(){
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

//    private void promptConversion(){
//        if (!walletCurrency.equals(baseCurrency)){
//            CurrencyConvertDialog currencyConvertDialog = new CurrencyConvertDialog(this, walletCurrency.toLowerCase());
//            currencyConvertDialog.setAmt(amt);
//            currencyConvertDialog.show();
//        }
//    }

}