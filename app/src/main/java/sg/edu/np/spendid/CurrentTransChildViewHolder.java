package sg.edu.np.spendid;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CurrentTransChildViewHolder extends RecyclerView.ViewHolder {
    TextView name, time, amt, currency;
    LinearLayout layout;
    public CurrentTransChildViewHolder(View itemView){
        super(itemView);
        name = itemView.findViewById(R.id.curTransChildName_textView);
        time = itemView.findViewById(R.id.curTransChildTime_textView);
        amt = itemView.findViewById(R.id.curTransChildAmt_textView);
        currency = itemView.findViewById(R.id.curTransChildCur_textView);
        layout = itemView.findViewById(R.id.curTransChild_linearLayout);
    }
}