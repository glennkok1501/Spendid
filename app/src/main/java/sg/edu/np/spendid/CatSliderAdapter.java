//Created by Glenn

package sg.edu.np.spendid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CatSliderAdapter extends RecyclerView.Adapter<CatSliderViewHolder> {
    ArrayList<Category> data;
    TextView cat;
    int selectedPos = -1;
    CategoryHandler categoryHandler = new CategoryHandler();

    public CatSliderAdapter(ArrayList<Category> input, TextView select){
        data = input;
        cat = select;
    }

    public CatSliderViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_record_category_layout, parent, false);
        return new CatSliderViewHolder(item);
    }

    public void onBindViewHolder(CatSliderViewHolder holder, int position){
        Category s = data.get(position);
        holder.image.setImageResource(categoryHandler.setIcon(s.getTitle()));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPos = position;
                cat.setText(s.getTitle());
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

    public int getItemCount(){
        return data.size();
    }

}