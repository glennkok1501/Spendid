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

public class EditWalletActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private Wallet wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wallet);

        //Tool bar
        TextView activityTitle = findViewById(R.id.mainToolbarTitle_textView);
        ImageView backArrow = findViewById(R.id.mainToolbarMenu_imageView);
        ImageView trash = findViewById(R.id.mainToolbarMore_imageView);
        backArrow.setImageResource(R.drawable.ic_back_arrow_32);
        trash.setImageResource(R.drawable.ic_delete_32);
        activityTitle.setText("Edit Wallet");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deleteDialog();
                CustomDialog customDialog = new CustomDialog(EditWalletActivity.this);
                customDialog.showDecisionDialog(
                        "Delete Wallet",
                        "Are you sure you want to permanently delete this wallet and all its transactions?",
                        EditWalletActivity.this::deleteWallet,
                        customDialog::blank);
            }
        });

        Intent intent = getIntent();
        int chosenWalletID = intent.getIntExtra("walletId",0);
        dbHandler = new DBHandler(this, null, null, 1);
        wallet = dbHandler.getWallet(chosenWalletID);

        EditText editWalletName = findViewById(R.id.editWalletName);
        editWalletName.setText(wallet.getName());

        EditText editWalletDescription = findViewById(R.id.editWalletDescription);
        editWalletDescription.setText(wallet.getDescription());

        FloatingActionButton editWallet = findViewById(R.id.editWalletButton);
        editWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Wallet w = new Wallet(chosenWalletID, editWalletName.getText().toString(), editWalletDescription.getText().toString(), wallet.getCurrency(), wallet.getDateCreated());
                dbHandler.updateWallet(w);
                Toast.makeText(getApplicationContext(), "Wallet Edited", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditWalletActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private void deleteWallet(){
        if (dbHandler.deleteWallet(wallet.getWalletId())){
            dbHandler.deleteWalletRecords(wallet.getWalletId());
            Toast.makeText(getApplicationContext(), "Wallet Deleted", Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(), "Unknown Wallet", Toast.LENGTH_SHORT).show();
        }
    }
}