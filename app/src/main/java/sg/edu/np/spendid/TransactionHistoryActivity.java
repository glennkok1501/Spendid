package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class TransactionHistoryActivity extends AppCompatActivity {
    private EditText search;
    private DBHandler dbHandler;
    private final static String PREF_NAME = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        search = findViewById(R.id.search_History_editText);
        dbHandler = new DBHandler(this, null,null, 1);

        //Tool Bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        activityTitle.setText("Transaction History");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String baseCurrency = prefs.getString("baseCurrency", "");
        RecyclerView transactionHistoryRV = findViewById(R.id.transaction_history_recyclerView);
        HistoryAdaptor ha = new HistoryAdaptor(dbHandler.getRecordHistory(), baseCurrency);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        transactionHistoryRV.setLayoutManager(lm);
        transactionHistoryRV.setItemAnimator(new DefaultItemAnimator());
        transactionHistoryRV.setAdapter(ha);
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
}