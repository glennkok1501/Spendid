package sg.edu.np.spendid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WalletSliderAdapter extends RecyclerView.Adapter<WalletViewHolder> {
    ArrayList<Wallet> data;
    private Context context;

    public WalletSliderAdapter(ArrayList<Wallet> input){
        data = input;
    }

    public WalletViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_layout, parent, false);
        context = parent.getContext();
        return new WalletViewHolder(item);
    }

    public void onBindViewHolder(WalletViewHolder holder, int position){
        DBHandler dbHandler = new DBHandler(context, null, null, 1);
        Wallet s = data.get(position);
        holder.name.setText(s.getName());
        holder.amount.setText(String.valueOf(Math.round(dbHandler.getWalletTotal(s.getWalletId()) * 100.0) / 100.0));
    }

    public int getItemCount(){
        return data.size();
    }

}