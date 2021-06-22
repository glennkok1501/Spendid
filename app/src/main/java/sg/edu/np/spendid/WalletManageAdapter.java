package sg.edu.np.spendid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class WalletManageAdapter extends RecyclerView.Adapter<WalletSelectViewHolder> {
    ArrayList<Wallet> data;
    Context context;
    String baseCurrency;
    DecimalFormat df2 = new DecimalFormat("#0.00");

    public WalletManageAdapter(ArrayList<Wallet> input, String baseCurrency, Context context){
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
                CustomDialog dialog = new CustomDialog(v.getContext());
                dialog.showManageWallet(w, true);
            }
        });
    }

    public int getItemCount(){
        return data.size();
    }


}
