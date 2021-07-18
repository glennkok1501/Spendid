package sg.edu.np.spendid.Dashboard.Adapters.WalletSlider;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.spendid.R;

public class WalletSliderViewHolder extends RecyclerView.ViewHolder {
    TextView name, amount, currency;
    CardView cardView;
    public WalletSliderViewHolder(View itemView){
        super(itemView);
        name = itemView.findViewById(R.id.viewpager_wallet_name);
        amount = itemView.findViewById(R.id.viewpager_wallet_amount);
        currency = itemView.findViewById(R.id.viewpager_wallet_currency);
        cardView = itemView.findViewById(R.id.viewpager_wallet);

    }
}
