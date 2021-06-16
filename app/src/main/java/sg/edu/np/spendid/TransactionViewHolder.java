package sg.edu.np.spendid;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class TransactionViewHolder extends RecyclerView.ViewHolder {
    TextView action;
    TextView dateTime;
    TextView currencyType;
    TextView amt;

    public TransactionViewHolder(View item) {
        super(item);
        action = item.findViewById(R.id.actionText);
        dateTime = item.findViewById(R.id.dateTimeText);
        currencyType = item.findViewById(R.id.currencyText);
        amt = item.findViewById(R.id.amountText);
    }
}