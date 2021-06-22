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
    DecimalFormat df2 = new DecimalFormat("#0.00");

    public WalletSelectAdapter(ArrayList<Wallet> input, String baseCurrency, Context context){
        data = input;
        this.baseCurrency = baseCurrency;
        this.context = context;
    }

    public WalletSelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_wallet_layout, parent, false);
        return new WalletSelectViewHolder(item);
    }

    public void onBindViewHolder(WalletSelectViewHolder holder, int position){
        DBHandler dbHandler = new DBHandler(context, null, null, 1);
        Wallet w = data.get(position);
        holder.name.setText(w.getName());
        holder.amount.setText(df2.format(dbHandler.getWalletTotal(w.getWalletId()))+" "+baseCurrency);
        String lastUpdated = dbHandler.lastMadeTransaction(w.getWalletId());
        if (lastUpdated == null){ holder.date.setText("No Transactions"); }
        else{ holder.date.setText("Last Updated: "+lastUpdated); }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewRecordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("walletId", w.getWalletId());
                bundle.putString("walletName", w.getName());
                bundle.putString("walletCurrency", w.getCurrency());
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    public int getItemCount(){
        return data.size();
    }

}
