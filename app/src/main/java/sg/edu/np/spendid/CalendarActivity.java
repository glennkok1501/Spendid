package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import static java.lang.Math.round;

public class CalendarActivity extends AppCompatActivity {
    private Calendar calendar;
    private int maxDays;
    private CalendarAdapter calendarAdapter;
    private ImageView backArrow, nextArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        backArrow = findViewById(R.id.calendarBack_imageView);
        nextArrow = findViewById(R.id.calendarNext_imageView);
        calendar = Calendar.getInstance();
//        calendar.add(Calendar.MONTH, -5);
        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        TextView month = findViewById(R.id.calendarMonth_textView);
        month.setText(new SimpleDateFormat("MMMM yyyy").format(calendar.getTime()));

        RecyclerView recyclerView = findViewById(R.id.dailyGoal_RV);
        calendarAdapter = new CalendarAdapter(maxDays, month);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 7);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(calendarAdapter);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarAdapter.changeCalendar(-1);
            }
        });

        nextArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarAdapter.changeCalendar(1);
            }
        });

        LinearLayout posChart = findViewById(R.id.stats_posGraph_linearLayout);
        LinearLayout negChart = findViewById(R.id.stats_negGraph_linearLayout);
        int[] data = new int[5];
        data[0] = 60;
        data[1] = 30;
        data[2] = -200;
        data[3] = 500;
        data[4] = -50;
        initChart(posChart, negChart, data);

    }
    private void initChart(LinearLayout posChart, LinearLayout negChart, int[] data){
        int highest = getMax(data);
        int lowest = getMin(data);
        for (int num : data) {
            if (num > 0) {
                LinearLayout posBar = getBarLayout(true);
                posBar.addView(setBarData(convertPercent(num, highest), posBar));
                posChart.addView(posBar);
                negChart.addView(setSpace());
            }
            else{
                LinearLayout negBar = getBarLayout(false);
                negBar.addView(setBarData(convertPercent(num*-1, lowest*-1), negBar));
                negChart.addView(negBar);
                posChart.addView(setSpace());

            }
        }
    }

    private LinearLayout getBarLayout(boolean gravityBottom){
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1);
        parentParams.setMarginStart(20);
        parentParams.setMarginEnd(20);
        if (gravityBottom){
            parentParams.gravity = Gravity.BOTTOM;
        }
        linearLayout.setLayoutParams(parentParams);
        return linearLayout;
    }

    private Space setSpace(){
        Space space = new Space(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1);
        params.setMarginStart(20);
        params.setMarginEnd(20);
        space.setLayoutParams(params);
        return space;
    }

    private View setBarData(int height, LinearLayout linearLayout){
        View view = new View(this);
        linearLayout.setBackgroundColor(getResources().getColor(R.color.fire_bush));
        LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                height*5,
                1);
        view.setLayoutParams(childParams);
        return view;
    }

    private int getMax(int[] nums){
        int max = nums[0];
        for (int num : nums){
            if (num > max){
                max = num;
            }
        }
        return max;
    }

    private int getMin(int[] nums){
        int min = nums[0];
        for (int num : nums){
            if (num < min){
                min = num;
            }
        }
        return min;
    }

    private int convertPercent(int num, int max){
        return (int) Math.round(((double)num/max)*100);
    }
}