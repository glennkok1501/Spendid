package sg.edu.np.spendid.DataTransfer;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sg.edu.np.spendid.DataTransfer.Utils.ExportRecordHelper;
import sg.edu.np.spendid.DataTransfer.Utils.SelectFriendDialog;
import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Dialogs.MyAlertDialog;
import sg.edu.np.spendid.Models.Record;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Utils.Permissions.RequestWritePermission;
import sg.edu.np.spendid.Utils.Security.Cryptography;
import sg.edu.np.spendid.Models.Wallet;
import sg.edu.np.spendid.Utils.WalletNameList;

//export or import records from csv files to selected wallets
public class ExportActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private Uri path;
    private Wallet wallet;
    private ArrayList<Wallet> walletArrayList;
    private boolean encryptFile;
    private SelectFriendDialog sendToDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        initToolbar(); //set toolbar

        dbHandler = new DBHandler(this, null, null, 1);
        walletArrayList = dbHandler.getWallets();
        sendToDialog = new SelectFriendDialog(this, dbHandler.getFriends());

        //initiate spinner to select wallet to export or import with walletList
        Spinner spinner = findViewById(R.id.export_wallet_spinner);
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

        //enable encryption
        SwitchMaterial encryptSwitch = findViewById(R.id.export_encrypt_switch);
        encryptSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                encryptFile = isChecked;
                if (isChecked){
                    //choose public key to encrypt
                    sendToDialog.show();
                }
            }
        });

        Button exportButton = findViewById(R.id.export_exportData);
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wallet != null){
                    //export all records associated with specified wallet Id
                    if (new RequestWritePermission(ExportActivity.this).checkPermission()){
                        exportRecords(dbHandler.getWalletRecords(wallet.getWalletId()));
                    }
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
        clearFiles();
        //remove permissions when not in used
        this.revokeUriPermission(path, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void exportRecords (ArrayList<Record> records){
        String filename = "spendid_"+new SimpleDateFormat("dd-MM-yyyy_HHmmss").format(Calendar.getInstance().getTime())+".csv";
        try {
            //convert records array to csv format
            StringBuilder builderData = new ExportRecordHelper(records).ToCSV();

            //encrypt data if encryption is needed
            String data = (encryptFile && sendToDialog.getSelected() != null) ?
                    new Cryptography(ExportActivity.this).Encrypt(builderData.toString(), sendToDialog.getSelected().getPublicKey())
                    : builderData.toString();

            //write the file with string contents
            FileOutputStream out = openFileOutput(filename, Context.MODE_PRIVATE);
            File fileLocation = new File(getFilesDir(), filename);
            out.write(data.getBytes());
            out.close();

            //initiate file provider to share file written
            String authority = this.getPackageName() + ".fileprovider";
            path = FileProvider.getUriForFile(this, authority, fileLocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            fileIntent.setType("text/comma-separated-values"); //only allow CSV files
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, filename);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            Intent chooser = Intent.createChooser(fileIntent, "Spendid_backup");

            //grant read and write permission to avoid any permission denial
            List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                this.grantUriPermission(packageName, path, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            startActivity(chooser);
        }

        catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error occurred: unable to share file", Toast.LENGTH_LONG).show();
        }
    }

    //remove out files from files directory
    private void clearFiles(){
        File folder = new File(getFilesDir(), "");
        if (folder.isDirectory()){
            String[] files = folder.list();
            for (String file : files) {
                new File(folder, file).delete();
            }
        }
    }

    private void initToolbar(){
        //Tool Bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        activityTitle.setText("Export");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}