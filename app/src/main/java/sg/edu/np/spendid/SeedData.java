package sg.edu.np.spendid;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SeedData {
    private Context context;

    public SeedData(Context context){
        this.context = context;
    }

    public void initDatabase(){
        DBHandler dbHandler = new DBHandler(context, null, null, 1);

        //Create Wallets
        dbHandler.addWallet(new Wallet("General", "Daily wallet", "SGD", "2021-01-01"));
        dbHandler.addWallet(new Wallet("Japan Trip", "Japan trip expenses", "JPY", "2021-02-13"));
        dbHandler.addWallet(new Wallet("Computer", "Computer parts costs", "SGD", currentDate()));
        dbHandler.addWallet(new Wallet("Hobby", "", "USD", "2021-06-10"));

        //Create Category
        dbHandler.addCategory(new Category("Shopping", true));
        dbHandler.addCategory(new Category("Food", true));
        dbHandler.addCategory(new Category("Entertainment", true));
        dbHandler.addCategory(new Category("Leisure", true));
        dbHandler.addCategory(new Category("Transport", true));
        dbHandler.addCategory(new Category("Housing", true));
        dbHandler.addCategory(new Category("Vehicle", true));
        dbHandler.addCategory(new Category("Income", false));
        dbHandler.addCategory(new Category("Salary", false));
        dbHandler.addCategory(new Category("Others", true));

        //Create Records
        dbHandler.addRecord(new Record("Lunch at KFC", "Ate Fried Chickens!!", 14.50, "Food", "2021-01-01", "13:10:02", 1));
        dbHandler.addRecord(new Record("Shopping", "", 158.95, "Shopping", "2021-01-02", "15:04:32", 1));
        dbHandler.addRecord(new Record("Good Dinner", "Best pasta ever", 9.00, "Food", "2021-01-02", "20:43:23", 1));
        dbHandler.addRecord(new Record("Bought gifts", "New toy for baby cousin", 30.50, "Others", "2021-02-20", "10:00:55", 1));
        dbHandler.addRecord(new Record("New TV", "", 1999.00, "Entertainment", "2021-03-19", "18:54:12", 1));

        dbHandler.addRecord(new Record("Airport meal", "Omurice Oishi", 20.00, "Food", "2021-02-14", "8:02:11", 2));
        dbHandler.addRecord(new Record("Shopping", "Bought some new clothes", 274.93, "Shopping", "2021-02-15", "12:02:53", 2));
        dbHandler.addRecord(new Record("Last Dinner", "", 98.12, "Food", "2021-02-16", "18:51:54", 2));

        dbHandler.addRecord(new Record("Graphic Card", "", 2000.00, "Entertainment", currentDate(), currentTime(), 3));
        dbHandler.addRecord(new Record("CPU and MOBO", "", 700.00, "Entertainment", currentDate(), currentTime(), 3));
        dbHandler.addRecord(new Record("Monitor", "", 300.00, "Entertainment", currentDate(), currentTime(), 3));

        dbHandler.addRecord(new Record("Pokemon Card", "Bought 2 packs", 30.80, "Entertainment", currentDate(), currentTime(), 4));
        dbHandler.addRecord(new Record("Twice Albums", "", 70.60, "Others", currentDate(), currentTime(), 4));

    }

    public String currentDate(){
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(currentTime.getTime());
    }

    public String currentTime(){
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(currentTime.getTime());
    }
}
