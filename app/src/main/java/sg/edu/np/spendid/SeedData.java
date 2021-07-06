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
        dbHandler.addWallet(new Wallet("Necessities", "", "SGD", "2020-12-25"));
        dbHandler.addWallet(new Wallet("Petrol", "", "SGD", "2019-05-28"));
        dbHandler.addWallet(new Wallet("Outings", "", "SGD", "2019-10-25"));
        dbHandler.addWallet(new Wallet("Games", "", "USD", "2021-01-10"));
        dbHandler.addWallet(new Wallet("Dates", "", "SGD", "2021-03-09"));

        //Create Records
        dbHandler.addRecord(new Record("Lunch at KFC", "Ate Fried Chickens!!", 14.50, "Food & Drinks", "2021-01-01", "13:10:02", 1));
        dbHandler.addRecord(new Record("Shopping", "", 158.95, "Shopping", "2021-01-02", "15:04:32", 1));
        dbHandler.addRecord(new Record("Good Dinner", "Best pasta ever", 9.00, "Food & Drinks", "2021-01-02", "20:43:23", 1));
        dbHandler.addRecord(new Record("Bought gifts", "New toy for baby cousin", 30.50, "Others", "2021-02-20", "10:00:55", 1));
        dbHandler.addRecord(new Record("New TV", "", 1999.00, "Entertainment", "2021-03-19", "18:54:12", 1));
        dbHandler.addRecord(new Record("Won lottery", "", 5000.00, "Income", currentDate(), currentTime(), 1));
        dbHandler.addRecord(new Record("Salary", "", 4000.00, "Income", currentDate(), currentTime(), 1));
        dbHandler.addRecord(new Record("Performance Bonus", "", 2000.00, "Income", currentDate(), currentTime(), 1));

        dbHandler.addRecord(new Record("Airport meal", "Omurice Oishi", 20.00, "Food & Drinks", "2021-02-14", "8:02:11", 2));
        dbHandler.addRecord(new Record("Shopping", "Bought some new clothes", 274.93, "Shopping", "2021-02-15", "12:02:53", 2));
        dbHandler.addRecord(new Record("Last Dinner", "", 98.12, "Food & Drinks", "2021-02-16", "18:51:54", 2));

        dbHandler.addRecord(new Record("Graphic Card", "", 2000.00, "Entertainment", currentDate(), currentTime(), 3));
        dbHandler.addRecord(new Record("CPU and MOBO", "", 700.00, "Entertainment", currentDate(), currentTime(), 3));
        dbHandler.addRecord(new Record("Monitor", "", 300.00, "Entertainment", currentDate(), currentTime(), 3));

        dbHandler.addRecord(new Record("Pokemon Card",
                "Bought 2 packs", 30.80, "Entertainment", currentDate(), currentTime(), 4));
        dbHandler.addRecord(new Record("Twice Albums", "", 70.60, "Others", currentDate(), currentTime(), 4));

        dbHandler.addRecord(new Record("Toiletries", "ToothBrush & Toilet Papers", 10.50, "Others", currentDate(), currentTime(), 5));
        dbHandler.addRecord(new Record("Personal Healthcare", "Face Wash & Lotion", 49.80, "Shopping", currentDate(), currentTime(), 5));
        dbHandler.addRecord(new Record("Groceries", "", 140.70, "Others", currentDate(), currentTime(), 5));

        dbHandler.addRecord(new Record("Petrol", "", 87.58, "Vehicle", "2019-06-01", "20:04", 6));
        dbHandler.addRecord(new Record("Petrol", "", 92.31, "Vehicle", "2020-02-11", "23:15", 6));
        dbHandler.addRecord(new Record("Petrol", "", 60.34, "Vehicle", "2021-02-14", currentTime(), 6));

        dbHandler.addRecord(new Record("Out with ITE Friends", "Watched a movie, went to the arcade and had dinner together", 40, "Leisure","2020-07-03" , "23:48", 7));
        dbHandler.addRecord(new Record("Out with Colleagues", "Had Dinner", 25, "Food & Drinks","2020-09-03" , "20:20", 7));
        dbHandler.addRecord(new Record("Out with CCA Exco Team", "Went Guitar Shopping", 1100, "Shopping","2021-04-17" , "22:00", 7));


        dbHandler.addRecord(new Record("COD:BO4", "", 60, "Entertainment", "2019-06-01", "20:04", 8));
        dbHandler.addRecord(new Record("COD:MW", "", 60, "Entertainment", "2019-06-01", "20:04", 8));
        dbHandler.addRecord(new Record("CS:GO", "", 14, "Entertainment", "2017-02-03", "13:00", 8));
        dbHandler.addRecord(new Record("Valorant", "", 180, "Entertainment", "2021-07-05", "20:04", 8));
        dbHandler.addRecord(new Record("LOL", "", 100, "Entertainment",currentDate() , currentTime(), 8));


        dbHandler.addRecord(new Record("SEA Aquarium", "Brought GF out to SEA Aquarium", 70, "Entertainment","2021-03-09" , "15:30", 9));
        dbHandler.addRecord(new Record("USS", "Brought GF out to USS", 160, "Entertainment","2021-04-08" , "21:39", 9));
        dbHandler.addRecord(new Record("SAM", "Brought GF out to SAM", 40, "Entertainment","2021-06-22" , "15:35", 9));
        dbHandler.addRecord(new Record("Movie", "Brought GF out to watch Fast 9", 30, "Entertainment","2021-06-25" , "17:15", 9));
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
