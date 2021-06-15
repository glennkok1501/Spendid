//Created by Glenn

package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Wallet> walletList;
    private DBHandler dbHandler;
    private TextView balance, income, expense;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new DBHandler(this, null, null, 1);
        balance = findViewById(R.id.totalBalCost_textView);
        income = findViewById(R.id.totalBalIncCost_textView);
        expense = findViewById(R.id.totalBalExpCost_textView);
        fab = findViewById(R.id.dashboard_fab);

        //Seed Data
        if (dbHandler.getWallets().size() == 0){
            SeedData seedData = new SeedData(this);
            seedData.initDatabase();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Total Balance
        HashMap<String, Double> bal = dbHandler.getBalance();
        balance.setText(df2.format(bal.get("balance")));
        income.setText(df2.format(bal.get("income")));
        expense.setText(df2.format(bal.get("expense")));

        //Wallets view pager
        walletList = dbHandler.getWallets();
        ViewPager2 viewPager = findViewById(R.id.wallets_viewPager);
        WalletSliderAdapter walletSliderAdapter = new WalletSliderAdapter(walletList);
        viewPager.setAdapter(walletSliderAdapter);

        //Current Transactions
        RecyclerView currentTransRV = findViewById(R.id.main_transHist_RV);
        CurrentTransAdapter myCurrentTransAdapter = new CurrentTransAdapter(dbHandler.getGroupedTransaction(currentDate()));
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        currentTransRV.setLayoutManager(myLayoutManager);
        currentTransRV.setItemAnimator(new DefaultItemAnimator());
        currentTransRV.setAdapter(myCurrentTransAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectWalletActivity.class);
                startActivity(intent);
            }
        });
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

    private String currentDate(){
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(currentTime.getTime());
    }
}