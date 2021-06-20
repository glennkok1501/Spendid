package sg.edu.np.spendid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CatAdaptor extends RecyclerView.Adapter<CatAdaptor.CatViewHolder> {
    ArrayList<Category> categories;
    CategoryHandler categoryHandler = new CategoryHandler();

    public CatAdaptor(ArrayList<Category> cList) {
        categories = cList;
    }

    public CatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_category_layout, parent, false);
        CatViewHolder catViewHolder = new CatViewHolder(item);

        return catViewHolder;
    }

    public void onBindViewHolder(CatViewHolder vh, int pos) {
        Category c = categories.get(pos);
        vh.image.setImageResource(categoryHandler.setIcon(c.getTitle()));
        vh.catName.setText(c.getTitle());
    }

    public int getItemCount(){
        return categories.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class CatViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView catName;
        public CatViewHolder(View item){
            super(item);
            image = item.findViewById(R.id.cat_imageView);
            catName = item.findViewById(R.id.name_Category_textView);
        }
    }
}
