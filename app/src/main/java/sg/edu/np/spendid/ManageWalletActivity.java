package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

import maes.tech.intentanim.CustomIntent;

public class ManageWalletActivity extends AppCompatActivity {
    private final static String PREF_NAME = "sharedPrefs";
    private DecimalFormat df2 = new DecimalFormat("#0.00");
    private DBHandler dbHandler;
    private String baseCurrency;
    private TextView bal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_wallet);
        dbHandler = new DBHandler(this, null, null, 1);
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        baseCurrency = prefs.getString("baseCurrency", "");
        bal = findViewById(R.id.totalWalletBal_textView);
        TextView header = findViewById(R.id.totalWalletTitle_textView);
        header.setText("Total Balance");

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
        RecyclerView recyclerView = findViewById(R.id.manageWallet_RV);
        WalletManageAdapter myAdapter = new WalletManageAdapter(dbHandler.getWallets(), baseCurrency, this);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(myLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);
    }
}