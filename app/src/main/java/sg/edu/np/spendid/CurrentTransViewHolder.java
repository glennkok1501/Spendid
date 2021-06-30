package sg.edu.np.spendid;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CurrentTransViewHolder extends RecyclerView.ViewHolder {
    ImageView image;
    TextView cat, amt, currency;
    CardView cardView;
    public CurrentTransViewHolder(View itemView){
        super(itemView);
        image = itemView.findViewById(R.id.currentTrans_imageView);
        cat = itemView.findViewById(R.id.currentTransCat_textView);
        amt = itemView.findViewById(R.id.currentTransAmt_textView);
        currency = itemView.findViewById(R.id.currentTransCur_textView);
        cardView = itemView.findViewById(R.id.currentTrans);
    }
}