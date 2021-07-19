//Created by Glenn

package sg.edu.np.spendid.Wallets.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Models.Wallet;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Records.AddRecordActivity;

public class WalletSelectAdapter extends RecyclerView.Adapter<WalletSelectViewHolder> {
    ArrayList<Wallet> data;
    private DBHandler dbHandler;
    private DecimalFormat df2 = new DecimalFormat("#0.00");

    public WalletSelectAdapter(ArrayList<Wallet> input, DBHandler dbHandler){
        data = input;
        this.dbHandler = dbHandler;

    }

    public WalletSelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_wallet_layout, parent, false);
        return new WalletSelectViewHolder(item);
    }

    public void onBindViewHolder(WalletSelectViewHolder holder, int position){
        Wallet w = data.get(position);
        holder.name.setText(w.getName());
        holder.amount.setText(df2.format(dbHandler.getWalletTotal(w.getWalletId()))+" SGD");
        holder.date.setText("Date Created: "+data.get(position).getDateCreated());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddRecordActivity.class);
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
