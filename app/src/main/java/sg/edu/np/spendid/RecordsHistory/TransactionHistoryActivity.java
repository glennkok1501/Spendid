package sg.edu.np.spendid.RecordsHistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Records.AddRecordActivity;
import sg.edu.np.spendid.RecordsHistory.Adapters.HistoryAdapter;
import sg.edu.np.spendid.Models.Record;
import sg.edu.np.spendid.R;

public class TransactionHistoryActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private TextView emptyTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        dbHandler = new DBHandler(this, null,null, 1);
        FloatingActionButton fab = findViewById(R.id.transaction_history_fab);
        emptyTrans = findViewById(R.id.transaction_history_textView);
        boolean emptyWallet = dbHandler.getWallets().size() == 0;

        initToolbar(); //set toolbar

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emptyWallet){
                    Toast.makeText(getApplicationContext(), "Please create a wallet", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(TransactionHistoryActivity.this, AddRecordActivity.class);
                    startActivity(intent);
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
        RecyclerView transactionHistoryRV = findViewById(R.id.transaction_history_recyclerView);
        HashMap<String, ArrayList<Record>> historyRecord = dbHandler.getRecordHistory();
        checkEmpty(historyRecord);
        HistoryAdapter ha = new HistoryAdapter(historyRecord);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        transactionHistoryRV.setLayoutManager(lm);
        transactionHistoryRV.setItemAnimator(new DefaultItemAnimator());
        transactionHistoryRV.setAdapter(ha);
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

    private void initToolbar(){
        //Tool bar
        TextView activityTitle = findViewById(R.id.toolbarTitle_textView);
        ImageView backArrow = findViewById(R.id.toolbarBtn_imageView1);
        ImageView search = findViewById(R.id.toolbarBtn_imageView2);
        backArrow.setImageResource(R.drawable.ic_back_arrow_32);
        search.setImageResource(R.drawable.ic_search_32);
        activityTitle.setText("Transaction History");
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
    }

    private void checkEmpty(HashMap<String, ArrayList<Record>> historyRecord){
        if (historyRecord.size() > 0){
            emptyTrans.setVisibility(View.GONE);
        }
        else{
            emptyTrans.setVisibility(View.VISIBLE);
            emptyTrans.setText("You have no transactions");
        }
    }

}