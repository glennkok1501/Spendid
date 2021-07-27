package sg.edu.np.spendid.Records;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Utils.Permissions.RequestWritePermission;

public class ViewImageActivity extends AppCompatActivity {

    private byte[] imageBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        initToolbar();

        //get record id
        Intent intent = getIntent();

        //get bytes of image
        DBHandler dbHandler = new DBHandler(this, null, null, 1);
        imageBytes = dbHandler.getRecordImage(intent.getIntExtra("recordId", 0));

        ImageView image = findViewById(R.id.imageDisplay_imageView);
        FloatingActionButton download = findViewById(R.id.imageDisplayDl_fab);
        setImage(image);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check permission to download
                if (new RequestWritePermission(ViewImageActivity.this).checkPermission()){
                    try {
                        download();
                    }
                    //error handling
                    catch (Exception e) {
                        Toast.makeText(ViewImageActivity.this, "Unable to download image", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
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

    private void setImage(ImageView image){
        image.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
    }

    //write file to downloads folder
    private void download() throws Exception {
        Toast.makeText(ViewImageActivity.this, "Downloading...", Toast.LENGTH_SHORT).show();
        String filename = "spendid_"+new SimpleDateFormat("dd-MM-yyyy_HHmmss").format(Calendar.getInstance().getTime())+".jpg";
        File fileLocation = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), filename);
        FileOutputStream out = new FileOutputStream(fileLocation);
        out.write(imageBytes);
        out.close();
        Toast.makeText(ViewImageActivity.this, "Saved to "+fileLocation, Toast.LENGTH_SHORT).show();
    }

    private void initToolbar(){
        //Tool bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        backArrow.setImageResource(R.drawable.ic_clear_32);
        activityTitle.setText("View Transaction");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}