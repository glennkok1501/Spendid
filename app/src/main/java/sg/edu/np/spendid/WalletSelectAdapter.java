//Created by Glenn

package sg.edu.np.spendid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class WalletSelectAdapter extends RecyclerView.Adapter<WalletSelectAdapter.WalletSelectViewHolder> {
    ArrayList<Wallet> data;
    private Context context;

    public WalletSelectAdapter(ArrayList<Wallet> input, Context getContext){
        data = input;
        context = getContext;
    }

    public WalletSelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_wallet_layout, parent, false);
        WalletSelectViewHolder holder = new WalletSelectViewHolder(item);
        item.findViewById(R.id.sel_wallet_cardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Wallet w = data.get(holder.getAdapterPosition());
                Intent intent = new Intent(v.getContext(), NewRecordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("walletId", w.getWalletId());
                bundle.putString("walletName", w.getName());
                bundle.putString("walletCurrency", w.getCurrency());
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
                CustomIntent.customType(v.getContext(), "bottom-to-up");
            }
        });
        return holder;
    }

    public void onBindViewHolder(WalletSelectViewHolder holder, int position){
        DBHandler dbHandler = new DBHandler(context, null, null, 1);
        Wallet s = data.get(position);
        holder.name.setText(s.getName());
        holder.amount.setText(String.valueOf(Math.round(dbHandler.getWalletTotal(s.getWalletId()) * 100.0) / 100.0));
        holder.date.setText("Last Updated: "+dbHandler.lastMadeTransaction(s.getWalletId()));
    }

    public int getItemCount(){
        return data.size();
    }

    public class WalletSelectViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView amount;
        TextView date;
        public WalletSelectViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.sel_walletName_textView);
            amount = itemView.findViewById(R.id.sel_walletAmt_textView);
            date = itemView.findViewById(R.id.sel_walletDate_textView);

        }
    }
}
