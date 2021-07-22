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
    private Context context;
    private Calendar currentTime = Calendar.getInstance();
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private int limitAmt;
    private int notifyAt;
    private double limitedAmt;
    private boolean limit, notify, moreThan;
    private DBHandler dbHandler;
    private final String PREF_NAME = "sharedPrefs";

    public LimitNotification(Context context, DBHandler db, double amount) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        dbHandler = db;
        this.context = context;
        this.limit = prefs.getBoolean("Daily Limit", false);
//        this.notify = prefs.getBoolean("Notify", true);
        this.limitAmt = prefs.getInt("Limit Amount", 0);
//        this.notifyAt = prefs.getInt("Notify At", 8);
//        limitedAmt = (double) limitAmt * (notifyAt + 1) / 10;
        this.moreThan = getTotalBalance() + amount > (double) limitAmt * (notifyAt + 1) / 10;
    }

    public boolean checkExceed(){
         return limit && moreThan;
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
