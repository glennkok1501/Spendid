//Created by Glenn

package sg.edu.np.spendid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class WalletSliderAdapter extends RecyclerView.Adapter<WalletSliderAdapter.WalletSliderViewHolder> {
    ArrayList<Wallet> data;
    String baseCurrency;
    Context context;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public WalletSliderAdapter(ArrayList<Wallet> input, String currency, Context getContext){
        data = input;
        baseCurrency = currency;
        context = getContext;
    }

    public WalletSliderViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_layout, parent, false);
        WalletSliderViewHolder holder = new WalletSliderViewHolder(item);

        ImageView front = item.findViewById(R.id.walletSliderFront_imageView);
        ImageView back = item.findViewById(R.id.walletSliderBack_imageView);

        if (viewType == 0){
            front.setColorFilter(ContextCompat.getColor(parent.getContext(), R.color.light_grey));
            back.setColorFilter(ContextCompat.getColor(parent.getContext(), android.R.color.transparent));
        }
        else if (viewType == data.size()-1){
            back.setColorFilter(ContextCompat.getColor(parent.getContext(), R.color.light_grey));
            front.setColorFilter(ContextCompat.getColor(parent.getContext(), android.R.color.transparent));
        }
        else{
            front.setColorFilter(ContextCompat.getColor(parent.getContext(), R.color.light_grey));
            back.setColorFilter(ContextCompat.getColor(parent.getContext(), R.color.light_grey));
        }

        item.findViewById(R.id.viewpager_wallet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog dialog = new CustomDialog(v.getContext());
                dialog.showManageWallet(data.get(viewType), false);
            }
        });
        return holder;
    }

    public void onBindViewHolder(WalletSliderViewHolder holder, int position){
        DBHandler dbHandler = new DBHandler(context, null, null, 1);
        Wallet s = data.get(position);
        holder.name.setText(s.getName());
        holder.amount.setText(df2.format(dbHandler.getWalletTotal(s.getWalletId())));
        holder.currency.setText(baseCurrency);
    }

    public int getItemCount(){
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class WalletSliderViewHolder extends RecyclerView.ViewHolder {
        TextView name, amount, currency;
        public WalletSliderViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.viewpager_wallet_name);
            amount = itemView.findViewById(R.id.viewpager_wallet_amount);
            currency = itemView.findViewById(R.id.viewpager_wallet_currency);

        }
    }
}