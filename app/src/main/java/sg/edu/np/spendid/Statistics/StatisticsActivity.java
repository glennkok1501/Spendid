package sg.edu.np.spendid.Statistics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import sg.edu.np.spendid.Dashboard.Adapters.WalletSlider.WalletSliderAdapter;
import sg.edu.np.spendid.Dashboard.MainActivity;
import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Models.Category;
import sg.edu.np.spendid.Models.Record;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Statistics.Adapters.CatAdapter;
import sg.edu.np.spendid.Statistics.Adapters.ChartsAdapter;
import sg.edu.np.spendid.Utils.ViewPagerIndicators;

public class StatisticsActivity extends AppCompatActivity {

    private DBHandler dbHandler;
    private Calendar currentMonth;
    private TextView monthText;
    private ArrayList<Category> cats;
    private final DecimalFormat df2 = new DecimalFormat("#0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        initToolbar();

        RecyclerView categoryExpenseRV = findViewById(R.id.stats_expense_RV);
        TextView emptyExpense = findViewById(R.id.stats_expenseEmpty_textView);
        dbHandler = new DBHandler(this, null, null, 1);
        currentMonth = Calendar.getInstance();
        cats = dbHandler.getCategories();
        monthText = findViewById(R.id.stats_month_textView);
        monthText.setText(new SimpleDateFormat("d MMMM yyyy").format(currentMonth.getTime()));

        //initiate data for graphs
        int range = 6; //indicates number of months back, 6 would be last 6 months

        //get array of months
        Date[] monthsRange = getLastMonths(range);

        //get array of data
        ArrayList<double[]> input = new ArrayList<>();
        SortData sortData = new SortData(dbHandler, range);
        input.add(sortData.getData("balance"));
        input.add(sortData.getData("income"));
        input.add(sortData.getData("expense"));

        TextView monthBal = findViewById(R.id.stats_bal_textView);
        monthBal.setText(df2.format(input.get(0)[range-1]));

        ViewPager2 charts = findViewById(R.id.stats_graph_viewpager);
        ChartsAdapter chartsAdapter = new ChartsAdapter(this, input, monthsRange);
        charts.setAdapter(chartsAdapter);

        LinearLayout indicators = findViewById(R.id.stats_graph_indicators);
        new ViewPagerIndicators(this, charts, indicators).init(input.size());

        HashMap<String, ArrayList<Record>> catMonthData = dbHandler.getGroupedTransaction(new SimpleDateFormat("yyyy-MM").format(currentMonth.getTime())+"-%");

        HashMap<String, Double> expenseCat = catData(catMonthData);
        if (expenseCat.size() == 0){
            emptyExpense.setText("You have no expense");
        }
        else{
            emptyExpense.setVisibility(View.GONE);
            CatAdapter catExpenseAdapter = new CatAdapter(expenseCat);
            LinearLayoutManager expenseLayoutManager = new LinearLayoutManager(this);
            categoryExpenseRV.setLayoutManager(expenseLayoutManager);
            categoryExpenseRV.setItemAnimator(new DefaultItemAnimator());
            categoryExpenseRV.setAdapter(catExpenseAdapter);
        }

        TextView viewAllCats = findViewById(R.id.stats_viewCat_textView);
        viewAllCats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticsActivity.this, StatisticsAllCatActivity.class);
                intent.putExtra("data", expenseCat);
                startActivity(intent);
            }
        });
    }

    private Date[] getLastMonths(int range){
        Date[] monthsRange = new Date[range];
        for (int i = 0; i < range; i++){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -i);
            monthsRange[(range-1)-i] = cal.getTime();
        }
        return monthsRange;
    }

        private HashMap<String, Double> catData(HashMap<String, ArrayList<Record>> data){
            HashMap<String, Double> sortedData = new HashMap<>();
            for (Category cat : cats){

                //find for records that are expense
                if (cat.isExpense() && data.containsKey(cat.getTitle())){
                    String catTitle = cat.getTitle();
                    sortedData.put(catTitle, 0.0);

                    //get total amount for category
                        for (Record record : data.get(catTitle)){
                            double add = sortedData.get(catTitle) + record.getAmount();
                            sortedData.put(catTitle, add);
                        }
                    }
                }
        return sortedData;
    }

    private void initToolbar(){
        //Tool Bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        activityTitle.setText("Statistics");
        backArrow.setImageResource(R.drawable.ic_back_arrow_32);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}