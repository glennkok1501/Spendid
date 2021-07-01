package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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

public class AddWalletActivity extends AppCompatActivity {
    private DBHandler dbhandler;
    private EditText newWalletName, newWalletDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet);
        dbhandler = new DBHandler(this, null, null, 1);
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

        newWalletName = findViewById(R.id.editWalletName);
        TextInputLayout walletNameLayout = findViewById(R.id.addNewWalletName_layout);
        newWalletDescription = findViewById(R.id.addNewWalletDescription);

        Intent receivedData = getIntent();
        String currencyChosen = receivedData.getStringExtra("Currency").toUpperCase();
        TextView chosenC = findViewById(R.id.chosenWalletCurrency);
        chosenC.setText(currencyChosen);

        FloatingActionButton CreateWallet = findViewById(R.id.CreateWalletButton);
        CreateWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidWalletName() && isValidWallet(newWalletName.getText().toString().toLowerCase())){
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

        chosenC.setOnClickListener(new View.OnClickListener() {
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
    private String currentDate(){
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(currentTime.getTime());
    }

    private boolean isValidWalletName(){
        int len = newWalletName.getText().toString().length();
        return len != 0 && len <= 15;
    }

    private boolean isValidWallet(String walletName){
        boolean valid = true;
        ArrayList<Wallet> walletList = dbhandler.getWallets();
        for (Wallet w : walletList){
            if (w.getName().toLowerCase().equals(walletName)){
                valid = false;
                break;
            }
        }
        return valid;
    }

}