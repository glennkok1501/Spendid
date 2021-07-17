package sg.edu.np.spendid.Wallets.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Models.Wallet;
import sg.edu.np.spendid.Wallets.ManageWalletDialog;
import sg.edu.np.spendid.R;

public class WalletManageAdapter extends RecyclerView.Adapter<WalletSelectViewHolder> {
    ArrayList<Wallet> data;
    private Context context;
    private DecimalFormat df2 = new DecimalFormat("#0.00");

    public WalletManageAdapter(ArrayList<Wallet> input, Context context){
        data = input;
        this.context = context;
    }

    public WalletSelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_wallet_layout, parent, false);
        return new WalletSelectViewHolder(item);
    }

    public void onBindViewHolder(WalletSelectViewHolder holder, int position){
        DBHandler dbHandler = new DBHandler(context, null, null, 1);
        String baseCurrency = context.getString(R.string.baseCurrency);
        Wallet wallet = data.get(position);
        holder.name.setText(wallet.getName());
        holder.amount.setText(df2.format(dbHandler.getWalletTotal(wallet.getWalletId()))+" "+baseCurrency);
        holder.date.setText("Date Created: "+data.get(position).getDateCreated());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManageWalletDialog dialog = new ManageWalletDialog(v.getContext(), wallet, true);
                dialog.show();
            }
        });
    }

    public int getItemCount(){
        return data.size();
    }


}