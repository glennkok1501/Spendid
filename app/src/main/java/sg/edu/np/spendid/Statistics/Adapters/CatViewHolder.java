package sg.edu.np.spendid.Statistics.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.spendid.R;

public class CatViewHolder extends RecyclerView.ViewHolder {
    ImageView image;
    TextView catName;
    TextView percent;
    public CatViewHolder(View item){
        super(item);
        image = item.findViewById(R.id.stats_cat_imageView);
        catName = item.findViewById(R.id.stats_cat_title);
        percent = item.findViewById(R.id.stats_cat_percent);
    }
}