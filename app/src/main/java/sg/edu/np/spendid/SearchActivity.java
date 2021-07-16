package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentProviderClient;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SearchActivity extends AppCompatActivity {
    private ArrayList<String> searchParams = new ArrayList<>();
    private String[] defaultParams = new String[]{"Name", "Desc", "Category"};
    private ArrayList<RadioButton> optionRadios = new ArrayList<>();
    private ArrayList<Record> records;
    boolean checked;
    private TransactionAdapter ta;
    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    private TextView advanced, dateToSearch;
    private EditText search;
    private ImageView searchBtn;
    private RadioButton nameR, descR, catR, walletR, amtR, dateR;
    private CalendarView dateChosen;
    private LinearLayout options;
    private DBHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //setting views
        advanced = findViewById(R.id.search_Advanced_textView);
        search = findViewById(R.id.search_Record_editText);
        searchBtn = findViewById(R.id.search_Btn_imageView);
        nameR = findViewById(R.id.radioName);
        descR = findViewById(R.id.radioDesc);
        catR = findViewById(R.id.radioCategory);
        walletR = findViewById(R.id.radioWallet);
        amtR = findViewById(R.id.radioAmt);
        dateR = findViewById(R.id.radioDate);
        dateChosen= findViewById(R.id.search_calendarView);
        dateToSearch = findViewById(R.id.dateToSearch_textView);
        options = findViewById(R.id.options_linearLayout);
        optionRadios.add(nameR);
        optionRadios.add(descR);
        optionRadios.add(catR);
        optionRadios.add(walletR);
        optionRadios.add(amtR);
        optionRadios.add(dateR);
        dbHandler = new DBHandler(this, null,null, 1);

        initToolbar(); //set toolbar
        options.setVisibility(View.GONE); //hide advanced search options
        dateToSearch.setVisibility(View.GONE);
        dateChosen.setVisibility(View.GONE);
        //search.requestFocus(); //start keyboard

        advanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (options.getVisibility() == View.GONE) {
                    options.setVisibility(View.VISIBLE);
                } else {
                    options.setVisibility(View.GONE);
                    searchParams.toArray(defaultParams);
                }

            }
        });

        for (RadioButton r : optionRadios) {
            r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (searchParams.contains(r.getText().toString())) {
                        searchParams.remove(r.getText().toString());
                    } else {
                        searchParams.add(r.getText().toString());
                    }
                    checked = !checked;
                }
            });

        }

        dateR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateToSearch.getVisibility() == View.GONE) {
                    //dateToSearch.setText(dateChosen.getDate());
                    dateToSearch.setVisibility(View.VISIBLE);
                    dateChosen.setVisibility(View.VISIBLE);
                } else {
                    dateToSearch.setVisibility(View.GONE);
                    dateChosen.setVisibility(View.GONE);
                }

            }
        });

        //filters as search string changes
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0) {
                    searchString(search.getText().toString(), searchParams);
                    searchBtn.setImageResource(R.drawable.ic_clear_24);
                    searchBtn.setColorFilter(getResources().getColor(R.color.light_grey));
                    searchBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            search.getText().clear();
                        }
                    });
                }
                else{
                    searchString("", searchParams);
                    searchBtn.setImageResource(R.drawable.ic_search_24);
                    searchBtn.setColorFilter(getResources().getColor(R.color.light_grey));
                    searchBtn.setOnClickListener(null);
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
        records = dbHandler.getAllRecords();
        RecyclerView searchRv = findViewById(R.id.search_recyclerView);
        ta = new TransactionAdapter(records, true);
        LinearLayoutManager lm = new LinearLayoutManager(SearchActivity.this);
        searchRv.setLayoutManager(lm);
        searchRv.setItemAnimator(new DefaultItemAnimator());
        searchRv.setAdapter(ta);
        ta.notifyDataSetChanged();
        search.getText().clear();
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

    private void searchString(String s, ArrayList<String> searchParams) {
        ArrayList<Record> out = new ArrayList<>();
        String details = "";
        //Concatenating record details into a single string for all records, into a single list
        for (Record r : records) {
            for (String param : searchParams) {
                switch (param) {
                    case "Name":
                        details += r.getTitle() + " ";
                    case "Desc":
                        details += r.getDescription() + " ";
                    case "Category":
                        details += r.getCategory() + " ";
                    case "Wallet":
                        details += r.getWalletId() + " ";
                    case "Amt":
                        details += r.getAmount() + " ";
                    case "Date":
                        details += r.getDateCreated() + " ";
                }
            }
            if (details.toLowerCase().contains(s.toLowerCase())) {
                out.add(r);
            }
        }
        ta.filter(out);
    }

    private void initToolbar(){
        //Tool bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        activityTitle.setText("Search Transactions");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });
    }

}