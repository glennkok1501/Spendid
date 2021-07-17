package sg.edu.np.spendid.Wallets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import sg.edu.np.spendid.Dashboard.MainActivity;
import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Models.Wallet;

//create wallets
public class AddWalletActivity extends AppCompatActivity {
    private DBHandler dbhandler;
    private EditText newWalletName, newWalletDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet);
        dbhandler = new DBHandler(this, null, null, 1);
        initToolbar(); //set toolbar

        newWalletName = findViewById(R.id.editWalletName);
        TextInputLayout walletNameLayout = findViewById(R.id.addNewWalletName_layout);
        newWalletDescription = findViewById(R.id.addNewWalletDescription);

        //retrieve currency from select country
        Intent receivedData = getIntent();
        String currencyChosen = receivedData.getStringExtra("Currency").toUpperCase();
        TextView chosenCurrency = findViewById(R.id.chosenWalletCurrency);
        chosenCurrency.setText(currencyChosen);

        FloatingActionButton CreateWallet = findViewById(R.id.CreateWalletButton);
        CreateWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create wallet if wallet is unique and name is valid
                if (isValidWalletName()){
                    Wallet w = new Wallet(newWalletName.getText().toString(), newWalletDescription.getText().toString(), currencyChosen, currentDate());
                    dbhandler.addWallet(w);
                    Toast.makeText(getApplicationContext(), "New Wallet Added", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddWalletActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    walletNameLayout.setError("Invalid Wallet Name");
                    Toast.makeText(getApplicationContext(), "Invalid Wallet Name", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //direct to select currency
        chosenCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddWalletActivity.this, WalletCurrencyActivity.class);
                startActivity(intent);
                finish();
            }
        });
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

    //get current date
    private String currentDate(){
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(currentTime.getTime());
    }

    //check if wallet name is valid
    private boolean isValidWalletName(){
        int len = newWalletName.getText().toString().length();
        return len != 0 && len <= 15;
    }

    private void initToolbar(){
        //Tool Bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        activityTitle.setText("Add Wallet");
        backArrow.setImageResource(R.drawable.ic_clear_32);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}