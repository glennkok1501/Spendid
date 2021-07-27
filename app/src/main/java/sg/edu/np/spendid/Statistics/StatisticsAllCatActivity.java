package sg.edu.np.spendid.Statistics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Statistics.Adapters.CatAdapter;
import sg.edu.np.spendid.Statistics.Adapters.CatProgressAdapter;

public class StatisticsAllCatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_all_cat);

        initToolbar();
        RecyclerView categoryExpenseRV = findViewById(R.id.stats_all_cats_RV);
        TextView expenseEmpty = findViewById(R.id.stats_allExpenseEmpty_textView);

        //get category expense data from intent
        Intent intent = getIntent();

        //use serializable to get hashmap
        HashMap<String, Double> expenseCat = (HashMap<String, Double>) intent.getSerializableExtra("data");

        //check if data is empty
        if (expenseCat.size() > 0){
            expenseEmpty.setVisibility(View.GONE);
            double highest = getTotal(expenseCat);
            //stats_all_cats_RV
            CatProgressAdapter catProgressAdapter = new CatProgressAdapter(expenseCat, highest);
            LinearLayoutManager expenseLayoutManager = new LinearLayoutManager(this);
            categoryExpenseRV.setLayoutManager(expenseLayoutManager);
            categoryExpenseRV.setItemAnimator(new DefaultItemAnimator());
            categoryExpenseRV.setAdapter(catProgressAdapter);
        }
        else{
            expenseEmpty.setText("You have no expenses");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //calculate total value
    private double getTotal(HashMap<String, Double> map){

        //placeholder value
        double total = 0.0;

        //iterate through hashmap
        for (String cat : map.keySet()){
            total += map.get(cat);
        }
        return total;
    }

    private void initToolbar(){
        //Tool Bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        activityTitle.setText("Expense Category");
        backArrow.setImageResource(R.drawable.ic_back_arrow_32);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}