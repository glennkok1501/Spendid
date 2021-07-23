package sg.edu.np.spendid.RecurringEntry;

import android.content.Context;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Models.Record;
import sg.edu.np.spendid.Models.Recurring;

public class UpdateEntryToWallet {
    private ArrayList<Recurring> recurringArrayList;
    private DBHandler dbHandler;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public UpdateEntryToWallet(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
        recurringArrayList = dbHandler.getAllRecurring();
    }
    //check if there is or no recurringenddate. If have, add entry
    public void UpdateRecurring() throws ParseException {
        for (Recurring recurring : recurringArrayList){
            if (recurring.getRecurringendDate() == null){
                updateEntry(recurring);
            }
        }
    }
    //to update the last updated date og the app, so it can keep track and charge accordingly
    public void updateEntry(Recurring r) throws ParseException {
        Date lastUpdated = sdf.parse(r.getLastUpdated());
        Date currentDate = removeTime(Calendar.getInstance().getTime());

        Calendar cal = Calendar.getInstance();
        cal.setTime(lastUpdated);
        while (cal.getTime().before(currentDate)){
            String frequency = r.getFrequency();
            switch (frequency){
                case "Daily":
                    increment(cal, currentDate, Calendar.DAY_OF_MONTH, r);
                    break;
                case "Monthly":
                    increment(cal, currentDate, Calendar.MONTH, r);
                    break;
                default:
                    increment(cal, currentDate, Calendar.YEAR, r);
                    break;
            }
        }
    }
    //remove time and keep date only
    private Date removeTime(Date date) throws ParseException {
        String stringDate = sdf.format(date);
        return sdf.parse(stringDate);
    }
    //self increment according to frequency and add the record accordingly. Also updates the last updated so the app can keep track when it added in the transaction
    private void increment(Calendar cal, Date currentDate, int field, Recurring r){
        cal.add(field, 1);
        if(!cal.getTime().after(currentDate)){
            String tempDate = sdf.format(cal.getTime());

            dbHandler.addRecord(new Record(
                    r.getRecurringtitle(),
                    r.getRecurringdescription(),
                    r.getAmount(), r.getCategory(),
                    tempDate,
                    "00:00:00",
                    null,
                    r.getWalletId()));

            dbHandler.UpdateRecurring(new Recurring(
                    r.getRecurringId(),
                    r.getRecurringtitle(),
                    r.getRecurringdescription(),
                    r.getAmount(),
                    r.getCategory(),
                    r.getRecurringstartDate(),
                    null,
                    tempDate,
                    r.getWalletId(),
                    r.getFrequency()));
        }
    }

}