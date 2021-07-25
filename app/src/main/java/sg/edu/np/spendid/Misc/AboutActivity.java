package sg.edu.np.spendid.Misc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import sg.edu.np.spendid.Dialogs.MyAlertDialog;
import sg.edu.np.spendid.R;

//About page that contains information about the app, attributes and some disclaimers.

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initToolBar(); //set toolbars

        CardView disclaimer = findViewById(R.id.about_disclaimer_cardView);
        disclaimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectURL("Github Repository", getString(R.string.github_api_link));
            }
        });

        CardView credits = findViewById(R.id.about_credits_cardView);
        credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectURL("Flaticon Website", getString(R.string.icon_link));
            }
        });

        CardView openSourceLib = findViewById(R.id.about_openSourceLib_cardView);
        openSourceLib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutActivity.this, AttributionActivity.class));
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

    private void redirectURL(String website, String url){
        MyAlertDialog dialog = new MyAlertDialog(AboutActivity.this);
        dialog.setTitle("Visit Website");
        dialog.setBody("Do you want to be redirected to "+website+"?");
        dialog.setPositive().setText("Proceed");
        dialog.setPositive().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Uri uri = Uri.parse(url);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });
        dialog.setNegative().setText("Cancel");
        dialog.setNegative().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void initToolBar(){
        //Tool Bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        activityTitle.setText("About");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}