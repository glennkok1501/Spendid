package sg.edu.np.spendid.Statistics.Adapters;

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
    private final DecimalFormat df2 = new DecimalFormat("#0.00");

    public CatAdapter(HashMap<String, Double> input) {
        data = input;
        cats = sort(input);
    }

    public CatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.stats_cat_layout, parent, false);
        return new CatViewHolder(item);
    }

    public void onBindViewHolder(CatViewHolder vh, int pos) {
        String cat = cats.get(pos);
        double amount = data.get(cat);
        vh.image.setImageResource(categoryHandler.setIcon(cat));
        vh.catName.setText(cat);
        vh.percent.setText(df2.format(amount));
    }

    public int getItemCount(){
        return Math.min(cats.size(), 5);
    }

    private ArrayList<String> sort(HashMap<String, Double> input){
        ArrayList<String> sortedCats = new ArrayList<>(input.keySet());
        int n = sortedCats.size();
        String temp;
        for(int i = 0; i < n; i++){
            for(int j = 1; j < (n-i); j++){
                if(input.get(sortedCats.get(j-1)) < input.get(sortedCats.get(j))){
                    //swap elements
                    temp = sortedCats.get(j-1);
                    sortedCats.remove(j-1);
                    sortedCats.add(j, temp);
                }

            }
        }
        return sortedCats;
    }

}