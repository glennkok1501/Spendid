package sg.edu.np.spendid.Friends;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Friends.Utils.Capture;
import sg.edu.np.spendid.Models.Friend;
import sg.edu.np.spendid.R;

public class FriendScannerActivity extends AppCompatActivity {

    private EditText name_editText;
    private EditText key;
    private FloatingActionButton add_fab;
    private DBHandler dbHandler;
    private SwitchMaterial editKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_scanner);
        name_editText = findViewById(R.id.friendScanner_name_editText);
        editKey = findViewById(R.id.friend_key_switch);
        key = findViewById(R.id.friendScanner_key_editText);
        add_fab = findViewById(R.id.friendScanner_add_fab);
        TextInputLayout layout = findViewById(R.id.friendScanner_name_layout);
        dbHandler = new DBHandler(this, null, null, 1);

        initToolBar();

        //initialize scanner
        IntentIntegrator intentIntegrator = new IntentIntegrator(FriendScannerActivity.this);
        intentIntegrator.setPrompt("For flash use volume up key");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setCaptureActivity(Capture.class);
        intentIntegrator.initiateScan();

        editKey.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    enableKey();
                }
                else{
                    disableKey();
                }
            }
        });

        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = name_editText.getText().toString();
                String friendKey = key.getText().toString();
                if (checkName(name)){
                    if (friendKey.length() > 0){
                        dbHandler.addFriend(new Friend(name, currentDate(), friendKey));
                        Toast.makeText(FriendScannerActivity.this, "Added "+name, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(FriendScannerActivity.this, "No Key Added", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    layout.setError("Invalid Name");
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, data
        );
        //get receivers public key
        if (intentResult.getContents() != null){
            try{
                //lock edit text if key found
                disableKey();
                //split contents by name and public key
                String[] received = intentResult.getContents().split(";");

                //set values
                key.setText(received[1]);
                name_editText.setText(received[0]);
            }
            catch (Exception e){
                //error handling
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            editKey.setChecked(true);
            Toast.makeText(FriendScannerActivity.this, "No Result", Toast.LENGTH_SHORT).show();
        }
    }

    //get current date
    private String currentDate(){
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(currentTime.getTime());
    }

    private void disableKey(){
        editKey.setChecked(false);
        key.setEnabled(false);
    }

    private void enableKey(){
        editKey.setChecked(true);
        key.setEnabled(true);
    }

    private boolean checkName(String name){
        return !(name.length() < 1 || name.length() > 15);
    }

    private void initToolBar(){
        //Tool Bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView close = findViewById(R.id.activityImg_toolBar);
        close.setImageResource(R.drawable.ic_clear_32);
        activityTitle.setText("New Friend");
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}