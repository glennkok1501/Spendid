package sg.edu.np.spendid.RecurringEntry.Adapters;

import android.view.View;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.spendid.R;

public class RecurCatSliderViewHolder extends RecyclerView.ViewHolder{
    ImageView image;
    CardView cardView;
    public RecurCatSliderViewHolder(View itemView){
        super(itemView);
        image = itemView.findViewById(R.id.newRecurringCat_imageView);
        cardView = itemView.findViewById(R.id.newRecurringCat_bubble);
    }
}