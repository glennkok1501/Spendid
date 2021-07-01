package sg.edu.np.spendid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;


public class ExportActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private final int CREATE_CODE = 40;
    private int READ_REQUEST_CODE = 2;
    private static final int CREATE_FILE = 1;
    private Uri path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        dbHandler = new DBHandler(this, null, null, 1);
        Button exportButton = findViewById(R.id.export_exportData);
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                export();
            }
        });
        //StringBuilder data = new StringBuilder();
        //data.append("\n" + dbHandler.getWalletRecords(0).getId() + "," + dbHandler.getWalletRecords(0).getDescription()+ ","+ dbHandler.getWalletRecords(0).getAmount()+","+ dbHandler.getWalletRecords(0).getCategory()+ ","+ dbHandler.getWalletRecords(0).getDateCreated()+","+ dbHandler.getWalletRecords(0).getTimeCreated()+","+ dbHandler.getWalletRecords(0).getWalletId());
//        exportButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
//                //Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//                intent.putExtra(Intent.EXTRA_TITLE, "exportData.csv");
//                startActivityForResult(intent, 2);
//
//            }
//        });
    }
    private void export (){
        StringBuilder data = new StringBuilder();
        data.append("\n" + dbHandler.getWalletRecords(1).getId() + ";n%" + dbHandler.getWalletRecords(1).getDescription()+ ";n%"+ dbHandler.getWalletRecords(1).getAmount()+";n%"+ dbHandler.getWalletRecords(1).getCategory()+ ";n%"+ dbHandler.getWalletRecords(1).getDateCreated()+";n%"+ dbHandler.getWalletRecords(1).getTimeCreated()+";n%"+ dbHandler.getWalletRecords(1).getWalletId());
//        try{
//            FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
//            out.write((data.toString().getBytes()));
//            out.close();
//
//            Context context = getApplicationContext();
//
//            File filelocation = new File(getFilesDir(), "data.csv");
//            String authority = context.getPackageName() + ".fileprovider";
//            Uri path = FileProvider.getUriForFile(context, authority, filelocation);
//
//            //context.grantUriPermission(getPackageName(), path, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//            Intent fileIntent = new Intent(Intent.ACTION_SEND);
//            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            fileIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            fileIntent.setType("text/csv");
//            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
//            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
//            //startActivity(fileIntent);
//
//            startActivity(Intent.createChooser(fileIntent, "Send mail"));
//            //revoke permisions
//            context.revokeUriPermission(path, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
        try {
            FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
            out.write((data.toString().getBytes()));
            out.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Context context = getApplicationContext();

        File filelocation = new File(getFilesDir(), "data.csv");
        String authority = context.getPackageName() + ".fileprovider";
        path = FileProvider.getUriForFile(context, authority, filelocation);

        //context.grantUriPermission(getPackageName(), path, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent fileIntent = new Intent(Intent.ACTION_SEND);
        fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        fileIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        fileIntent.setType("text/csv");
        fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
        fileIntent.putExtra(Intent.EXTRA_STREAM, path);
        //startActivity(fileIntent);
        Intent chooser = Intent.createChooser(fileIntent, "Send mail");
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, path, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        startActivity(chooser);
        //revoke permisions
        //this.revokeUriPermission(path, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.revokeUriPermission(path, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }
    //    private void createFile(Uri pickerInitialUri) {
//        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("application/pdf");
//        intent.putExtra(Intent.EXTRA_TITLE, "invoice.pdf");
//
//        // Optionally, specify a URI for the directory that should be opened in
//        // the system file picker when your app creates the document.
//        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
//
//        startActivityForResult(intent, CREATE_FILE);
//    }

//    public void btnExportPress(View v){
//        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
//        intent.putExtra(Intent.EXTRA_TITLE, "exportData.csv");
//        startActivityForResult(intent, CREATE_CODE);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Uri fileURL = null;
//        if (resultCode == Activity.RESULT_OK){
//            if (resultCode == CREATE_CODE){
//                if (data != null){
//                    Toast.makeText(this, "Exported", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }
//    public void performFileSearch() {
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//        startActivityForResult(intent, READ_REQUEST_CODE);
//    }
}