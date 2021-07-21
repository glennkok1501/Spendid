package sg.edu.np.spendid.Statistics;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import sg.edu.np.spendid.R;

public class Charts {
    private Context context;
    private LinearLayout chart;
    private LinearLayout monthsChart;
    private final SimpleDateFormat sdf = new SimpleDateFormat("MMM");

    public Charts(Context context, LinearLayout chart, LinearLayout monthsChart) {
        this.context = context;
        this.chart = chart;
        this.monthsChart = monthsChart;
    }

    public void init(double[] data, Date[] months){
        double highest = getMax(data);
        for (int i = 0; i < data.length; i++) {
            LinearLayout bar = getBarLayout();
            if (data[i] > 0) {
                bar.addView(setBarData(convertPercent(data[i], highest), bar));
            }
            else{
                bar.addView(emptyBar(100, bar));
            }
            final double amt = data[i];
            final Date date = months[i];
            bar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AmountDialog(context).show(amt, date);
                }
            });
            chart.addView(bar);
            monthsChart.addView(getMonth(amt, date));
        }
    }

    private View getMonth(double amt, Date date){

        TextView monthText = new TextView(context);
        monthText.setText(sdf.format(date));
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
        );
        monthText.setGravity(Gravity.CENTER);
        monthText.setLayoutParams(textParams);
        monthText.setTextSize(14);

        monthText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AmountDialog(context).show(amt, date);
            }
        });

        return monthText;
    }

    private LinearLayout getBarLayout(){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1);
        parentParams.setMarginStart(20);
        parentParams.setMarginEnd(20);
        parentParams.gravity = Gravity.BOTTOM;
        linearLayout.setLayoutParams(parentParams);
        return linearLayout;
    }

    private View setBarData(int height, LinearLayout linearLayout){
        View view = new View(context);
        linearLayout.setBackgroundColor(context.getResources().getColor(R.color.fire_bush));
        LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                height*3,
                1);
        view.setLayoutParams(childParams);
        return view;
    }

    private View emptyBar(int height, LinearLayout linearLayout){
        View view = new View(context);
        LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                height*3,
                1);
        view.setLayoutParams(childParams);
        return view;
    }

    private double getMax(double[] nums){
        double max = nums[0];
        for (double num : nums){
            if (num > max){
                max = num;
            }
        }
        return max;
    }

    private int convertPercent(double num, double max){
        return (int) Math.round((num/max)*100);
    }


}
