package sg.edu.np.spendid.Statistics.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Statistics.AmountDialog;
import sg.edu.np.spendid.Utils.Helpers.CategoryHelper;

public class CatProgressAdapter extends RecyclerView.Adapter<CatProgressViewHolder> {
    HashMap<String, Double> data;
    private CategoryHelper categoryHandler = new CategoryHelper();
    private ArrayList<String> cats;
    private double highest;
    private final DecimalFormat df1 = new DecimalFormat("#0.#");

    public CatProgressAdapter(HashMap<String, Double> input, double highest) {
        data = input;
        cats = new ArrayList<>(input.keySet());
        this.highest = highest;
    }

    public CatProgressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.stats_cat_progress_layout, parent, false);
        return new CatProgressViewHolder(item);
    }

    public void onBindViewHolder(CatProgressViewHolder vh, int pos) {
        String cat = cats.get(pos);
        double amount = data.get(cat);
        double percent = convertPercent(amount, highest);

        vh.image.setImageResource(categoryHandler.setIcon(cat));
        vh.catName.setText(cat);
        vh.percent.setText(df1.format(percent)+"%");
        vh.bar.setProgress((int)percent);
        vh.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AmountDialog(vh.itemView.getContext()).show(amount, null);
            }
        });
    }

    public int getItemCount(){
        return data.size();
    }

    private double convertPercent(double num, double max){
        return (num/max)*100;
    }

}