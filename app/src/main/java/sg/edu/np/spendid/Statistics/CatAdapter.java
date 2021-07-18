package sg.edu.np.spendid.Statistics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Utils.Helpers.CategoryHelper;

public class CatAdapter extends RecyclerView.Adapter<CatViewHolder> {
    HashMap<String, Double> data;
    private CategoryHelper categoryHandler = new CategoryHelper();
    private ArrayList<String> cats;
    private DecimalFormat df1 = new DecimalFormat("#0.0");

    public CatAdapter(HashMap<String, Double> input) {
        data = input;
        cats = new ArrayList<>(data.keySet());
    }

    public CatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.stats_cat_layout, parent, false);
        return new CatViewHolder(item);
    }

    public void onBindViewHolder(CatViewHolder vh, int pos) {
        String cat = cats.get(pos);
        double percent = data.get(cat);
        vh.image.setImageResource(categoryHandler.setIcon(cat));
        vh.catName.setText(cat);
        vh.percent.setText(df1.format(percent)+"%");

    }

    public int getItemCount(){
        return data.size();
    }

}