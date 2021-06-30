package sg.edu.np.spendid;

import android.view.View;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CatSliderViewHolder extends RecyclerView.ViewHolder {
    ImageView image;
    CardView cardView;
    public CatSliderViewHolder(View itemView){
        super(itemView);
        image = itemView.findViewById(R.id.newRecordCat_imageView);
        cardView = itemView.findViewById(R.id.newRecordCat_bubble);
    }
}