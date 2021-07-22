package sg.edu.np.spendid.DataTransfer;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;

import sg.edu.np.spendid.DataTransfer.Utils.ImportCSV;
import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Models.Wallet;
import sg.edu.np.spendid.Utils.WalletNameList;

public class ImportExternalActivity extends AppCompatActivity {

    private Wallet wallet;
    private DBHandler dbHandler;
    private Uri uri;
    private ArrayList<Wallet> walletArrayList;
    private boolean encrypted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_external);
        initToolbar();
        Button cancelBtn = findViewById(R.id.import_cancel_btn);
        Button importBtn = findViewById(R.id.import_btn);

        uri = getImportIntent(); //get Uri of file
        dbHandler = new DBHandler(this, null, null, 1);
        walletArrayList = dbHandler.getWallets();

        //initiate spinner to select wallet to export or import with walletList
        Spinner spinner = findViewById(R.id.import_wallet_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                new WalletNameList(walletArrayList).getList());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //check if user has wallets
        if (walletArrayList.size() > 0){

            //get wallet object based on selected index
            wallet = walletArrayList.get(spinner.getSelectedItemPosition());

            //reassign selected wallet when spinner index change
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    wallet = walletArrayList.get(spinner.getSelectedItemPosition());
                }
                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // pass
                }
            });
        }
        else{
            wallet = null;
            Toast.makeText(getApplicationContext(), "No wallets available", Toast.LENGTH_SHORT).show();
        }

        //encryption is enabled
        SwitchMaterial encryptSwitch = findViewById(R.id.import_encrypt_switch);
        encryptSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                encrypted = isChecked;
            }
        });


        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uri != null && wallet != null){
                    try{
                        //insert records from csv files
                        new ImportCSV(ImportExternalActivity.this, uri, wallet, dbHandler).run(encrypted);
                        finish();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Import failed: file corrupted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //check if Uri is valid
    private Uri getImportIntent() {
        Uri uri = getIntent().getData();
        if (uri == null) {
            Toast.makeText(this, "File could not open", Toast.LENGTH_SHORT).show();
            return null;
        }
        return uri;
    }

    private void initToolbar(){
        //Tool Bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        activityTitle.setText("Import");
        backArrow.setVisibility(View.GONE);
    }



}