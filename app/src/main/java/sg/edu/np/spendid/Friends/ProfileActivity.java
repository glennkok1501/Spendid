package sg.edu.np.spendid.Friends;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Utils.Security.Cryptography;

public class ProfileActivity extends AppCompatActivity {

    private ImageView qrcode;
    private Button newKey;
    private Bitmap bitmap;
    private TextView keyText;
    private EditText username;
    private final String PREF_NAME = "sharedPrefs";
    private final String PUBLIC_KEY = "publicKey";
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        qrcode = findViewById(R.id.profile_qrcode_imageView);
        newKey = findViewById(R.id.profile_newKey_button);
        username = findViewById(R.id.profile_username_editText);
        keyText = findViewById(R.id.profile_key_textView);
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        username.setText("Spendid User");
        getQrCode();

        keyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Create an ACTION_SEND Intent*/
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                /*The type of the content is text, obviously.*/
                intent.setType("text/plain");
                /*Applying information Subject and Body.*/
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Spendid Key");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, sendKeyText());
                startActivity(Intent.createChooser(intent, "Spendid Key"));
            }
        });

        newKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Cryptography(ProfileActivity.this).newKeyPair();
                getQrCode();
            }
        });
    }

    private void getQrCode(){
        String key = prefs.getString(PUBLIC_KEY, null);

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width
        int width = point.x;

        // generating dimension from width and height.
        int dimen = (width / 4)*3;

        QRGEncoder qrgEncoder = new QRGEncoder(username.getText().toString()+";"+key, null, QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder.encodeAsBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            qrcode.setImageBitmap(bitmap);
        } catch (Exception e) {
            // this method is called for
            // exception handling.
            e.printStackTrace();
        }
    }
    private String sendKeyText(){
        return String.format("Hi, %s here, this is my code:\n%s", username.getText().toString(), prefs.getString(PUBLIC_KEY, null));
    }
}