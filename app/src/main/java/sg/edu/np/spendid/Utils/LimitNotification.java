package sg.edu.np.spendid.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Dialogs.MyAlertDialog;
import sg.edu.np.spendid.Misc.DailyLimitActivity;
import sg.edu.np.spendid.Models.Record;
import sg.edu.np.spendid.R;

import static android.content.Context.MODE_PRIVATE;

public class LimitNotification {
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private boolean limit, notify, moreThanLimit, moreThanWarning;
    private DBHandler dbHandler;
    private final String PREF_NAME = "sharedPrefs";
    private Context context;
    private int notifyAt;

    public LimitNotification(Context context, DBHandler db, double amount) {
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        dbHandler = db;
        limit = prefs.getBoolean("Daily Limit", false);
        notify = prefs.getBoolean("Notify", false);
        notifyAt = prefs.getInt("Notify At", 8);
        int limitAmt = prefs.getInt("Limit Amount", 0);
        moreThanWarning = (getTotalBalance() + amount >= (double) limitAmt * (notifyAt + 1) / 10);
        moreThanLimit = getTotalBalance() + amount > (double) limitAmt;
    }

    public void checkExceedWarning(boolean isExpense) {
        if (notify && moreThanWarning && isExpense){
            showNotification();
        }
    }

    public boolean checkExceedLimit() { return limit && moreThanLimit; }


    private double getTotalBalance() {
        String date = df.format(Calendar.getInstance().getTime());
        return dbHandler.getDailyExpense(date);
    }

    public void showNotification(){
        String channelId = "Spendid_Channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(channelId, "Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        String message = "You have spend at least "+(notifyAt+1)*10+"% of your desired limit";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_spendid_256)
                .setContentTitle(context.getResources().getString(R.string.app_name)+" Daily Limit")
                .setContentText(message)
                .setAutoCancel(true);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1, builder.build());
    }
}
