package sg.edu.np.spendid.Models;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import sg.edu.np.spendid.Database.DBHandler;

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
        dbHandler.addWallet(new Wallet("Outings", "", "SGD", "2019-10-25"));
        dbHandler.addWallet(new Wallet("Games", "", "USD", "2021-01-10"));

        //Create Records
        dbHandler.addRecord(new Record("Lunch at KFC", "Ate Fried Chickens!!", 14.50, "Food & Drinks", "2021-01-01", "13:10:02", null, 1));
        dbHandler.addRecord(new Record("Shopping", "", 158.95, "Shopping", "2021-01-02", "15:04:32", null, 1));
        dbHandler.addRecord(new Record("Good Dinner", "Best pasta ever", 9.00, "Food & Drinks", "2021-01-02", "20:43:23", null, 1));
        dbHandler.addRecord(new Record("Bought gifts", "New toy for baby cousin", 30.50, "Others", "2021-02-20", "10:00:55", null, 1));
        dbHandler.addRecord(new Record("New TV", "", 1999.00, "Entertainment", "2021-03-19", "18:54:12", null, 1));
        dbHandler.addRecord(new Record("Won lottery", "", 100.00, "Income", currentDate(), currentTime(), null, 1));
        dbHandler.addRecord(new Record("Salary", "", 4000.00, "Salary", currentDate(), currentTime(), null, 1));
        dbHandler.addRecord(new Record("Bonus", "", 2000.00, "Income", currentDate(), currentTime(), null, 1));

        dbHandler.addRecord(new Record("Airport meal", "Omurice Oishi", 20.00, "Food & Drinks", "2021-02-14", "8:02:11",null,  2));
        dbHandler.addRecord(new Record("Shopping", "Bought some new clothes", 274.93, "Shopping", "2021-02-15", "12:02:53", null, 2));
        dbHandler.addRecord(new Record("Last Dinner", "", 98.12, "Food & Drinks", "2021-02-16", "18:51:54", null, 2));

        dbHandler.addRecord(new Record("Graphic Card", "", 2000.00, "Entertainment", currentDate(), currentTime(), null, 3));
        dbHandler.addRecord(new Record("CPU and MOBO", "", 700.00, "Entertainment", currentDate(), currentTime(), null, 3));
        dbHandler.addRecord(new Record("Monitor", "", 300.00, "Entertainment", currentDate(), currentTime(), null, 3));

        dbHandler.addRecord(new Record("Pokemon Card", "Bought 2 packs", 30.80, "Entertainment", currentDate(), currentTime(), null, 4));
        dbHandler.addRecord(new Record("Figurines", "", 70.60, "Others", currentDate(), currentTime(), null, 4));
        dbHandler.addRecord(new Record("Twice Albums", "", 70.60, "Others", currentDate(), currentTime(), null, 4));

        dbHandler.addRecord(new Record("Gathering", "Watched a movie, went to the arcade and had dinner together", 40, "Leisure","2020-07-03" , "23:48:12", null, 5));
        dbHandler.addRecord(new Record("Meet up", "Had Dinner at Korean Restaurant", 25, "Food & Drinks","2020-09-03" , "20:20:23", null, 5));
        dbHandler.addRecord(new Record("Team Outing", "Team bonding trip with CCA at USS", 60, "Leisure","2021-04-17" , "14:00:23", null, 5));


        dbHandler.addRecord(new Record("COD:BO4", "", 60, "Entertainment", "2019-06-01", "20:04", null, 6));
        dbHandler.addRecord(new Record("COD:MW", "", 60, "Entertainment", "2019-06-01", "20:04", null, 6));
        dbHandler.addRecord(new Record("CS:GO", "", 14, "Entertainment", "2017-02-03", "13:00", null, 6));
        dbHandler.addRecord(new Record("Valorant", "", 180, "Entertainment", "2021-07-05", "20:04", null, 6));
        dbHandler.addRecord(new Record("LoL", "", 100, "Entertainment",currentDate() , currentTime(), null, 6));
    }

    public String currentDate(){
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(currentTime.getTime());
    }

}
