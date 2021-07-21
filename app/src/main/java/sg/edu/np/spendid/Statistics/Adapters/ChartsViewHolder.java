package sg.edu.np.spendid.Statistics.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.spendid.R;

public class ChartsViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    LinearLayout chart, months;
    ImageView image;
    public ChartsViewHolder(View itemView){
        super(itemView);
        title = itemView.findViewById(R.id.stats_graph_textView);
        chart = itemView.findViewById(R.id.stats_graph_linearLayout);
        months = itemView.findViewById(R.id.stats_monthsGraph_linearLayout);
        image = itemView.findViewById(R.id.stats_graph_imageView);
    }
}
