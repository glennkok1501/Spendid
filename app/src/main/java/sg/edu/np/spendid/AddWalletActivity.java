package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddWalletActivity extends AppCompatActivity {
    private DBHandler dbhandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet);
        dbhandler = new DBHandler(this, null, null, 1);
        //Tool Bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        activityTitle.setText("Add Wallet");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        EditText newWalletName = findViewById(R.id.editWalletName);
        EditText newWalletDescription = findViewById(R.id.addNewWalletDescription);

        Intent receivedData = getIntent();
        String currencyChosen = receivedData.getStringExtra("Currency").toUpperCase();
        TextView chosenC = findViewById(R.id.chosenWalletCurrency);
        chosenC.setText(currencyChosen);

        FloatingActionButton CreateWallet = findViewById(R.id.CreateWalletButton);
        CreateWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Wallet w = new Wallet(newWalletName.getText().toString(), newWalletDescription.getText().toString(), currencyChosen, currentDate());
                dbhandler.addWallet(w);
                Toast.makeText(getApplicationContext(), "New Wallet Added", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddWalletActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
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
    private String currentDate(){
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(currentTime.getTime());
    }


}