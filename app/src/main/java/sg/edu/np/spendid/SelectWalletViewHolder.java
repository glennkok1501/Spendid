package sg.edu.np.spendid;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class SelectWalletViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView amount;
    TextView date;
    public SelectWalletViewHolder(View itemView){
        super(itemView);
        name = itemView.findViewById(R.id.sel_walletName_textView);
        amount = itemView.findViewById(R.id.sel_walletAmt_textView);
        date = itemView.findViewById(R.id.sel_walletDate_textView);

    }
}
