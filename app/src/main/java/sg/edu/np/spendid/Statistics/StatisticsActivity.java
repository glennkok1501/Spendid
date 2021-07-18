package sg.edu.np.spendid.Statistics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Models.Category;
import sg.edu.np.spendid.Models.Record;
import sg.edu.np.spendid.R;

public class StatisticsActivity extends AppCompatActivity {

    private DBHandler dbHandler;
    private Calendar currentMonth;
    private TextView monthText;
    private final DecimalFormat df2 = new DecimalFormat("#0.00");
    private ArrayList<Category> cats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        initToolbar();

        dbHandler = new DBHandler(this, null, null, 1);
        currentMonth = Calendar.getInstance();
        cats = dbHandler.getCategories();
        monthText = findViewById(R.id.stats_month_textView);
        monthText.setText(new SimpleDateFormat("d MMMM yyyy").format(currentMonth.getTime()));


        LinearLayout numChart = findViewById(R.id.stats_numGraph_linearLayout);
        LinearLayout posChart = findViewById(R.id.stats_posGraph_linearLayout);
        LinearLayout negChart = findViewById(R.id.stats_negGraph_linearLayout);
        LinearLayout months = findViewById(R.id.stats_monthsGraph_linearLayout);

        double[] data = getMonthsData(5);

        String[] monthsRange = getLastMonths(5);

        new Charts(this, numChart, posChart, negChart, months).init(data, monthsRange);

        populateTable();

        HashMap<String, ArrayList<Record>> catMonthData = dbHandler.getGroupedTransaction(new SimpleDateFormat("yyyy-MM").format(currentMonth.getTime())+"-%");

        RecyclerView categoryExpenseRV = findViewById(R.id.stats_expense_RV);
        CatAdapter catExpenseAdapter = new CatAdapter(sortData(catMonthData));
        LinearLayoutManager expenseLayoutManager = new LinearLayoutManager(this);
        categoryExpenseRV.setLayoutManager(expenseLayoutManager);
        categoryExpenseRV.setItemAnimator(new DefaultItemAnimator());
        categoryExpenseRV.setAdapter(catExpenseAdapter);
    }

    private double[] getMonthsData(int range){
        double[] data = new double[range];
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
        for (int i = 0; i < range; i++){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -i);
            String[] formattedDate = monthFormat.format(cal.getTime()).split("-");
            data[(range-1)-i] = dbHandler.getBalance(formattedDate[0], formattedDate[1]).get("balance");
        }
        return data;

    }

    private String[] getLastMonths(int range){
        String[] monthsRange = new String[range];
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
        for (int i = 0; i < range; i++){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -i);
            monthsRange[(range-1)-i] = monthFormat.format(cal.getTime());
        }
        return monthsRange;
    }

    private void populateTable(){
        TextView tableInc = findViewById(R.id.stats_table_income);
        TextView tableExp = findViewById(R.id.stats_table_expense);

        TextView incRecord = findViewById(R.id.stats_table_incRecords);
        TextView expRecord = findViewById(R.id.stats_table_expRecords);

        TextView avgInc = findViewById(R.id.stats_table_incAvg);
        TextView avgExp = findViewById(R.id.stats_table_expAvg);

        TextView totalInc = findViewById(R.id.stats_table_incTotal);
        TextView totalExp = findViewById(R.id.stats_table_expTotal);

        TextView bal = findViewById(R.id.stats_table_balance);

        //initializing values
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
        String[] formattedDate = monthFormat.format(currentMonth.getTime()).split("-");
        ArrayList<Record> records = dbHandler.getMonthRecords(formattedDate[0], formattedDate[1]);

        double income = 0;
        double expense = 0;
        int incCount = 0;
        int expCount = 0;

        //iterate through records and sort base on income or expense
        int totalDays = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (Record record : records) {
            if (recordIsExpense(cats, record.getCategory())) {
                expense += record.getAmount();
                expCount += 1;
            } else {
                income += record.getAmount();
                incCount += 1;
            }
        }

        //set values in table
        tableInc.setText(String.valueOf(incCount));
        tableExp.setText(String.valueOf(expCount));
        incRecord.setText(df2.format(incCount/totalDays));
        expRecord.setText(df2.format(expCount/totalDays));
        avgInc.setText(df2.format(income/totalDays));
        avgExp.setText(df2.format(expense/totalDays));
        totalInc.setText(df2.format(income));
        totalExp.setText(df2.format(expense));
        bal.setText(df2.format(income-expense));

    }

    private boolean recordIsExpense(ArrayList<Category> cats, String cat){
        //find category object by name and check if expense
        for (Category category : cats){
            if (cat.equals(category.getTitle())){
                return category.isExpense();
            }
        }
        return false;
    }

    private HashMap<String, Double> sortData(HashMap<String, ArrayList<Record>> data){
        HashMap<String, Double> sortedData = new HashMap<>();
        double total = 0;
        for (Category cat : cats){
            //find for records that are expense
            if (cat.isExpense()){
                String catTitle = cat.getTitle();

                //initialize with value 0
                sortedData.put(catTitle, 0.0);

                //check if category exists in records
                if (data.containsKey(catTitle)){

                    //get total amount for category
                    for (Record record : data.get(catTitle)){
                        double add = sortedData.get(catTitle) + record.getAmount();
                        sortedData.put(catTitle, add);
                    }
                    total += sortedData.get(catTitle);
                }
            }
        }

        //convert to percentage
        for (String cat : new ArrayList<>(sortedData.keySet())){
            sortedData.put(cat, (sortedData.get(cat)/total)*100);
        }
        return  sortedData;
    }

    private void initToolbar(){
        //Tool Bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        activityTitle.setText("Statistics");
        backArrow.setImageResource(R.drawable.ic_clear_32);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}