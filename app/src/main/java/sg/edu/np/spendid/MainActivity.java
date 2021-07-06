//Created by Glenn

package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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
    private TextView monthText, balance, income, expense, currency, manage, viewAll, noWallet, noCurTrans;
    private final DecimalFormat df2 = new DecimalFormat("#0.00");
    private final String PREF_NAME = "sharedPrefs";
    private FloatingActionButton fab, addWallet, addRecord;
    private Animation open, close, up, down;
    private boolean fabClicked;
    private DrawerLayout drawerLayout;
    private boolean collapse_add;

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
        currency.setText("SGD");
        fab = findViewById(R.id.dashboard_fab);
        addRecord = findViewById(R.id.dashboardAddRecord_fab);
        addWallet = findViewById(R.id.dashboardAddWallet_fab);
        manage = findViewById(R.id.manage_textView);
        viewAll = findViewById(R.id.viewAll_textView);
        noWallet = findViewById(R.id.walletViewPageStatus_textView);
        noCurTrans = findViewById(R.id.curTransStatus_textView);

        //floating action button (fab) animations
        open = AnimationUtils.loadAnimation(this, R.anim.rotate_open_animation);
        close = AnimationUtils.loadAnimation(this, R.anim.rotate_close_animation);
        up = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top_animation);
        down = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom_animation);
        hideHiddenFab(); //Init hidden buttons
        initToolbar(); //set toolbar
        initDrawer(); //Drawer and Navbar;

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

        //hide or show hidden fab when clicked
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
        //get total balance for the month with income and expenses
        HashMap<String, Double> bal = dbHandler.getBalance(currentMonth());
        monthText.setText(new SimpleDateFormat("MMMM").format(Calendar.getInstance().getTime())+" Balance");
        balance.setText(df2.format(bal.get("balance")));
        income.setText(df2.format(bal.get("income")));
        expense.setText(df2.format(bal.get("expense")));


        //retrieve wallet array list and sort based on favourite
        walletList = sortWallet(dbHandler.getWallets());

        //check if wallet data is empty and show message
        if (walletList.size() == 0){
            noWallet.setVisibility(View.VISIBLE);
            noWallet.setText("No Wallets");
        }
        else{
            noWallet.setVisibility(View.GONE);
        }

        //Wallets view pager
        ViewPager2 viewPager = findViewById(R.id.wallets_viewPager);
        WalletSliderAdapter walletSliderAdapter = new WalletSliderAdapter(walletList, this);
        viewPager.setAdapter(walletSliderAdapter);

        //view pager indicators
        LinearLayout indicators = findViewById(R.id.walletsIndicators_linearLayout);
        TextView[] dots = new TextView[walletList.size()];
        viewPagerIndicators(dots, indicators);
        //change color depending on which page
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

        //check if there are any transactions today and show message if empty
        if (curTransMap.size() == 0){
            noCurTrans.setVisibility(View.VISIBLE);
            noCurTrans.setText("No Transactions");
        }
        else{
            noCurTrans.setVisibility(View.GONE);
        }
        RecyclerView currentTransRV = findViewById(R.id.main_transHist_RV);
        CurrentTransAdapter myCurrentTransAdapter = new CurrentTransAdapter(curTransMap);
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

    private void closeDrawer(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
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
        //hide fab if hidden fab shown (used for onPause)
        if (fabClicked){
            addWallet.startAnimation(down);
            addRecord.startAnimation(down);
            fab.startAnimation(close);
            hideHiddenFab();
            fabClicked = !fabClicked;
        }
    }

    private ArrayList<Wallet> sortWallet(ArrayList<Wallet> walletList){
        //retrieve preferred first wallet from shared prefs
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int value = prefs.getInt("firstWallet",0);
        //insert preferred wallet to the first index
        for (int i = 0; i < walletList.size(); i++){
            Wallet t = walletList.get(i);
            if (t.getWalletId() == value){
                walletList.remove(i);
                walletList.add(0, t);
                break;
            }
        }
        return walletList;
    }

    //create view pager indicators
    private void viewPagerIndicators(TextView[] d, LinearLayout l){
        l.removeAllViews();
        //add new TextView with bullet points into linear layout depending on viewpager size
        for (int i = 0; i < d.length; i++){
            d[i] = new TextView(this);
            d[i].setText(Html.fromHtml(getResources().getString(R.string.dot)));
            d[i].setTextSize(18);
            l.addView(d[i]);
        }
    }

    //get current date
    private String currentDate(){
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(currentTime.getTime());
    }

    //get current month
    private String currentMonth(){
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        return dateFormat.format(currentTime.getTime());
    }

    //Toolbar
    private void initToolbar(){
        ImageView menuBtn = findViewById(R.id.mainToolbarMenu_imageView);
        ImageView moreBtn = findViewById(R.id.mainToolbarMore_imageView);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        initPopupMenu(moreBtn);
    }

    //navigation drawer
    private void initDrawer(){
        LinearLayout manageWallet, transHist, currencyRates,
                shoppingList, settings, about, search, recurring, add,
                additional, addWallet, addRecord;
        drawerLayout = findViewById(R.id.dashboard_drawer_layout);

        //set re-directions to activities
        manageWallet = findViewById(R.id.navbar_manageWallets);
        setButton(manageWallet, ManageWalletActivity.class);

        transHist = findViewById(R.id.navbar_transHist);
        setButton(transHist, TransactionHistoryActivity.class);

        search = findViewById(R.id.navbar_search);
        setButton(search, SearchActivity.class);

        currencyRates = findViewById(R.id.navbar_currencyRate);
        setButton(currencyRates, ExchangeRateActivity.class);

        shoppingList = findViewById(R.id.navbar_shoppingList);
        setButton(shoppingList, ShoppingListMainActivity.class);

        about = findViewById(R.id.navbar_about);
        setButton(about, AboutActivity.class);

        settings = findViewById(R.id.navbar_settings);
        setButton(settings, SettingsActivity.class);

        //additional options
        add = findViewById(R.id.navbar_add);
        additional = findViewById(R.id.navbar_additional);
        addWallet = findViewById(R.id.navbar_addWallet);
        setButton(addWallet, WalletCurrencyActivity.class);
        addRecord = findViewById(R.id.navbar_addRecord);
        setButton(addRecord, SelectWalletActivity.class);
        collapseBar(add, additional);

    }

    //attach intent re-direction to layout when clicked
    private void setButton(LinearLayout linearLayout, Class _class){
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, _class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    //collapse addition row in navigation bar
    private void collapseBar(LinearLayout add, LinearLayout additional){
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapse_add = !collapse_add;
                //change height to collapse
                if (collapse_add){
                    additional.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                }
                else{
                    additional.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                }
            }
        });
    }

    private void initPopupMenu(ImageView moreBtn){
        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, moreBtn);
                popupMenu.getMenu().add("About");
                popupMenu.getMenu().add("Settings");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent;
                        switch (item.getTitle().toString()){
                            case "About":
                                intent = new Intent(MainActivity.this, AboutActivity.class);
                                startActivity(intent);
                                break;
                            case "Settings":
                                intent = new Intent(MainActivity.this, SettingsActivity.class);
                                startActivity(intent);
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), "Unknown Page", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

}