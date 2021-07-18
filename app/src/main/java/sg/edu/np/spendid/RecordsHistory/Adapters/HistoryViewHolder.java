package sg.edu.np.spendid.RecordsHistory.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.spendid.R;

public class HistoryViewHolder extends RecyclerView.ViewHolder {
    TextView date;
    RecyclerView rv;
    public HistoryViewHolder(View item) {
        super(item);
        date = item.findViewById(R.id.history_date_textView);
        rv = item.findViewById(R.id.historyRV);
    }
}