package sg.edu.np.spendid;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ExportActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private Uri path;
    private Wallet wallet;
    private ArrayList<Wallet> walletArrayList;
    private String delimiter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        dbHandler = new DBHandler(this, null, null, 1);
        walletArrayList = dbHandler.getWallets();
        String[] walletList = getWalletList();
        delimiter = ";!:";

        Spinner spinner = findViewById(R.id.export_wallet_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, walletList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        wallet = findWallet(spinner.getSelectedItem().toString());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                wallet = findWallet(spinner.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // pass
            }
        });

        Button exportButton = findViewById(R.id.export_exportData);
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wallet != null){
                    exportRecords(dbHandler.getWalletRecords(wallet.getWalletId()));
                }
            }
        });

        ActivityResultLauncher<String>getFile = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null && wallet != null){
                            importCSV(result);
                        }
                    }
                });

        Button importButton = findViewById(R.id.export_importData);
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFile.launch("*/*");
            }
        });

    }
    @Override
    protected void onStop() {
        super.onStop();
        this.revokeUriPermission(path, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }

    private String[] getWalletList(){
        String[] walletList = new String[walletArrayList.size()];
        for (int i = 0; i < walletArrayList.size(); i++){
            walletList[i] = walletArrayList.get(i).getName();
        }
        return walletList;
    }

    private Wallet findWallet(String walletName){
        for (Wallet wallet : walletArrayList){
            if (wallet.getName().equals(walletName)){
                return wallet;
            }
        }
        return null;
    }

    private void importCSV(Uri uri){
        try{
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            Log.v("TAG", reader.toString());
            String line;
            while ((line = reader.readLine()) != null ){
                String[] data = line.split(delimiter);
                dbHandler.addRecord(new Record(data[0], data[1], Double.parseDouble(data[2]), data[3], data[4], data[5], wallet.getWalletId()));
            }
            Toast.makeText(getApplicationContext(), "File successfully imported", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Import failed: file corrupted", Toast.LENGTH_SHORT).show();
        }
    }

    private void exportRecords (ArrayList<Record> records){
        StringBuilder data = new StringBuilder();
        String filename = new SimpleDateFormat("spendid_dd-MM-yyyy_HH:mm:ss").format(Calendar.getInstance().getTime())+".csv";
        for (Record r : records){
            data.append(r.getTitle()+delimiter+r.getDescription()+delimiter+r.getAmount()+delimiter+r.getCategory()+delimiter+r.getDateCreated()+delimiter+r.getTimeCreated()+"\n");
        }

        try {
            FileOutputStream out = openFileOutput(filename, Context.MODE_PRIVATE);
            File fileLocation = new File(getFilesDir(), filename);
            out.write(data.toString().getBytes());
            out.close();
            Toast.makeText(getApplicationContext(), "File exported", Toast.LENGTH_SHORT).show();

            String authority = this.getPackageName() + ".fileprovider";
            path = FileProvider.getUriForFile(this, authority, fileLocation);

            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            fileIntent.setType("*/*");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, filename);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);

            Intent chooser = Intent.createChooser(fileIntent, null);
            List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                this.grantUriPermission(packageName, path, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            startActivity(chooser);
        }

        catch(Exception e){
            e.printStackTrace();
        }
    }
}