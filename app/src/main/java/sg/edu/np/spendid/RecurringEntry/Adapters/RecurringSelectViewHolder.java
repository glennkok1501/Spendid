package sg.edu.np.spendid.RecurringEntry.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.spendid.R;

public class RecurringSelectViewHolder extends RecyclerView.ViewHolder {
    TextView name, amount, des, status;
    ImageView cat;

    public RecurringSelectViewHolder(View itemView){
        super(itemView);
        name = itemView.findViewById(R.id.sel_RecurringName_textView);
        des = itemView.findViewById(R.id.sel_recurringDes_textView);
        amount = itemView.findViewById(R.id.sel_recurringAmt_textView);
        status = itemView.findViewById(R.id.sel_recurringStatus_textView);
        cat = itemView.findViewById(R.id.sel_recurringCat_imageView);
    }
}