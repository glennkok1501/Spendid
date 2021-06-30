package sg.edu.np.spendid;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CatViewHolder extends RecyclerView.ViewHolder {
    ImageView image;
    TextView catName;
    public CatViewHolder(View item){
        super(item);
        image = item.findViewById(R.id.cat_imageView);
        catName = item.findViewById(R.id.name_Category_textView);
    }
}