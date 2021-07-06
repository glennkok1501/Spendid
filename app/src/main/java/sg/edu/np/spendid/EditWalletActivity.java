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

import java.util.ArrayList;

//edit and delete wallets
public class EditWalletActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private Wallet wallet;
    private EditText editWalletName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wallet);

        initToolbar(); //set toolbar

        //retrieve wallet Id
        Intent intent = getIntent();
        int chosenWalletID = intent.getIntExtra("walletId",0);
        dbHandler = new DBHandler(this, null, null, 1);
        wallet = dbHandler.getWallet(chosenWalletID);

        editWalletName = findViewById(R.id.editWalletName);
        editWalletName.setText(wallet.getName());

        EditText editWalletDescription = findViewById(R.id.editWalletDescription);
        editWalletDescription.setText(wallet.getDescription());
        TextInputLayout walletNameLayout = findViewById(R.id.editNewWalletName_layout);
        FloatingActionButton editWallet = findViewById(R.id.editWalletButton);
        editWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update wallet if wallet is unique and name is valid
                if (isValidWalletName() && isValidWallet(editWalletName.getText().toString().toLowerCase())){
                    Wallet w = new Wallet(chosenWalletID, editWalletName.getText().toString(), editWalletDescription.getText().toString(), wallet.getCurrency(), wallet.getDateCreated());
                    dbHandler.updateWallet(w);
                    Toast.makeText(getApplicationContext(), "Wallet Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditWalletActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    walletNameLayout.setError("Improper Wallet Name");
                    Toast.makeText(getApplicationContext(), "Invalid Wallet Name", Toast.LENGTH_SHORT).show();
                }

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

    //check if wallet name is valid
    private boolean isValidWalletName(){
        int len = editWalletName.getText().toString().length();
        return len != 0 && len <= 15;
    }

    //prompt alert dialog for deletion of wallet
    private void deleteDialog(){
        AlertDialog alert = new AlertDialog(this);
        alert.setTitle("Delete Wallet");
        alert.setBody("Are you sure you want to permanently delete this wallet and all its transactions?");
        alert.setPositive().setText("Delete");
        alert.setPositive().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteWallet();
                alert.dismiss();
            }
        });
        alert.setNegative().setText("Cancel");
        alert.setNegative().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    //check if wallet name is unique by comparing with all wallets
    private boolean isValidWallet(String walletName){
        boolean valid = true;
        ArrayList<Wallet> walletList = dbHandler.getWallets();
        for (Wallet w : walletList){
            if (w.getName().toLowerCase().equals(walletName)){
                valid = false;
                break;
            }
        }
        return valid;
    }

    private void initToolbar(){
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
                deleteDialog();
            }
        });
    }

}