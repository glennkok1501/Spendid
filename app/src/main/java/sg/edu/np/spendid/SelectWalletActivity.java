//Created by Glenn

package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import maes.tech.intentanim.CustomIntent;

public class SelectWalletActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private final static String PREF_NAME = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_wallet);
        dbHandler = new DBHandler(this, null, null, 1);

        //Tool bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        activityTitle.setText("Select a Wallet");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String baseCurrency = prefs.getString("baseCurrency", "");

        RecyclerView selWalletRV = findViewById(R.id.sel_wallet_RV);
        WalletSelectAdapter WalletSelectAdapter = new WalletSelectAdapter(dbHandler.getWallets(), baseCurrency, this);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        selWalletRV.setLayoutManager(myLayoutManager);
        selWalletRV.setItemAnimator(new DefaultItemAnimator());
        selWalletRV.setAdapter(WalletSelectAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
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