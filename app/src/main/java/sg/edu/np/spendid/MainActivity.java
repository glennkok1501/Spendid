//Created by Glenn

package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private LinearLayout manangeWallet, transHist, currencyRates, shoppingList, stats, settings, about;

    //For nav bar
    DrawerLayout drawerLayout;

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

        //Drawer and Navbar
        initDrawer();

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
                Intent intent = new Intent(MainActivity.this, WalletCurrencyActivity.class);
                startActivity(intent);
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

    private void closeDrawer(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
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

        LinearLayout indicators = findViewById(R.id.walletsIndicators_linearLayout);
        TextView[] dots = new TextView[walletList.size()];
        viewPagerIndicators(dots, indicators);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dots.length; i++){
                    if (i == position){
                        dots[i].setTextColor(getResources().getColor(R.color.fire_bush));
                    }
                    else{
                        dots[i].setTextColor(getResources().getColor(R.color.light_grey));
                    }
                }
                super.onPageSelected(position);

            }
        });

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
        closeDrawer();

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

    private void initDrawer(){
        //Tool bar
        drawerLayout = findViewById(R.id.dashboard_drawer_layout);
        ImageView menuBtn = findViewById(R.id.mainToolbarMenu_imageView);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //drawer
        manangeWallet = findViewById(R.id.navbar_manageWallets);
        setButton(manangeWallet, ManageWalletActivity.class);

        transHist = findViewById(R.id.navbar_transHist);
        setButton(transHist, TransactionHistoryActivity.class);
    }

    private void setButton(LinearLayout l, Class c){
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, c);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void viewPagerIndicators(TextView[] d, LinearLayout l){
        l.removeAllViews();
        for (int i = 0; i < d.length; i++){
            d[i] = new TextView(this);
            d[i].setText(Html.fromHtml("&#8226;"));
            d[i].setTextSize(18);
            l.addView(d[i]);
        }
    }
}