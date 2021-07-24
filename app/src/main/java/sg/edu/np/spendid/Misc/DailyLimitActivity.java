package sg.edu.np.spendid.Misc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import org.w3c.dom.Text;

import java.util.ArrayList;

import sg.edu.np.spendid.R;

public class DailyLimitActivity extends AppCompatActivity {
    private SwitchMaterial dSwitch, dNotif;
    private EditText dAmtEdit;
    private Spinner dSpinner;
    private String[] range = new String[] {"10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%", "90%"};

    private final String PREF_NAME = "sharedPrefs";
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_limit);

        //setting views
        dSwitch = findViewById(R.id.limit_daily_switch);
        dAmtEdit = findViewById(R.id.limit_amt_editText);
        dSpinner = findViewById(R.id.limit_daily_spinner);
        dNotif = findViewById(R.id.limit_daily_notification_switch);

        initToolbar(); //set toolbar
        editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();

        //setting up spinner attributes
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, range);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dSpinner.setAdapter(adapter);

        //placing necessary views(all except main switch) into a list for easier enabling/disabling
        View[] dViews = new View[] {dAmtEdit, dSpinner, dNotif};

        //setting attributes to how they were when last saved
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        dSwitch.setChecked(prefs.getBoolean("Daily Limit", false));
        dAmtEdit.setText(String.valueOf(prefs.getInt("Limit Amount", 50)));
        dSpinner.setSelection(prefs.getInt("Notify At", 8));
        dNotif.setChecked(getSharedPreferences(PREF_NAME, MODE_PRIVATE).getBoolean("Notify", true));
        toggleLimit(prefs.getBoolean("Daily Limit", false), dViews);

        dSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleLimit(isChecked, dViews);
            }
        });
    }

    @Override
    protected void onResume() { super.onResume(); }

    @Override
    protected void onPause() { super.onPause(); }

    @Override
    protected void onStop() {
        super.onStop();
        //save limit amount
        if (dSwitch.isChecked()){
            String amountString = dAmtEdit.getText().toString();
            if (amountString.length() == 0){
                amountString = "0";
            }
            editor.putInt("Limit Amount", Integer.parseInt(amountString));

            //save notify me status
            editor.putBoolean("Notify", dNotif.isChecked());

            //save percentage position of spinner
            editor.putInt("Notify At", dSpinner.getSelectedItemPosition());
        }
        else{
            //save notify me status
            editor.putBoolean("Notify", false);
        }

        editor.apply();

    }

    @Override
    protected void onDestroy() { super.onDestroy(); }

    private void toggleLimit(boolean checked, View[] dViews) {
        editor.putBoolean("Daily Limit", checked);
        editor.apply();

        for (View v : dViews) {
            v.setEnabled(checked);
        }
    }

    private void initToolbar(){
        //Tool bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        activityTitle.setText("Set Limits");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });
    }
}