package sg.edu.np.spendid.Utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Dialogs.MyAlertDialog;
import sg.edu.np.spendid.Models.Record;

import static android.content.Context.MODE_PRIVATE;

public class LimitNotification {
    private Context context;
    private Calendar currentTime = Calendar.getInstance();
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private String limitAmt;
    private int notifyAt;
    private double limitedAmt;
    private boolean limit, notify, moreThan, result;

    private DBHandler dbHandler;
    private final String PREF_NAME = "sharedPrefs";

    public LimitNotification(Context activityContext, DBHandler db, double amount) {
        dbHandler = db;
        this.context = activityContext;
        this.limit = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).getBoolean("Daily Limit", false);
        this.notify = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).getBoolean("Notify", true);
        this.limitAmt = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString("Limit Amount", "50");
        this.notifyAt = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).getInt("Notify At", 8);
        limitedAmt = Double.valueOf(limitAmt) * (notifyAt + 1) / 10;
        this.moreThan = getTotalBalance() + amount > limitedAmt;
    }

    public boolean isResult() {
        return result;
    }

    public boolean isNotifyOn() {
        if (limit && notify) {
            return true;
        }
        return false;
    }

    public void exceedMessage() {
        result = true;
        if (moreThan) {
            MyAlertDialog dialog = new MyAlertDialog(this.context);
            dialog.setTitle("Limit Exceeded");
            dialog.setBody("You will exceed your daily limit.\nDo you want to proceed?");

            dialog.setPositive().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    result = true;
                    dialog.dismiss();
                }
            });

            dialog.setNegative().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    result = false;
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    private double getTotalBalance() {
        double total = 0;
        HashMap<String, ArrayList<Record>> recordsToday = dbHandler.getGroupedTransaction(df.format(currentTime.getTime()));
        for (ArrayList<Record> rList : recordsToday.values()) {
            for (Record r : rList) {
                total += r.getAmount();
            }
        }
        return total;
    }
}
