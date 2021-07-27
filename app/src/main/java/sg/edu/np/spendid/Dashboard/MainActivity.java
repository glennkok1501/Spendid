package sg.edu.np.spendid.Dashboard;

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
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import sg.edu.np.spendid.RecurringEntry.RecurringEntryActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import sg.edu.np.spendid.Dashboard.Adapters.WalletSlider.WalletSliderAdapter;
import sg.edu.np.spendid.Friends.FriendsListActivity;
import sg.edu.np.spendid.Friends.ProfileActivity;
import sg.edu.np.spendid.Misc.AboutActivity;
import sg.edu.np.spendid.Dashboard.Adapters.CurrentTrans.CurrentTransAdapter;
import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.ExchangeRates.ExchangeRateActivity;
import sg.edu.np.spendid.Records.AddRecordActivity;
import sg.edu.np.spendid.Statistics.StatisticsActivity;
import sg.edu.np.spendid.Utils.ViewPagerIndicators;
import sg.edu.np.spendid.Wallets.ManageWalletActivity;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Models.Record;
import sg.edu.np.spendid.RecordsHistory.SearchActivity;
import sg.edu.np.spendid.Misc.SettingsActivity;
import sg.edu.np.spendid.ShoppingList.ShoppingListMainActivity;
import sg.edu.np.spendid.RecordsHistory.TransactionHistoryActivity;
import sg.edu.np.spendid.Models.Wallet;
import sg.edu.np.spendid.Wallets.WalletCurrencyActivity;

public class MainActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private TextView monthText, balance, income, expense, noWallet, noCurTrans;
    private final DecimalFormat df2 = new DecimalFormat("#0.00");
    private FloatingActionButton fab, addWallet, addRecord;
    private Animation open, close, up, down;
    private boolean fabClicked, collapse_add;
    private DrawerLayout drawerLayout;
    private ViewPager2 walletsViewPager;
    private RecyclerView currentTransRV;
    private ArrayList<Wallet> walletList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize variable
        dbHandler = new DBHandler(this, null, null, 1);
        monthText = findViewById(R.id.totalBalMonth_textView);
        balance = findViewById(R.id.totalBalCost_textView);
        income = findViewById(R.id.totalBalIncCost_textView);
        expense = findViewById(R.id.totalBalExpCost_textView);
        TextView currency = findViewById(R.id.totalBalCur_textView);
        currency.setText(getString(R.string.baseCurrency));
        fab = findViewById(R.id.dashboard_fab);
        addRecord = findViewById(R.id.dashboardAddRecord_fab);
        addWallet = findViewById(R.id.dashboardAddWallet_fab);
        TextView manage = findViewById(R.id.manage_textView);
        TextView viewAll = findViewById(R.id.viewAll_textView);
        noWallet = findViewById(R.id.walletViewPageStatus_textView);
        noCurTrans = findViewById(R.id.curTransStatus_textView);
        walletsViewPager = findViewById(R.id.wallets_viewPager);
        currentTransRV = findViewById(R.id.main_transHist_RV);

        //floating action button (fab) animations
        open = AnimationUtils.loadAnimation(this, R.anim.rotate_open_animation);
        close = AnimationUtils.loadAnimation(this, R.anim.rotate_close_animation);
        up = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top_animation);
        down = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom_animation);
        hideHiddenFab(); //Init hidden buttons
        initToolbar(); //set toolbar
        initDrawer(); //Drawer and Navbar;

        //navigate to manage wallets
        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ManageWalletActivity.class));
            }
        });

        //navigate to transaction history
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TransactionHistoryActivity.class));
            }
        });

        //navigate to add transaction
        addRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check for wallets before starting activity
                if (walletList.size() > 0){
                    startActivity(new Intent(MainActivity.this, AddRecordActivity.class));
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please create a wallet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //navigate to add wallet
        addWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WalletCurrencyActivity.class));
            }
        });

        //hide or show hidden fab when clicked
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabClicked = !fabClicked;

                //show hidden buttons
                if (fabClicked){
                    addWallet.startAnimation(up);
                    addRecord.startAnimation(up);
                    fab.startAnimation(open);
                    showHiddenFab();
                }

                //Hide hidden buttons
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
        String[] splitDate = currentDate().split("-");

        //get a hashmap consisting of income, expense and balance based on date
        HashMap<String, Double> bal = dbHandler.getBalance(splitDate[0], splitDate[1]);
        monthText.setText(currentMonth()+" Balance");
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
        WalletSliderAdapter walletSliderAdapter = new WalletSliderAdapter(walletList, dbHandler);
        walletsViewPager.setAdapter(walletSliderAdapter);

        //view pager indicators
        LinearLayout indicators = findViewById(R.id.walletsIndicators_linearLayout);
        new ViewPagerIndicators(this, walletsViewPager, indicators).init(walletList.size());

        //get transaction for the day grouped by category
        HashMap<String, ArrayList<Record>> curTransMap = dbHandler.getGroupedTransaction(currentDate());

        //check if there are any transactions today and show message if empty
        if (curTransMap.size() == 0){
            noCurTrans.setVisibility(View.VISIBLE);
            noCurTrans.setText("No Transactions");
        }
        else{
            noCurTrans.setVisibility(View.GONE);
        }
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

    //hide navigation drawer
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

    /*
    check if for preferred first wallet in shared pref
    insert that wallet to the front if found
     */
    private ArrayList<Wallet> sortWallet(ArrayList<Wallet> walletList){
        String PREF_NAME = "sharedPrefs";
        //retrieve preferred first wallet from shared prefs
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int value = prefs.getInt("firstWallet",0);
        //insert preferred wallet to the first index
        for (int i = 0; i < walletList.size(); i++){
            Wallet tmpWallet = walletList.get(i);
            if (tmpWallet.getWalletId() == value){
                walletList.remove(i);
                walletList.add(0, tmpWallet);
                break;
            }
        }
        return walletList;
    }

    //get current date
    private String currentDate(){
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(currentTime.getTime());
    }

    //get current month
    private String currentMonth(){
        return new SimpleDateFormat("MMMM").format(Calendar.getInstance().getTime());
    }

    //Toolbar
    private void initToolbar(){
        TextView title = findViewById(R.id.toolbarTitle_textView);
        ImageView btn1 = findViewById(R.id.toolbarBtn_imageView1);
        ImageView btn2 = findViewById(R.id.toolbarBtn_imageView2);
        title.setText("Dashboard");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        initPopupMenu(btn2);
    }

    //navigation drawer
    private void initDrawer(){
        LinearLayout manageWallet, transHist, currencyRates,
                shoppingList, recurringEntry, settings, about, search, add,
                additional, addWallet, addRecord, stats, friendsList;
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

        recurringEntry = findViewById(R.id.navbar_recurring);
        setButtonForRecord(recurringEntry, RecurringEntryActivity.class);

        about = findViewById(R.id.navbar_about);
        setButton(about, AboutActivity.class);

        settings = findViewById(R.id.navbar_settings);
        setButton(settings, SettingsActivity.class);

        stats = findViewById(R.id.navbar_stats);
        setButton(stats, StatisticsActivity.class);

        friendsList = findViewById(R.id.navbar_friendsList);
        setButton(friendsList, FriendsListActivity.class);

        //additional options
        add = findViewById(R.id.navbar_add);
        additional = findViewById(R.id.navbar_additional);
        addWallet = findViewById(R.id.navbar_addWallet);
        setButton(addWallet, WalletCurrencyActivity.class);
        addRecord = findViewById(R.id.navbar_addRecord);
        setButtonForRecord(addRecord, AddRecordActivity.class);
        collapseBar(add, additional);

    }

    //attach intent re-direction to layout when clicked
    private void setButton(LinearLayout linearLayout, Class _class){
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, _class));
            }
        });
    }

    //check if wallet is available before starting activity
    private void setButtonForRecord(LinearLayout linearLayout, Class _class){
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (walletList.size() == 0){
                    Toast.makeText(getApplicationContext(), "Please create a wallet", Toast.LENGTH_SHORT).show();
                }
                else{
                    startActivity(new Intent(MainActivity.this, _class));
                }
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
                popupMenu.getMenu().add("My Code");
                popupMenu.getMenu().add("Settings");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getTitle().toString()){
                            case "My Code":
                                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                                break;
                            case "About":
                                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                                break;
                            case "Settings":
                                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
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