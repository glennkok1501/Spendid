//Created by Glenn

package sg.edu.np.spendid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class WalletSliderAdapter extends RecyclerView.Adapter<WalletSliderAdapter.WalletSliderViewHolder> {
    ArrayList<Wallet> data;
    String baseCurrency;
    Context context;
    DecimalFormat df2 = new DecimalFormat("#0.00");

    public WalletSliderAdapter(ArrayList<Wallet> input, String baseCurrency, Context context){
        data = input;
        this.baseCurrency = baseCurrency;
        this.context = context;
    }

    public WalletSliderViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_layout, parent, false);
        return new WalletSliderViewHolder(item);
    }

    public void onBindViewHolder(WalletSliderViewHolder holder, int position){
        DBHandler dbHandler = new DBHandler(context, null, null, 1);
        Wallet w = data.get(position);
        holder.name.setText(w.getName());
        holder.amount.setText(df2.format(dbHandler.getWalletTotal(w.getWalletId())));
        holder.currency.setText(baseCurrency);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog dialog = new CustomDialog(v.getContext());
                dialog.showManageWallet(w, false);
            }
        });
    }

    public int getItemCount(){
        return data.size();
    }


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
}