package sg.edu.np.spendid.Friends.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static android.content.Context.WINDOW_SERVICE;

public class GenerateQRCode {
    private Context context;
    private ImageView imageView;

    public GenerateQRCode(Context context, ImageView imageView) {
        this.context = context;
        this.imageView = imageView;
    }

    public void run(String text){

        //get window size
        WindowManager manager = (WindowManager) context.getSystemService(WINDOW_SERVICE);

        //initializing a variable for default display
        Display display = manager.getDefaultDisplay();

        //creating a variable for point which is to be displayed in QR Code
        Point point = new Point();
        display.getSize(point);

        //getting width
        int width = point.x;

        //generating dimension for 4/5.
        int dimen = (width / 5)*4;

        QRGEncoder qrgEncoder = new QRGEncoder(text, null, QRGContents.Type.TEXT, dimen);
        try {
            //getting qrcode in the form of bitmap
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            //set bitmap to imageview
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            //exception handling.
            e.printStackTrace();
        }
    }
}
