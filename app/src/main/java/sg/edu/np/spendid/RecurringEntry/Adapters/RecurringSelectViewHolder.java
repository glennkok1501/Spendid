package sg.edu.np.spendid.RecurringEntry.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.spendid.R;

public class RecurringSelectViewHolder extends RecyclerView.ViewHolder {
    TextView name, amount, date, frequency;
    CardView cardView;

    public RecurringSelectViewHolder(View itemView){
        super(itemView);
        name = itemView.findViewById(R.id.sel_RecurringName_textView);
        amount = itemView.findViewById(R.id.sel_recurringAmt_textView);
        date = itemView.findViewById(R.id.sel_recurringDate_textView);
        frequency = itemView.findViewById(R.id.sel_recurringFrequency_textView);
        cardView = itemView.findViewById(R.id.sel_recurring_cardView);
    }
}
