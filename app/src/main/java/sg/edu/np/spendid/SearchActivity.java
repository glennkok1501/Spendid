package sg.edu.np.spendid;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.solver.GoalRow;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SearchActivity extends AppCompatActivity {
    private ArrayList<String> searchParams = new ArrayList<>();
    private String[] defaultParams = new String[]{"Name", "Description", "Category"};
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private ArrayList<RadioButton> optionRadios = new ArrayList<>();
    private ArrayList<Record> records;
    private TransactionAdapter ta;
    private TextView advanced, dateToSearch;
    private EditText search;
    private ImageView searchBtn;
    private RadioButton nameR, descR, catR, walletR, amtR, dateR;
    private DatePicker dateChosen;
    private CardView finalDate;
    private Button saveDate;
    private LinearLayout options;
    private DBHandler dbHandler;


    @RequiresApi(api = Build.VERSION_CODES.O)
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
        finalDate = findViewById(R.id.editSearchDate_cardView);
        dateToSearch = findViewById(R.id.dateToSearch_textView);
        dateChosen= findViewById(R.id.search_datePicker);
        saveDate = findViewById(R.id.saveDate_Btn);
        options = findViewById(R.id.options_linearLayout);

        optionRadios.add(nameR);
        optionRadios.add(descR);
        optionRadios.add(catR);
        optionRadios.add(walletR);
        optionRadios.add(amtR);
        //optionRadios.add(dateR);

        dbHandler = new DBHandler(this, null,null, 1);

        initToolbar(); //set toolbar
        options.setVisibility(View.GONE); //hide advanced search options
        try {
            dateChosen.setMinDate(new SimpleDateFormat("dd/MM/yyyy").
                    parse(dbHandler.getRecord(1).getDateCreated()).getTime());
        } catch (ParseException e) {}
        dateChosen.setMaxDate(new Date().getTime());
        //search.requestFocus(); //start keyboard

        //set default checked filter options
        //defaultParams can be modified in the future to allow user to set default search options
        for (String s : defaultParams) {
            searchParams.add(s);
        }

        advanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (options.getVisibility() == View.GONE) {
                    nameR.setChecked(true);
                    descR.setChecked(true);
                    catR.setChecked(true);
                    options.setVisibility(View.VISIBLE);
                    finalDate.setVisibility(View.GONE);
                    dateChosen.setVisibility(View.GONE);
                    saveDate.setVisibility(View.GONE);
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
                        if (searchParams.size() == 1) {
                            Toast.makeText(SearchActivity.this,
                                    "There must at least be 1 filter option chosen", Toast.LENGTH_SHORT);
                        } else {
                            searchParams.remove(r.getText().toString());
                            r.setChecked(false);
                        }
                    } else {
                        searchParams.add(r.getText().toString());
                        r.setChecked(true);
                    }
                }
            });
        }

        dateR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateChosen.getVisibility() == View.GONE) {
                    if (finalDate.getVisibility() == View.GONE) {
                        dateChosen.setVisibility(View.VISIBLE);
                        saveDate.setVisibility(View.VISIBLE);
                        searchParams.add(dateR.getText().toString());
                        dateR.setChecked(true);
                        return;
                    }
                }

                if (searchParams.size() == 1) {
                        Toast.makeText(SearchActivity.this,
                                "There must at least be 1 filter option chosen", Toast.LENGTH_SHORT);
                } else {
                    finalDate.setVisibility(View.GONE);
                    dateChosen.setVisibility(View.GONE);
                    saveDate.setVisibility(View.GONE);
                    searchParams.remove(dateR.getText().toString());
                    dateR.setChecked(false);
                }
            }
        });

        finalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateChosen.setVisibility(View.VISIBLE);
                saveDate.setVisibility(View.VISIBLE);
                finalDate.setVisibility(View.GONE);
            }
        });

        saveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int month = dateChosen.getMonth() + 1;
                dateToSearch.setText(dateChosen.getDayOfMonth() + "/" + month + "/" + dateChosen.getYear());
                finalDate.setVisibility(View.VISIBLE);
                dateChosen.setVisibility(View.GONE);
                saveDate.setVisibility(View.GONE);
                try {
                    Log.v("TAG", String.valueOf(df.parse(dateToSearch.getText().toString())));
                    searchString(String.valueOf(df.parse(dateToSearch.getText().toString())), searchParams);
                } catch (ParseException e) {}
            }
        });

        //filters as search string changes
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    searchString(search.getText().toString(), searchParams);
                    searchBtn.setImageResource(R.drawable.ic_clear_24);
                    searchBtn.setColorFilter(getResources().getColor(R.color.light_grey));
                    searchBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            search.getText().clear();
                        }
                    });
                } else {
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
        String details;
        //Concatenating record details into a single string for all records, into a single list
        //depending on chosen filter options
        for (Record r : records) {
            details = "";
            for (String param : searchParams) {
                switch (param) {
                    case "Name":
                        details += r.getTitle() + " ";
                        break;
                    case "Description":
                        details += r.getDescription() + " ";
                        break;
                    case "Category":
                        details += r.getCategory() + " ";
                        break;
                    case "Wallet":
                        details += dbHandler.getWallet(r.getWalletId()).getName() + " ";
                        break;
                    case "Amount":
                        details += r.getAmount() + " ";
                        break;
                    case "Date":
                        details += r.getDateCreated() + " ";
                        break;
                    default:
                        Toast.makeText(SearchActivity.this, "Invalid Filter", Toast.LENGTH_SHORT);
                }
                Log.v("TAG", details);
            }
            try {
                if (details.toLowerCase().contains(s.toLowerCase() + df.parse(dateToSearch.getText().toString()))) {
                    out.add(r);
                }
            } catch (ParseException e) {}
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