package sg.edu.np.spendid;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class TransactionViewHolder extends RecyclerView.ViewHolder {
    TextView title, time, amt, cur;
    ImageView editBtn, cat;

    public TransactionViewHolder(View item) {
        super(item);
        title = item.findViewById(R.id.title_Record_textView);
        time = item.findViewById(R.id.time_Record_textView);
        amt = item.findViewById(R.id.amount_Record_textView);
        cur = item.findViewById(R.id.currency_Record_textView);
        editBtn = item.findViewById(R.id.edit_Record_imageView);
        cat = item.findViewById(R.id.cat_Record_imageView);
    }
}