//Created by Glenn

package sg.edu.np.spendid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class WalletSelectAdapter extends RecyclerView.Adapter<WalletSelectViewHolder> {
    ArrayList<Wallet> data;
    String baseCurrency;
    Context context;
    private DecimalFormat df2 = new DecimalFormat("#.00");

    public WalletSelectAdapter(ArrayList<Wallet> input, String currency, Context getContext){
        data = input;
        baseCurrency = currency;
        context = getContext;
    }

    public WalletSelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_wallet_layout, parent, false);
        WalletSelectViewHolder holder = new WalletSelectViewHolder(item);
        item.findViewById(R.id.sel_wallet_cardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Wallet w = data.get(viewType);
                Intent intent = new Intent(v.getContext(), NewRecordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("walletId", w.getWalletId());
                bundle.putString("walletName", w.getName());
                bundle.putString("walletCurrency", w.getCurrency());
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
//                CustomIntent.customType(v.getContext(), "bottom-to-up");
            }
        });
        return holder;
    }

    public void onBindViewHolder(WalletSelectViewHolder holder, int position){
        DBHandler dbHandler = new DBHandler(context, null, null, 1);
        Wallet s = data.get(position);
        holder.name.setText(s.getName());
        holder.amount.setText(df2.format(dbHandler.getWalletTotal(s.getWalletId()))+" "+baseCurrency);
        String lastUpdated = dbHandler.lastMadeTransaction(s.getWalletId());
        if (lastUpdated == null){
            holder.date.setText("No Transactions");
        }
        else{
            holder.date.setText("Last Updated: "+lastUpdated);
        }

    }

    public int getItemCount(){
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
