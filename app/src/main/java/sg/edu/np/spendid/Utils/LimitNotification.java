package sg.edu.np.spendid.Utils;

import android.content.Context;
import android.content.SharedPreferences;
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
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private boolean limit, notify, moreThanLimit, moreThanWarning;
    private DBHandler dbHandler;
    private final String PREF_NAME = "sharedPrefs";

    public LimitNotification(Context context, DBHandler db, double amount) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        dbHandler = db;
        limit = prefs.getBoolean("Daily Limit", false);
        notify = prefs.getBoolean("Notify", true);
        int notifyAt = prefs.getInt("Notify At", 8);
        double limitAmt = prefs.getInt("Limit Amount", 0);
        double limitedAmt = (double) limitAmt * (notifyAt + 1) / 10;
        moreThanWarning = getTotalBalance() + amount > (double) limitAmt * (notifyAt + 1) / 10;
        moreThanLimit = getTotalBalance() + amount > (double) limitedAmt;
    }

    public boolean checkExceedWarning() { return notify && moreThanWarning; }

    public boolean checkExceedLimit() { return limit && moreThanLimit; }


    private double getTotalBalance() {
        double total = 0;
        HashMap<String, ArrayList<Record>> recordsToday = dbHandler.getGroupedTransaction(df.format(Calendar.getInstance().getTime()));
        for (ArrayList<Record> rList : recordsToday.values()) {
            for (Record r : rList) {
                total += r.getAmount();
            }
        }
        return total;
    }
}
