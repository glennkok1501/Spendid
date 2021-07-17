package sg.edu.np.spendid.Friends;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import sg.edu.np.spendid.Dialogs.MyAlertDialog;
import sg.edu.np.spendid.Friends.Utils.GenerateQRCode;
import sg.edu.np.spendid.Friends.Utils.ShareText;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.ShoppingList.AddCartToRecord;
import sg.edu.np.spendid.ShoppingList.ShoppingListActivity;
import sg.edu.np.spendid.Utils.Security.Cryptography;

public class ProfileActivity extends AppCompatActivity {

    private ImageView qrcode;
    private Button saveName;
    private TextView keyText;
    private EditText username;
    private final String PREF_NAME = "sharedPrefs";
    private final String PUBLIC_KEY = "publicKey";
    private final String DEFAULT_NAME = "Spendid User";
    private final String USERNAME = "Username";
    private SharedPreferences prefs;
    private GenerateQRCode genQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        qrcode = findViewById(R.id.profile_qrcode_imageView);
        username = findViewById(R.id.profile_username_editText);
        keyText = findViewById(R.id.profile_key_textView);
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        saveName = findViewById(R.id.profile_username_button);
        TextInputLayout layout = findViewById(R.id.profile_username_layout);
        genQRCode = new GenerateQRCode(this, qrcode);

        initToolbar();

        genQRCode.run(getQRCodeText());

        checkName();

        keyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareText(ProfileActivity.this).start("Spendid Key", sendKeyText());
            }
        });

        saveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                if (checkName(name)){
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(USERNAME, name);
                    editor.apply();
                    genQRCode.run(getQRCodeText());
                    Toast.makeText(getApplicationContext(), "Saved name", Toast.LENGTH_SHORT).show();
                    layout.setError(null);
                }
                else{
                    layout.setError("Invalid Name");
                }

            }
        });
    }

    private String sendKeyText(){
        return String.format("Hi, %s here, this is my code:\n%s", prefs.getString(USERNAME, DEFAULT_NAME), prefs.getString(PUBLIC_KEY, null));
    }

    private void checkName(){
        String name = prefs.getString(USERNAME, null);
        if (name == null){
            username.setText(DEFAULT_NAME);
        }
        else{
            username.setText(name);
        }
    }

    private boolean checkName(String name){
        return !(name.length() < 1 || name.length() > 15);
    }

    private void initToolbar(){
        TextView activityTitle = findViewById(R.id.mainToolbarTitle_textView);
        ImageView backArrow = findViewById(R.id.mainToolbarMenu_imageView);
        ImageView more = findViewById(R.id.mainToolbarMore_imageView);
        backArrow.setImageResource(R.drawable.ic_back_arrow_32);
        activityTitle.setText("Shopping Cart");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initPopupMenu(more);

    }

    private void initPopupMenu(ImageView more){
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ProfileActivity.this, more);
                popupMenu.getMenu().add("New Key");
                popupMenu.getMenu().add("Export Keys");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getTitle().toString()){
                            case "New Key":
                                newKeyPair();
                                break;
                            case "Export Keys":
                                Toast.makeText(getApplicationContext(), "Exporting Keys...", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), "Unknown Page", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    private void newKeyPair(){
        MyAlertDialog dialog = new MyAlertDialog(this);
        dialog.setTitle("Generate New Key");
        dialog.setBody("You would not be able to decrypt any files that was previous encrypted with your current key\nAre you sure you want to generate new keys?");
        dialog.setNegative().setText("Cancel");
        dialog.setNegative().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setPositive().setText("Confirm");
        dialog.setPositive().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Cryptography(ProfileActivity.this).newKeyPair();
                Toast.makeText(getApplicationContext(), "Generated New Keys", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                genQRCode.run(getQRCodeText());
            }
        });
        dialog.show();
    }

    private String getQRCodeText(){
        String key = prefs.getString(PUBLIC_KEY, null);
        String name = prefs.getString(USERNAME, DEFAULT_NAME);
        return (name+";"+key);
    }
}