package sg.edu.np.spendid.Statistics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import sg.edu.np.spendid.Database.DBHandler;

public class SortData {
    private DBHandler dbHandler;
    private int range;
    private ArrayList<HashMap<String, Double>> data;
    private Date[] dateList;

    public SortData(DBHandler dbHandler, int range) {
        this.dbHandler = dbHandler;
        this.range = range;
        data = new ArrayList<>();
        dateList = new Date[range];
        initData();
    }

    public Date[] getDateList() {
        return dateList;
    }

    private void initData(){
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");

        /*
        get balance of past (range) months
        and insert into array
         */
        for (int i = 0; i < range; i++){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -i);

            //set month in reverse
            dateList[(range-1)-i] = cal.getTime();

            //separate year and month
            String[] formattedDate = monthFormat.format(cal.getTime()).split("-");

            //get balance from database
            data.add(0, dbHandler.getBalance(formattedDate[0], formattedDate[1]));
        }
    }

    /*
    return array of amount
    based on income, expense, balance
    in reverse order, respective to months
     */
    public double[] getData(String key){
        double[] dataSet = new double[range];
        for (int i = 0; i < range; i++){
            int index = (range-1)-i;
            dataSet[index] = data.get(index).get(key);
        }
        return dataSet;
    }
}
