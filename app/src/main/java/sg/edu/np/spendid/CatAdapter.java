package sg.edu.np.spendid;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CatAdapter extends RecyclerView.Adapter<CatViewHolder> {
    ArrayList<Category> data;
    TextView cat;
    ArrayList<View> itemViewList;

    public CatAdapter(ArrayList<Category> input, TextView select){
        data = input;
        cat = select;
        itemViewList = new ArrayList<View>();
    }

    public CatViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_record_category_layout, parent, false);
        CatViewHolder holder = new CatViewHolder(item);
        itemViewList.add(item);
        item.findViewById(R.id.newRecordCat_bubble).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(View i : itemViewList) {
                    CardView ic = i.findViewById(R.id.newRecordCat_bubble);
                    if (itemViewList.get(holder.getAdapterPosition()) == i) {
                        ic.setCardBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.purple_700));
                    } else {
                        ic.setCardBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.purple_200));
                    }
                    cat.setText(data.get(holder.getAdapterPosition()).getTitle());
                }
            }
        });
        return holder;
    }

    public void onBindViewHolder(CatViewHolder holder, int position){
        Category s = data.get(position);
        setIcon(holder, s.getTitle());
    }

    public int getItemCount(){
        return data.size();
    }

    private void setIcon(CatViewHolder holder, String cat) {
        switch (cat) {
            case "Shopping":
                holder.image.setImageResource(R.drawable.ic_shopping_24);
                break;
            case "Food":
                holder.image.setImageResource(R.drawable.ic_food_24);
                break;
            case "Entertainment":
                holder.image.setImageResource(R.drawable.ic_entertainment_24);
                break;
            case "Leisure":
                holder.image.setImageResource(R.drawable.ic_leisure_24);
                break;
            case "Transport":
                holder.image.setImageResource(R.drawable.ic_transport_24);
                break;
            case "Housing":
                holder.image.setImageResource(R.drawable.ic_housing_24);
                break;
            case "Vehicle":
                holder.image.setImageResource(R.drawable.ic_vehicle_24);
                break;
            case "Income":
                holder.image.setImageResource(R.drawable.ic_income_24);
                break;
            case "Salary":
                holder.image.setImageResource(R.drawable.ic_salary_24);
                break;
            case "Others":
                holder.image.setImageResource(R.drawable.ic_others_24);
                break;
        }
    }
}
