package sg.edu.np.spendid.Utils;

import android.content.Context;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager2.widget.ViewPager2;

import sg.edu.np.spendid.R;

public class ViewPagerIndicators {
    private Context context;
    private ViewPager2 viewPager;
    private LinearLayout indicators;

    public ViewPagerIndicators(Context context, ViewPager2 viewPager, LinearLayout indicators) {
        this.context = context;
        this.viewPager = viewPager;
        this.indicators = indicators;
    }

    public void init(int size){

        //create textview array to store dots based on no. of wallets
        TextView[] dots = new TextView[size];
        viewPagerIndicators(dots, indicators);
        //change color depending on which page
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dots.length; i++){
                    if (i == position){
                        dots[i].setTextColor(context.getResources().getColor(R.color.fire_bush));
                    }
                    else{
                        dots[i].setTextColor(context.getResources().getColor(R.color.light_grey));
                    }
                }
                super.onPageSelected(position);
            }
        });
    }

    //create view pager indicators
    private void viewPagerIndicators(TextView[] d, LinearLayout l){
        l.removeAllViews(); //reset textview array
        //add new TextView with bullet points into linear layout depending on viewpager size
        for (int i = 0; i < d.length; i++){
            d[i] = new TextView(context);
            d[i].setText(Html.fromHtml(context.getResources().getString(R.string.dot)));
            d[i].setTextSize(18);
            l.addView(d[i]);
        }
    }
}
