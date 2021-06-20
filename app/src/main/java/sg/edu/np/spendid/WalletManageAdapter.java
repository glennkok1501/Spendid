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
    ArrayList<View> itemViewList;
    Context context;
    String baseCurrency;
    private DecimalFormat df2 = new DecimalFormat("#0.00");

    public WalletManageAdapter(ArrayList<Wallet> input, String currency, Context getContext){
        data = input;
        baseCurrency = currency;
        context = getContext;
        itemViewList = new ArrayList<View>();
    }

    public WalletSelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_wallet_layout, parent, false);
        WalletSelectViewHolder holder = new WalletSelectViewHolder(item);

        item.findViewById(R.id.sel_wallet_cardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Wallet w = data.get(viewType);
                CustomDialog dialog = new CustomDialog(v.getContext());
                dialog.showManageWallet(w, true);
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
