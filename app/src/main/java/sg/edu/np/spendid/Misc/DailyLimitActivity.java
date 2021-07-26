package sg.edu.np.spendid.Misc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.GnssNavigationMessage;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import sg.edu.np.spendid.R;

public class DailyLimitActivity extends AppCompatActivity {
    private SwitchMaterial dSwitch, dNotif;
    private EditText dAmtEdit, saveAmt, saveD, saveM, saveY;
    private Button calcAmt;
    private Spinner dSpinner;
    private CardView results;
    private LinearLayout notifyAt;
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
        saveAmt = findViewById(R.id.save_amt_editText);
        saveD = findViewById(R.id.save_amt__date_editText);
        saveM = findViewById(R.id.save_amt__month_editText);
        saveY = findViewById(R.id.save_amt__year_editText);
        calcAmt = findViewById(R.id.calcLimit_Btn);
        results = findViewById(R.id.save_results_cardView);
        notifyAt = findViewById(R.id.limit_notify_me_linearLayout);

        initToolbar(); //set toolbar
        editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        results.setVisibility(View.GONE);

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
        if (dNotif.isChecked()) {
            notifyAt.setVisibility(View.VISIBLE);
        } else {
            notifyAt.setVisibility(View.GONE);
        }

        dSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleLimit(isChecked, dViews);
            }
        });

        dNotif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    notifyAt.setVisibility(View.VISIBLE);
                }
                else {
                    notifyAt.setVisibility(View.GONE);
                }
            }
        });

        calcAmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAmt();
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
        String amountString = dAmtEdit.getText().toString();
        if (amountString.length() == 0){
            amountString = "0";
        }
        editor.putInt("Limit Amount", Integer.parseInt(amountString));

        //save notify me status
        editor.putBoolean("Notify", dNotif.isChecked());

        //save percentage position of spinner
        editor.putInt("Notify At", dSpinner.getSelectedItemPosition());

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

    private void calculateAmt() {
        int amt, savingsPerDay;
        TextView calcSaveAmt = findViewById(R.id.save_amt_textView),
                recoLimit = findViewById(R.id.limit_recommended_textView);
        Date dateToCalc;
        String dateStr, d = saveD.getText().toString(), m = saveM.getText().toString(), y = saveY.getText().toString();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);

        //input validation for amount to save
        if (saveAmt.getText().toString().equals("")) {
            Toast.makeText(DailyLimitActivity.this, "Enter an amount first", Toast.LENGTH_SHORT).show();
            return;
        } else {
            amt = Integer.parseInt(saveAmt.getText().toString());
        }

        //input validation for date
        if (d.equals("") || m.equals("") || y.equals("")) { //none of the date inputs should be blank
            Toast.makeText(DailyLimitActivity.this, "Enter a date first", Toast.LENGTH_SHORT).show();
            return;
        } else {
            dateStr = d + "/" + m + "/" + y; //date can still be invalid
        }

        try {
            dateToCalc = df.parse(dateStr);
            //check if date specified is after the current day
            if (dateToCalc.getTime() <= Calendar.getInstance().getTimeInMillis()) {
                Toast.makeText(DailyLimitActivity.this, "Please enter a date after today", Toast.LENGTH_SHORT).show();
            } else {
                double numDays = ((dateToCalc.getTime() - Calendar.getInstance().getTimeInMillis()) / (1000 * 60 * 60 * 24)) + 1;
                savingsPerDay = (int) Math.ceil(amt / numDays);
                calcSaveAmt.setText(String.valueOf(savingsPerDay));

                //recommended limit is 50 - savings per day
                if (dAmtEdit.getText().toString().equals("")) {
                    //if recommended limit < 30, set recommended limit as 30
                    if (50 - savingsPerDay < 30) {
                        recoLimit.setText("30");
                    } else {
                        recoLimit.setText(String.valueOf(50 - savingsPerDay));
                    }
                } else {
                    //if recommended limit < 30, set recommended limit as 30
                    if (Integer.parseInt(dAmtEdit.getText().toString()) - savingsPerDay < 30) {
                        recoLimit.setText("30");
                    } else {
                        recoLimit.setText(String.valueOf(Integer.parseInt(dAmtEdit.getText().toString()) - savingsPerDay));
                    }
                }
                results.setVisibility(View.VISIBLE);
            }
        } catch (ParseException e) {
            Toast.makeText(DailyLimitActivity.this, "Invalid Date", Toast.LENGTH_SHORT).show();
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