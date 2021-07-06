package sg.edu.np.spendid;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//export or import records from csv files to selected wallets
public class ExportActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private Uri path;
    private Wallet wallet;
    private ArrayList<Wallet> walletArrayList;
    private final String delimiter = ",";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        initToolbar(); //set toolbar

        dbHandler = new DBHandler(this, null, null, 1);
        walletArrayList = dbHandler.getWallets();
        String[] walletList = getWalletList();

        //initiate spinner to select wallet to export or import with walletList
        Spinner spinner = findViewById(R.id.export_wallet_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, walletList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //get wallet object on first selected choice based on name
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
                    //export all records associated with specified wallet Id
                    exportRecords(dbHandler.getWalletRecords(wallet.getWalletId()));
                }
            }
        });

        //Open file picker
        ActivityResultLauncher<String>getFile = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        //import the file if result of file is chosen
                        if (result != null && wallet != null){
                            importCSV(result);
                        }
                    }
                });

        Button importButton = findViewById(R.id.export_importData);
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFile.launch("*/*"); //initiate filer picker with any file type
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

    //create a string array of wallet names for spinner to use
    private String[] getWalletList(){
        String[] walletList = new String[walletArrayList.size()];
        for (int i = 0; i < walletArrayList.size(); i++){
            walletList[i] = walletArrayList.get(i).getName();
        }
        return walletList;
    }

    //search through wallet array list to find wallet object based on name
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
            //resolved Uri to filepath
            InputStream inputStream = getContentResolver().openInputStream(uri);

            //read file
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            //initiate empty array list to store records retrieved
            ArrayList<Record> newRecords = new ArrayList<>();

            //read file line by line
            String line;
            while ((line = reader.readLine()) != null ){
                String[] data = line.split(delimiter); //separate data by delimiter
                //add record to arrayList
                newRecords.add(new Record(data[0], data[1],Double.parseDouble(data[2]), data[3], data[4],data[5],wallet.getWalletId()));
            }

            //insert records to database in bulk when completed
            dbHandler.addRangeRecord(newRecords);
            Toast.makeText(getApplicationContext(), "File successfully imported", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Import failed: file corrupted", Toast.LENGTH_SHORT).show();
        }
    }

    private void exportRecords (ArrayList<Record> records){
        StringBuilder data = new StringBuilder(); //initiate string builder to store data

        String filename = "spendid_"+new SimpleDateFormat("dd-MM-yyyy_HHmmss").format(Calendar.getInstance().getTime())+".csv";

        //iterate through records array list and add to string builder
        for (Record r : records){
            //remove characters that can cause issue to file or importing
            String title = r.getTitle().replaceAll("[\n,]", "");
            String des = r.getDescription().replaceAll("[\n,]","");
            data.append(title+delimiter+des+delimiter+r.getAmount()+delimiter+r.getCategory()+delimiter+r.getDateCreated()+delimiter+r.getTimeCreated()+"\n");
        }

        try {
            //write the file with string builder contents
            FileOutputStream out = openFileOutput(filename, Context.MODE_PRIVATE);
            File fileLocation = new File(getFilesDir(), filename);
            out.write(data.toString().getBytes());
            out.close();

            //initiate file provider to share file written
            String authority = this.getPackageName() + ".fileprovider";
            path = FileProvider.getUriForFile(this, authority, fileLocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            fileIntent.setType("*/*");
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
        activityTitle.setText("Export / Import");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}