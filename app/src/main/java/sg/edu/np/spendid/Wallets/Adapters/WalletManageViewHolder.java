package sg.edu.np.spendid.Wallets.Adapters;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.spendid.R;

public class WalletManageViewHolder extends RecyclerView.ViewHolder {
    TextView name, amount, date;
    CardView cardView;
    public WalletManageViewHolder(View itemView){
        super(itemView);
        name = itemView.findViewById(R.id.sel_walletName_textView);
        amount = itemView.findViewById(R.id.sel_walletAmt_textView);
        date = itemView.findViewById(R.id.sel_walletDate_textView);
        cardView = itemView.findViewById(R.id.sel_wallet_cardView);
    }
}