package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {
    private ArrayList<String> recordDetails = new ArrayList<>();
    private EditText search;
    private ImageView searchBtn;
    private DBHandler dbHandler;
    private ArrayList<Record> records;
    private TransactionAdaptor ta;
    private final static String PREF_NAME = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search = findViewById(R.id.search_Record_editText);
        searchBtn = findViewById(R.id.search_Btn_imageView);
        dbHandler = new DBHandler(this, null,null, 1);

        //Tool bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        activityTitle.setText("Search Transactions");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Search for record
                searchString(search.getText().toString());
                Log.v("TAG", "Searching");
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        records = dbHandler.getAllRecords();
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String baseCurrency = prefs.getString("baseCurrency", "");
        RecyclerView searchRv = findViewById(R.id.search_recyclerView);
        ta = new TransactionAdaptor(records, baseCurrency);
        LinearLayoutManager lm = new LinearLayoutManager(SearchActivity.this);
        searchRv.setLayoutManager(lm);
        searchRv.setItemAnimator(new DefaultItemAnimator());
        searchRv.setAdapter(ta);
        ta.notifyDataSetChanged();
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

    private void searchString(String s) {
        ArrayList<Record> out = new ArrayList<>();
        //Concatenating record details into a single string for all records, into a single list
        for (Record r : records) {
            String details = r.getTitle() + " " + r.getDescription() + " " + r.getCategory();
            if (details.toLowerCase().contains(s.toLowerCase())) {
                out.add(r);
                Log.v("TAG", "True");
            }
        }
        ta.filter(out);
    }
}