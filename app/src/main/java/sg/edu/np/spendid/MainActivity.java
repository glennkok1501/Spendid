//Created by Glenn

package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
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
    private TextView monthText, balance, income, expense, manage, viewAll;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private final static String PREF_NAME = "sharedPrefs";
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new DBHandler(this, null, null, 1);
        monthText = findViewById(R.id.totalBalMonth_textView);
        balance = findViewById(R.id.totalBalCost_textView);
        income = findViewById(R.id.totalBalIncCost_textView);
        expense = findViewById(R.id.totalBalExpCost_textView);
        fab = findViewById(R.id.dashboard_fab);
        manage = findViewById(R.id.manage_textView);
        viewAll = findViewById(R.id.viewAll_textView);

        //Seed Data
        if (dbHandler.getWallets().size() == 0){
            SeedData seedData = new SeedData(this);
            seedData.initDatabase();
        }

        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManageWalletActivity.class);
                startActivity(intent);
            }
        });
    }

    private ArrayList<Wallet> sortWallet(ArrayList<Wallet> w){
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int value = prefs.getInt("firstWallet",0);
        for (int i = 0; i < w.size(); i++){
            Wallet t = w.get(i);
            if (t.getWalletId() == value){
                w.remove(i);
                w.add(0, t);
                break;
            }
        }
        return w;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Total Balance
        HashMap<String, Double> bal = dbHandler.getBalance(currentMonth());
        monthText.setText("Total Balance - "+new SimpleDateFormat("MMMM").format(Calendar.getInstance().getTime()));
        balance.setText(df2.format(bal.get("balance")));
        income.setText(df2.format(bal.get("income")));
        expense.setText(df2.format(bal.get("expense")));

        //Wallets view pager
        walletList = sortWallet(dbHandler.getWallets());
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

    private String currentMonth(){
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        return dateFormat.format(currentTime.getTime());
    }
}