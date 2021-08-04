package sg.edu.np.spendid.Misc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;

import sg.edu.np.spendid.Dashboard.MainActivity;
import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Models.Recurring;
import sg.edu.np.spendid.Network.CurrencyAPI;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Utils.Security.Cryptography;
import sg.edu.np.spendid.RecurringEntry.UpdateEntryToWallet;


public class SplashScreenActivity extends AppCompatActivity {
    private final int SPLASH_SCREEN = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        ImageView image = findViewById(R.id.AppIconLogo);
        TextView txt = findViewById(R.id.AppIconName);
        DBHandler dbHandler = new DBHandler(this, null, null, 1);

        //animation
        Animation topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        Animation bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        image.setAnimation(topAnim);
        txt.setAnimation(bottomAnim);

        Thread background = new Thread(){
            public void run(){
                try {
                    sleep(SPLASH_SCREEN);
                    Intent intent =new Intent(getBaseContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        background.start();

        //check for night mode
        toggleNightMode();

        //Fetch Data from API
        new CurrencyAPI(this, dbHandler).getData("sgd");

        //initialise public and private keys
        initKeyPair();

        //update recurring entries
        try {
            new UpdateEntryToWallet(dbHandler).UpdateRecurring();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    //check for night mode settings
    private void toggleNightMode(){
        if (getSharedPreferences("sharedPrefs", MODE_PRIVATE).getBoolean("nightMode", false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    //generate new key pair if not found
    private void initKeyPair(){
        Cryptography ctg = new Cryptography(SplashScreenActivity.this);
        if (ctg.checkKeyPair()){
            ctg.newKeyPair();
        }
    }
}