package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;

public class TransactionHistoryActivity extends AppCompatActivity {
    private EditText search;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        search = findViewById(R.id.search_History_editText);
        dbHandler = new DBHandler(this, null,null, 1);

        RecyclerView transactionHistoryRV = findViewById(R.id.transaction_history_recyclerView);
        HistoryAdaptor ha = new HistoryAdaptor(dbHandler.getRecordHistory());
        LinearLayoutManager lm = new LinearLayoutManager(this);
        transactionHistoryRV.setLayoutManager(lm);
        transactionHistoryRV.setItemAnimator(new DefaultItemAnimator());
        transactionHistoryRV.setAdapter(ha);
    }
}