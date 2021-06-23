package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {
    private final String PREF_NAME = "sharedPrefs";
    private boolean nightMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean nightMode = getSharedPreferences(PREF_NAME, MODE_PRIVATE).getBoolean("nightMode", false);
        toggleNightMode(nightMode);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //Tool Bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        activityTitle.setText("Settings");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        SwitchMaterial nightModeSw = findViewById(R.id.settings_nightMode_switch);
        nightModeSw.setChecked(nightMode);
        nightModeSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
                if (isChecked){
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("nightMode", true);
                }
                else{
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("nightMode", false);
                }
                editor.apply();
            }
        });
    }

    private void toggleNightMode(boolean on){
        if (on){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}