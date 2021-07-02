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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ExportActivity extends AppCompatActivity {
    private DBHandler dbHandler;
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
                export(dbHandler.getWalletRecords(1));
            }
        });

    }
    private void export (ArrayList<Record> records){
        Log.v("TAG", ""+records.size());

        StringBuilder data = new StringBuilder();
        String delimiter = ",";
        String filename = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss").format(Calendar.getInstance().getTime())+".csv";
        for (Record r : records){
            data.append(r.getTitle()+delimiter+r.getDescription()+delimiter+r.getAmount()+delimiter+r.getCategory()+delimiter+r.getDateCreated()+delimiter+r.getTimeCreated()+"\n");
        }

        try {
            FileOutputStream out = openFileOutput(filename, Context.MODE_PRIVATE);
            File fileLocation = new File(getFilesDir(), filename);
            out.write((data.toString().getBytes()));
            out.close();
            Toast.makeText(getApplicationContext(), "Saved to "+fileLocation, Toast.LENGTH_LONG).show();

            String authority = this.getPackageName() + ".fileprovider";
            path = FileProvider.getUriForFile(this, authority, fileLocation);

            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            fileIntent.setType("text/csv");
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

    @Override
    protected void onStop() {
        super.onStop();
        this.revokeUriPermission(path, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }
}