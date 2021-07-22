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

import sg.edu.np.spendid.R;


public class DailyLimitActivity extends AppCompatActivity {
    private SwitchMaterial dSwitch, dNotif;
    private TextView dLimitText;
    private EditText dAmtEdit;
    private Spinner dSpinner;
    private LinearLayout dailyLimit;
    private int dLimit;
    private String[] range = new String[] {"10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%", "90%"};

    private final String PREF_NAME = "sharedPrefs";
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_limit);

        //setting views
        dSwitch = findViewById(R.id.limit_daily_switch);
        dLimitText = findViewById(R.id.limit_daily_amt_textView);
        dAmtEdit = findViewById(R.id.limit_amt_editText);
        dSpinner = findViewById(R.id.limit_daily_spinner);
        dNotif = findViewById(R.id.limit_daily_notification_switch);
        dailyLimit = findViewById(R.id.limit_daily_linearLayout);

        initToolbar(); //set toolbar
        dLimit = getSharedPreferences(PREF_NAME, MODE_PRIVATE).getInt("dailyLimit", 8);
        dSwitch.setChecked((getSharedPreferences(PREF_NAME, MODE_PRIVATE).getBoolean("Daily Limit", false)));
        dailyLimit.setEnabled(getSharedPreferences(PREF_NAME, MODE_PRIVATE).getBoolean("Daily Limit", false));
        editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, range);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dSpinner.setAdapter(adapter);
        dSpinner.setSelection(dLimit);

        View[] dViews = new View[]{dLimitText, dAmtEdit, dSpinner, dNotif};

        toggleLimit(getSharedPreferences(PREF_NAME, MODE_PRIVATE).getBoolean("Daily Limit", false),
                "Daily Limit", dViews);

        dSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleLimit(isChecked, "Daily Limit", dViews);
            }
        });

        dAmtEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                editor.putString("Limit Amount", dAmtEdit.getText().toString());
                editor.apply();
            }
        });

        dNotif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("Notify", true);
                } else {
                    editor.putBoolean("Notify", false);
                }
            }
        });

        dSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putString("Notify At", range[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    @Override
    protected void onResume() { super.onResume(); }

    @Override
    protected void onPause() { super.onPause(); }

    @Override
    protected void onStop() { super.onStop(); }

    @Override
    protected void onDestroy() { super.onDestroy(); }

    private void toggleLimit(boolean checked, String dm, View[] views) {
        TextView toChange = findViewById(views[0].getId());
        if (checked){
            toChange.setTextColor(Color.parseColor("black"));
        }
        else{
            toChange.setTextColor(Color.parseColor("#B0B0B0"));
        }
        editor.putBoolean("Daily Limit", checked);
        editor.apply();

        for (View v : views) {
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