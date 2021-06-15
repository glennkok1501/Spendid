//Created by Glenn

package sg.edu.np.spendid;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WalletSliderAdapter extends RecyclerView.Adapter<WalletSliderAdapter.WalletSliderViewHolder> {
    ArrayList<Wallet> data;
    private Context context;

    public WalletSliderAdapter(ArrayList<Wallet> input){
        data = input;
    }

    public WalletSliderViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_layout, parent, false);
        context = parent.getContext();
        WalletSliderViewHolder holder = new WalletSliderViewHolder(item);
        item.findViewById(R.id.viewpager_wallet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("TAG", "Wallet clicked - "+data.get(holder.getAdapterPosition()).getName());
            }
        });
        return holder;
    }

    public void onBindViewHolder(WalletSliderViewHolder holder, int position){
        DBHandler dbHandler = new DBHandler(context, null, null, 1);
        Wallet s = data.get(position);
        holder.name.setText(s.getName());
        holder.amount.setText(String.valueOf(Math.round(dbHandler.getWalletTotal(s.getWalletId()) * 100.0) / 100.0));
    }

    public int getItemCount(){
        return data.size();
    }

    public class WalletSliderViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView amount;
        public WalletSliderViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.viewpager_wallet_name);
            amount = itemView.findViewById(R.id.viewpager_wallet_amount);

        }
    }

}