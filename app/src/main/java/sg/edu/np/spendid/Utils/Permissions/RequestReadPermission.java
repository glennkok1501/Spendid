package sg.edu.np.spendid.Utils.Permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import sg.edu.np.spendid.Dialogs.MyAlertDialog;

/*
request for read permission for file and media access
to be called at run time
 */

public class RequestReadPermission {
    private Activity activity;
    private Context context;
    private final int STORAGE_PERMISSION_CODE = 1;

    public RequestReadPermission(Context context) {
        this.context = context;
        activity = (Activity) context;
    }

    //check if app has permission to read files
    public boolean checkPermission(){
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else{
            requestStoragePermission();
            return false;
        }
     }

     private void requestStoragePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)){
            permissionDialog();
        }
        else{
            ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE );
        }
     }

     private void permissionDialog(){
         MyAlertDialog dialog = new MyAlertDialog(context);
         dialog.setTitle("Permission Needed");
         dialog.setBody("Permission to access files and media is needed to use this feature");
         dialog.setPositive().setText("Proceed");
         dialog.setPositive().setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 dialog.dismiss();
                 ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE );
             }
         });
         dialog.setNegative().setText("Dismiss");
         dialog.setNegative().setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 dialog.dismiss();
             }
         });
         dialog.show();
     }
}
