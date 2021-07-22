package sg.edu.np.spendid.RecurringEntry.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.spendid.Models.Category;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Utils.Helpers.CategoryHelper;

public class RecurCatSliderAdapter extends RecyclerView.Adapter<RecurCatSliderViewHolder>{
    ArrayList<Category> data;
    private TextView cat;
    private int selectedPos = -1;
    private CategoryHelper categoryHelper = new CategoryHelper();

    public RecurCatSliderAdapter(ArrayList<Category> input, TextView select){
        data = input;
        cat = select;
    }

    public RecurCatSliderViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_recurring_category_layout, parent, false);
        return new RecurCatSliderViewHolder(item);
    }

    public void onBindViewHolder(RecurCatSliderViewHolder holder, int position){
        Category category = data.get(position);
        holder.image.setImageResource(categoryHelper.setIcon(category.getTitle()));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPos = position;
                cat.setText(category.getTitle());
                notifyDataSetChanged();
            }
        });

        if (selectedPos == position){
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.denim));
        }
        else{
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.fire_bush));
        }
    }

    public int getItemCount(){return data.size(); }
}