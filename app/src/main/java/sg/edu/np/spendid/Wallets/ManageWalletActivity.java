package sg.edu.np.spendid.Wallets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Models.Wallet;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Wallets.Adapters.WalletManageAdapter;

public class ManageWalletActivity extends AppCompatActivity {
    private final DecimalFormat df2 = new DecimalFormat("#0.00");
    private DBHandler dbHandler;
    private TextView bal, emptyWallets;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_wallet);
        dbHandler = new DBHandler(this, null, null, 1);
        bal = findViewById(R.id.totalWalletBal_textView);
        TextView header = findViewById(R.id.totalWalletTitle_textView);
        emptyWallets = findViewById(R.id.manageWallet_empty_textView);
        recyclerView = findViewById(R.id.manageWallet_RV);
        header.setText("Total Balance");

        initToolbar(); //set toolbar

        FloatingActionButton addWalletBtn = findViewById(R.id.manageWallet_fab);
        addWalletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageWalletActivity.this, WalletCurrencyActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        bal.setText(df2.format(dbHandler.getTotalBalance()));
        ArrayList<Wallet> walletArrayList = dbHandler.getWallets();
        checkEmpty(walletArrayList);
        WalletManageAdapter myAdapter = new WalletManageAdapter(walletArrayList, this);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(myLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);
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

    private void initToolbar(){
        //Tool bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        activityTitle.setText("Manage Wallets");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void checkEmpty(ArrayList<Wallet> walletArrayList){
        if (walletArrayList.size() > 0){
            emptyWallets.setVisibility(View.GONE);
        }
        else{
            emptyWallets.setVisibility(View.VISIBLE);
            emptyWallets.setText("You have no wallets");
        }
    }
}