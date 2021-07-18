//Created by Glenn

package sg.edu.np.spendid.Wallets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Models.Wallet;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Wallets.Adapters.WalletSelectAdapter;

//select wallet to add transaction to
public class SelectWalletActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private TextView emptyWallets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_wallet);
        dbHandler = new DBHandler(this, null, null, 1);
        emptyWallets = findViewById(R.id.selWallet_empty_textView);
        initToolbar(); //set toolbar
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView selWalletRV = findViewById(R.id.sel_wallet_RV);
        ArrayList<Wallet> walletArrayList = dbHandler.getWallets();
        checkEmpty(walletArrayList);
        WalletSelectAdapter walletSelectAdapter = new WalletSelectAdapter(walletArrayList, this);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        selWalletRV.setLayoutManager(myLayoutManager);
        selWalletRV.setItemAnimator(new DefaultItemAnimator());
        selWalletRV.setAdapter(walletSelectAdapter);
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
        activityTitle.setText("Select a Wallet");
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