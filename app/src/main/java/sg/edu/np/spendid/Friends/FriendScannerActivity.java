package sg.edu.np.spendid.Friends;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Models.Friend;
import sg.edu.np.spendid.R;

public class FriendScannerActivity extends AppCompatActivity {

    private EditText name_editText;
    private TextView key;
    private FloatingActionButton add_fab;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_scanner);
        name_editText = findViewById(R.id.friendScanner_name_editText);
        key = findViewById(R.id.friendScanner_key_textView);
        add_fab = findViewById(R.id.friendScanner_add_fab);
        dbHandler = new DBHandler(this, null, null, 1);

        //initialize scanner
        IntentIntegrator intentIntegrator = new IntentIntegrator(FriendScannerActivity.this);
        intentIntegrator.setPrompt("For flash use volume up key");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setCaptureActivity(Capture.class);
        intentIntegrator.initiateScan();

        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = name_editText.getText().toString();
                if (name.length() > 0 && key != null){
                    dbHandler.addFriend(new Friend(name, currentDate(), key.getText().toString()));
                    Toast.makeText(FriendScannerActivity.this, "Added "+name, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, data
        );
        //get receivers public key
        if (intentResult.getContents() != null){
            String[] received = intentResult.getContents().split(";");
            key.setText(received[1]);
            scanDialog(received[0]);
            name_editText.setText(received[0]);
        }
        else{
            key = null;
            Toast.makeText(FriendScannerActivity.this, "No Result", Toast.LENGTH_SHORT).show();
        }
    }

    private void scanDialog(String body){
        AlertDialog.Builder builder = new AlertDialog.Builder(FriendScannerActivity.this);
        builder.setTitle("Result");
        builder.setMessage(body);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private String currentDate(){
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(currentTime.getTime());
    }
}