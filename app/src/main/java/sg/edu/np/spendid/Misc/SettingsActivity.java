package sg.edu.np.spendid.Misc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;

import sg.edu.np.spendid.DataTransfer.ExportActivity;
import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Dialogs.MyAlertDialog;
import sg.edu.np.spendid.Models.Wallet;
import sg.edu.np.spendid.R;

public class SettingsActivity extends AppCompatActivity {
    private final String PREF_NAME = "sharedPrefs";
    private DBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        dbHandler = new DBHandler(this, null, null, 1);
        initToolbar(); //set toolbar

        SwitchMaterial nightModeSw = findViewById(R.id.settings_nightMode_switch);
        nightModeSw.setChecked(getSharedPreferences(PREF_NAME, MODE_PRIVATE).getBoolean("nightMode", false));
        nightModeSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleNightMode(isChecked);
            }
        });

        TextView exportTextView = findViewById(R.id.settings_export_textView);
        exportTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ExportActivity.class);
                startActivity(intent);
            }
        });

        TextView clearAllTextView = findViewById(R.id.settings_clearAll_textView);
        clearAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllDialog();
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

    private void toggleNightMode(boolean isChecked){
        SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        if (isChecked){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor.putBoolean("nightMode", true);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor.putBoolean("nightMode", false);
        }
        editor.apply();
    }

    private void initToolbar(){
        //Tool Bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        activityTitle.setText("Settings");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void clearAllDialog(){
        MyAlertDialog alert = new MyAlertDialog(this);
        alert.setTitle("Clear All Data");
        alert.setBody("Are you sure you want to permanently delete everything?");
        alert.setPositive().setText("Clear");
        alert.setPositive().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllData();
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

    private void clearAllData(){
        ArrayList<Wallet> walletArrayList = dbHandler.getWallets();
        try{
            for (Wallet wallet : walletArrayList){
                int walletId = wallet.getWalletId();
                dbHandler.deleteWalletRecords(walletId);
                dbHandler.deleteWallet(walletId);
            }
            Toast.makeText(getApplicationContext(), "Cleared", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Some data may not have been deleted properly", Toast.LENGTH_SHORT).show();
        }

    }

}