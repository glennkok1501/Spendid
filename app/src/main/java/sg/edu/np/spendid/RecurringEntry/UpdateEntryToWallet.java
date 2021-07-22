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

    public void UpdateRecurring() throws ParseException {
        Calendar currentDate = Calendar.getInstance();
        for (Recurring recurring : recurringArrayList){
            updateEntry(recurring);
        }
    }

    private void updateEntry(Recurring r) throws ParseException {
        Date lastUpdated = sdf.parse(r.getLastUpdated());
        Date currentDate = removeTime(Calendar.getInstance().getTime());

        Calendar cal = Calendar.getInstance();
        cal.setTime(lastUpdated);
        while (cal.getTime().before(currentDate)){
            cal.add(Calendar.MONTH, 1);
            if(!cal.getTime().after(currentDate)){
                String tempDate = sdf.format(cal.getTime());
                dbHandler.addRecord(new Record(r.getRecurringtitle(), r.getRecurringdescription(), r.getAmount(), r.getCategory(), tempDate, "00:00:00", null, r.getWalletId()));
                dbHandler.UpdateRecurring(new Recurring(r.getRecurringId(),r.getRecurringtitle(), r.getRecurringdescription(), r.getAmount(), r.getCategory(), r.getRecurringstartDate(), null, tempDate, r.getWalletId()));
            }
        }

    }

    private Date removeTime(Date date) throws ParseException {
        String stringdate = sdf.format(date);
        return sdf.parse(stringdate);
    }
}
