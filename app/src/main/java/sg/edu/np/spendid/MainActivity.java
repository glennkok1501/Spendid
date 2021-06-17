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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Wallet> walletList;
    private DBHandler dbHandler;
    private TextView monthText, balance, income, expense, currency, manage, viewAll;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private final static String PREF_NAME = "sharedPrefs";
    private FloatingActionButton fab, addWallet, addRecord;
    private String baseCurrency;
    private Animation open, close, up, down;
    private boolean fabClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new DBHandler(this, null, null, 1);
        monthText = findViewById(R.id.totalBalMonth_textView);
        balance = findViewById(R.id.totalBalCost_textView);
        income = findViewById(R.id.totalBalIncCost_textView);
        expense = findViewById(R.id.totalBalExpCost_textView);
        currency = findViewById(R.id.totalBalCur_textView);
        fab = findViewById(R.id.dashboard_fab);
        addRecord = findViewById(R.id.dashboardAddRecord_fab);
        addWallet = findViewById(R.id.dashboardAddWallet_fab);
        manage = findViewById(R.id.manage_textView);
        viewAll = findViewById(R.id.viewAll_textView);

        //fab animations
        open = AnimationUtils.loadAnimation(this, R.anim.rotate_open_animation);
        close = AnimationUtils.loadAnimation(this, R.anim.rotate_close_animation);
        up = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top_animation);
        down = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom_animation);


        //Seed Data
        if (dbHandler.getWallets().size() == 0){
            SeedData seedData = new SeedData(this);
            seedData.initDatabase();


        }
        //Seed currency
        SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putString("baseCurrency", "SGD");
        editor.apply();

        getBaseCurrency();
        hideHiddenFab();

        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManageWalletActivity.class);
                startActivity(intent);
            }
        });

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TransactionHistoryActivity.class);
                startActivity(intent);
            }
        });

        addRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectWalletActivity.class);
                startActivity(intent);
            }
        });

        addWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, SelectWalletActivity.class);
//                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Add wallet", Toast.LENGTH_SHORT).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabClicked = !fabClicked;
                if (fabClicked){
                    addWallet.startAnimation(up);
                    addRecord.startAnimation(up);
                    fab.startAnimation(open);
                    showHiddenFab();
                }
                else{
                    addWallet.startAnimation(down);
                    addRecord.startAnimation(down);
                    fab.startAnimation(close);
                    hideHiddenFab();
                }
            }
        });
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
        if (walletList.size() == 0){
            TextView noWallet = findViewById(R.id.walletViewPageStatus_textView);
            noWallet.setText("No Wallets");
        }
        ViewPager2 viewPager = findViewById(R.id.wallets_viewPager);
        WalletSliderAdapter walletSliderAdapter = new WalletSliderAdapter(walletList, baseCurrency, this);
        viewPager.setAdapter(walletSliderAdapter);

        //Current Transactions
        HashMap<String, ArrayList<Record>> curTransMap = dbHandler.getGroupedTransaction(currentDate());
        if (curTransMap.size() == 0){
            TextView noCurTrans = findViewById(R.id.curTransStatus_textView);
            noCurTrans.setText("No Transactions");
        }
        RecyclerView currentTransRV = findViewById(R.id.main_transHist_RV);
        CurrentTransAdapter myCurrentTransAdapter = new CurrentTransAdapter(curTransMap, baseCurrency);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        currentTransRV.setLayoutManager(myLayoutManager);
        currentTransRV.setItemAnimator(new DefaultItemAnimator());
        currentTransRV.setAdapter(myCurrentTransAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        resetFab();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showHiddenFab(){
        addWallet.setVisibility(View.VISIBLE);
        addWallet.setClickable(true);
        addRecord.setVisibility(View.VISIBLE);
        addRecord.setClickable(true);
    }

    private void hideHiddenFab(){
        addWallet.setVisibility(View.INVISIBLE);
        addWallet.setClickable(false);
        addRecord.setVisibility(View.INVISIBLE);
        addRecord.setClickable(false);
    }

    private void resetFab(){
        if (fabClicked){
            addWallet.startAnimation(down);
            addRecord.startAnimation(down);
            fab.startAnimation(close);
            hideHiddenFab();
            fabClicked = !fabClicked;
        }
    }

    private void getBaseCurrency(){
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        baseCurrency = prefs.getString("baseCurrency", "");
        currency.setText(baseCurrency);
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