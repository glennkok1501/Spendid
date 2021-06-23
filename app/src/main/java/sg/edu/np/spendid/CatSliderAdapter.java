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

public class CatSliderAdapter extends RecyclerView.Adapter<CatSliderAdapter.CatViewHolder> {
    ArrayList<Category> data;
    TextView cat;
    ArrayList<View> itemViewList;
    CategoryHandler categoryHandler = new CategoryHandler();

    public CatSliderAdapter(ArrayList<Category> input, TextView select){
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
                    if (itemViewList.get(viewType) == i) {
                        ic.setCardBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.denim));
                    } else {
                        ic.setCardBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.fire_bush));
                    }
                    cat.setText(data.get(viewType).getTitle());
                }
            }
        });
        return holder;
    }

    public void onBindViewHolder(CatViewHolder holder, int position){
        Category s = data.get(position);
        holder.image.setImageResource(categoryHandler.setIcon(s.getTitle()));
    }

    public int getItemCount(){
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class CatViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        public CatViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.newRecordCat_imageView);
        }
    }
}