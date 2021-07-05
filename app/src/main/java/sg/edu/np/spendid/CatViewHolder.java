package sg.edu.np.spendid;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CatViewHolder extends RecyclerView.ViewHolder {
    CardView catCard;
    ImageView image;
    TextView catName;
    ImageView editBtn;
    public CatViewHolder(View item){
        super(item);
        catCard = item.findViewById(R.id.viewCategory_cardView);
        image = item.findViewById(R.id.cat_imageView);
        catName = item.findViewById(R.id.name_Category_textView);
        editBtn = item.findViewById(R.id.edit_Category_imageView);
    }
}