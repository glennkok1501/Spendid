package sg.edu.np.spendid.Statistics.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.spendid.R;

public class CatProgressViewHolder extends RecyclerView.ViewHolder {
    ImageView image;
    TextView catName, percent;
    ProgressBar bar;
    ConstraintLayout layout;
    public CatProgressViewHolder(View item){
        super(item);
        image = item.findViewById(R.id.stats_catProgress_imageView);
        catName = item.findViewById(R.id.stats_catProgressTitle_textView);
        percent = item.findViewById(R.id.stats_catProgressPercent_textView);
        bar = item.findViewById(R.id.catProgressBar);
        layout = item.findViewById(R.id.stats_catProgress_layout);
    }
}