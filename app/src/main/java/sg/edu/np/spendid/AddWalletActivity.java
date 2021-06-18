package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ListIterator;

public class AddWalletActivity extends AppCompatActivity {
    DBHandler dbhandler = new DBHandler(this, null, null, 1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet);

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

        EditText newWalletName = findViewById(R.id.addNewWalletName);
        EditText newWalletDescription = findViewById(R.id.addNewWalletDescription);

        EditText newWalletAmount = findViewById(R.id.addNewWalletAmount);



        Intent receivedData = getIntent();
        String currencyChosen = receivedData.getStringExtra("Currency");
        TextView chosenC = findViewById(R.id.chosenWalletCurrency);
        chosenC.setText(currencyChosen);

        Button CreateWallet = findViewById(R.id.CreateWalletButton);
        CreateWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Wallet w = new Wallet(newWalletName.getText().toString(), newWalletDescription.getText().toString(), currencyChosen, currentDate());
                dbhandler.addWallet(w);
                Intent intent = new Intent(AddWalletActivity.this, MainActivity.class);
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