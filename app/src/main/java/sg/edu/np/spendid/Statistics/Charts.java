package sg.edu.np.spendid.Statistics;

import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.text.DecimalFormat;

import sg.edu.np.spendid.R;

public class Charts {
    private Context context;
    private LinearLayout numChart;
    private LinearLayout posChart;
    private LinearLayout negChart;
    private LinearLayout monthsChart;
    private final DecimalFormat df2 = new DecimalFormat("#0.00");

    public Charts(Context context, LinearLayout numChart, LinearLayout posChart, LinearLayout negChart, LinearLayout monthsChart) {
        this.context = context;
        this.numChart = numChart;
        this.posChart = posChart;
        this.negChart = negChart;
        this.monthsChart = monthsChart;

    }

    public void init(double[] data, String[] months){
        double highest = getMax(data);
        double lowest = getMin(data);
        for (int i = 0; i < data.length; i++) {
            if (data[i] > 0) {
                LinearLayout posBar = getBarLayout(true);
                posBar.addView(setBarData(convertPercent(data[i], highest), posBar));
                posChart.addView(posBar);
                negChart.addView(setSpace());
            }
            else if (data[i] < 0) {
                LinearLayout negBar = getBarLayout(false);
                negBar.addView(setBarData(convertPercent(data[i]*-1, lowest*-1), negBar));
                negChart.addView(negBar);
                posChart.addView(setSpace());
            }
            else{
                posChart.addView(setSpace());
                negChart.addView(setSpace());
            }
            monthsChart.addView(getText(months[i]));
            numChart.addView(getText(df2.format(data[i])));
        }

    }

    private View getText(String text){
        TextView monthText = new TextView(context);
        monthText.setText(text);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
        );
        monthText.setGravity(Gravity.CENTER);
        monthText.setLayoutParams(textParams);
        monthText.setTextSize(14);
        return monthText;
    }

    private LinearLayout getBarLayout(boolean gravityBottom){
        LinearLayout linearLayout = new LinearLayout(context);
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
        Space space = new Space(context);
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
        View view = new View(context);
        linearLayout.setBackgroundColor(context.getResources().getColor(R.color.fire_bush));
        LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                height*2,
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

    private double getMin(double[] nums){
        double min = nums[0];
        for (double num : nums){
            if (num < min){
                min = num;
            }
        }
        return min;
    }

    private int convertPercent(double num, double max){
        return (int) Math.round((num/max)*100);
    }


}
