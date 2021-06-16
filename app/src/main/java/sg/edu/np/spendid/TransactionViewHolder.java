package sg.edu.np.spendid;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class TransactionViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    TextView time;
    TextView amt;
    ImageView editBtn;

    public TransactionViewHolder(View item) {
        super(item);
        title = item.findViewById(R.id.title_Record_textView);
        time = item.findViewById(R.id.time_Record_textView);
        amt = item.findViewById(R.id.amount_Record_textView);
        editBtn = item.findViewById(R.id.edit_Record_imageView);
    }
}