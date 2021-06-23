package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TransactionHistoryActivity extends AppCompatActivity {
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        toggleNightMode();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        dbHandler = new DBHandler(this, null,null, 1);
        FloatingActionButton fab = findViewById(R.id.transaction_history_fab);

        //Tool bar
        TextView activityTitle = findViewById(R.id.mainToolbarTitle_textView);
        ImageView backArrow = findViewById(R.id.mainToolbarMenu_imageView);
        ImageView search = findViewById(R.id.mainToolbarMore_imageView);
        backArrow.setImageResource(R.drawable.ic_back_arrow_32);
        search.setImageResource(R.drawable.ic_search_32);
        activityTitle.setText("Trasaction History");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionHistoryActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionHistoryActivity.this, SelectWalletActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String PREF_NAME = "sharedPrefs";
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String baseCurrency = prefs.getString("baseCurrency", "");
        RecyclerView transactionHistoryRV = findViewById(R.id.transaction_history_recyclerView);
        HistoryAdapter ha = new HistoryAdapter(dbHandler.getRecordHistory(), baseCurrency);
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

    private void toggleNightMode(){
        if (getSharedPreferences("sharedPrefs", MODE_PRIVATE).getBoolean("nightMode", false)){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}