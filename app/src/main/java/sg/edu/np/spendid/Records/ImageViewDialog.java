package sg.edu.np.spendid.Records;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Utils.RequestPermission;

public class ImageViewDialog {
    private Context context;
    private byte[] imageBytes;

    public ImageViewDialog(Context context, byte[] imageBytes) {
        this.context = context;
        this.imageBytes = imageBytes;
    }

    public void show(){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.image_display_layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(true);

        ImageView close = dialog.findViewById(R.id.imageDisplayClose_imageView);
        ImageView image = dialog.findViewById(R.id.imageDisplay_imageView);
        FloatingActionButton download = dialog.findViewById(R.id.imageDisplayDl_fab);
        setImage(image);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new RequestPermission(context).checkPermission()){
                    try {
                        download();
                    } catch (Exception e) {
                        Toast.makeText(context, "Unable to download image", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void setImage(ImageView image){
        image.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
    }

    private void download() throws Exception {
        Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show();
        String filename = "spendid_"+new SimpleDateFormat("dd-MM-yyyy_HHmmss").format(Calendar.getInstance().getTime())+".jpg";
        File fileLocation = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), filename);
        FileOutputStream out = new FileOutputStream(fileLocation);
        out.write(imageBytes);
        out.close();
        Toast.makeText(context, "Saved to "+fileLocation, Toast.LENGTH_SHORT).show();
    }
}
