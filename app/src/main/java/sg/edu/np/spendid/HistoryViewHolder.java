package sg.edu.np.spendid;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class HistoryViewHolder extends RecyclerView.ViewHolder {
    TextView date;
    RecyclerView rv;

    public HistoryViewHolder(View item) {
        super(item);
        date = item.findViewById(R.id.dateText);
        rv = item.findViewById(R.id.historyRV);
    }
}
