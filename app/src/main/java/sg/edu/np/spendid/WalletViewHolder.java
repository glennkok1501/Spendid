package sg.edu.np.spendid;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class WalletViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView amount;
    public WalletViewHolder(View itemView){
        super(itemView);
        name = itemView.findViewById(R.id.viewpager_wallet_name);
        amount = itemView.findViewById(R.id.viewpager_wallet_amount);

    }
}
