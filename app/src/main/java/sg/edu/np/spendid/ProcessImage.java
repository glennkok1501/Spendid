package sg.edu.np.spendid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProcessImage {

    private Context context;

    public ProcessImage(Context context) {
        this.context = context;
    }

    public byte[] render(Uri uri) throws Exception {
        InputStream iStream = context.getContentResolver().openInputStream(uri);
        int maxSize = 5248000; //5Mb
        byte[] imageBytes = getBytes(iStream);
        byte[] imageData;
        if (imageBytes.length < maxSize){
            imageData = compressImg(imageBytes);
        }
        else{
            Log.v("TAG", "File too Large");
            Toast.makeText(context, "File too large: exceeded 5Mb", Toast.LENGTH_SHORT).show();
            imageData = null;
        }
        return imageData;
    }

    private byte[] compressImg(byte[] image){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BitmapFactory.decodeByteArray(image, 0, image.length).compress(Bitmap.CompressFormat.JPEG, 20, out);
        return out.toByteArray();
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
