package sg.edu.np.spendid.Friends;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.content.SharedPreferences;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import sg.edu.np.spendid.Dialogs.MyAlertDialog;
import sg.edu.np.spendid.Friends.Utils.GenerateQRCode;
import sg.edu.np.spendid.Friends.Utils.ShareText;
import sg.edu.np.spendid.Friends.Utils.TransferKeyPair;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Utils.RequestPermission;
import sg.edu.np.spendid.Utils.Security.Cryptography;

public class ProfileActivity extends AppCompatActivity {

    private EditText username;
    private final String PUBLIC_KEY = "publicKey";
    private final String PRIVATE_KEY = "privateKey";
    private final String DEFAULT_NAME = "Spendid User";
    private final String USERNAME = "Username";
    private SharedPreferences prefs;
    private GenerateQRCode genQRCode;
    private ActivityResultLauncher<String> getFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ImageView qrcode = findViewById(R.id.profile_qrcode_imageView);
        username = findViewById(R.id.profile_username_editText);
        TextView keyText = findViewById(R.id.profile_key_textView);
        String PREF_NAME = "sharedPrefs";
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        Button saveName = findViewById(R.id.profile_username_button);
        TextInputLayout layout = findViewById(R.id.profile_username_layout);

        //initialize QR code
        genQRCode = new GenerateQRCode(this, qrcode);

        //initialize toolbar
        initToolbar();

        genQRCode.run(getQRCodeText());

        //get name from shared prefs
        getName();

        keyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareText(ProfileActivity.this).start("Spendid Key", sendKeyText());
            }
        });

        //edit name and save to shared prefs
        saveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();

                //check for valid name
                if (validName(name)){
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(USERNAME, name);
                    editor.apply();

                    //regenerate qr code with new name
                    genQRCode.run(getQRCodeText());
                    Toast.makeText(getApplicationContext(), "Saved name", Toast.LENGTH_SHORT).show();

                    //remove error on layout
                    layout.setError(null);
                }
                else{
                    layout.setError("Invalid Name");
                }

            }
        });

        //open file picker
        getFile = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        //import the file if result of file is chosen
                        if (result != null){
                            try{
                                //Import key pair
                                JSONObject keyPairObj = new TransferKeyPair(ProfileActivity.this).Import(result);
                                new Cryptography(ProfileActivity.this).importKeyPair(
                                                keyPairObj.getString(PUBLIC_KEY),
                                                keyPairObj.getString(PRIVATE_KEY));

                                //regenerate qr code
                                genQRCode.run(getQRCodeText());
                                Toast.makeText(getApplicationContext(), "Import Successful", Toast.LENGTH_SHORT).show();

                            }
                            catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Import failed: file corrupted", Toast.LENGTH_SHORT).show();
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

    private String sendKeyText(){
        return String.format("Hi, %s here, this is my code:\n%s", prefs.getString(USERNAME, DEFAULT_NAME), prefs.getString(PUBLIC_KEY, null));
    }

    private void getName(){
        String name = prefs.getString(USERNAME, null);
        if (name == null){
            username.setText(DEFAULT_NAME);
        }
        else{
            username.setText(name);
        }
    }

    private boolean validName(String name){
        return !(name.length() < 1 || name.length() > 15);
    }

    private void initToolbar(){
        TextView activityTitle = findViewById(R.id.toolbarTitle_textView);
        ImageView backArrow = findViewById(R.id.toolbarBtn_imageView1);
        ImageView more = findViewById(R.id.toolbarBtn_imageView2);
        backArrow.setImageResource(R.drawable.ic_back_arrow_32);
        activityTitle.setText("Profile");
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
                popupMenu.getMenu().add("Import Keys");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getTitle().toString()){
                            case "New Key":
                                //generate new key pair
                                newKeyPair();
                                break;
                            case "Export Keys":
                                //check for permission
                                if (new RequestPermission(ProfileActivity.this).checkPermission()) {
                                    Toast.makeText(getApplicationContext(), "Exporting Keys...", Toast.LENGTH_SHORT).show();

                                    try {
                                        //export public and private key to JSON file
                                        TransferKeyPair transfer = new TransferKeyPair(ProfileActivity.this);
                                        transfer.setPublicKey(prefs.getString(PUBLIC_KEY, ""));
                                        transfer.setPrivateKey(prefs.getString(PRIVATE_KEY, ""));
                                        transfer.Export();
                                    }
                                    //error handling
                                    catch (Exception e) {
                                        Toast.makeText(ProfileActivity.this, "Unable to Export", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            case "Import Keys":
                                //check for permission
                                if (new RequestPermission(ProfileActivity.this).checkPermission()){
                                    //warning for importing keys
                                    importDialog();
                                }
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

    private void importDialog(){
        MyAlertDialog dialog = new MyAlertDialog(this);
        dialog.setTitle("Import Keys");
        dialog.setBody("You would not be able to decrypt any files that was previous encrypted with your current key\nAre you sure you want to import?");
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
                //open file picker
                getFile.launch("application/json"); //only accept JSON file
                dialog.dismiss();

                //regenerate qr code
                genQRCode.run(getQRCodeText());
            }
        });
        dialog.show();
    }

    //format qrcode text with name and public key
    private String getQRCodeText(){
        String key = prefs.getString(PUBLIC_KEY, null);
        String name = prefs.getString(USERNAME, DEFAULT_NAME);
        return (name+";"+key);
    }
}