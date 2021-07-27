//Created by Glenn

package sg.edu.np.spendid.Dashboard.Adapters.WalletSlider;

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

public class WalletSliderAdapter extends RecyclerView.Adapter<WalletSliderViewHolder> {
    ArrayList<Wallet> data;
    private DecimalFormat df2 = new DecimalFormat("#0.00");
    private DBHandler dbHandler;


    public WalletSliderAdapter(ArrayList<Wallet> input, DBHandler dbHandler){
        data = input;
        this.dbHandler = dbHandler;
    }

    public WalletSliderViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_layout, parent, false);
        return new WalletSliderViewHolder(item);
    }

    public void onBindViewHolder(WalletSliderViewHolder holder, int position){
        Wallet wallet = data.get(position);
        holder.name.setText(wallet.getName());

        //get total amount for wallet
        String totalAmt = df2.format(dbHandler.getWalletTotal(wallet.getWalletId()));
        holder.amount.setText(totalAmt);
        holder.currency.setText("SGD");

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManageWalletDialog dialog = new ManageWalletDialog(v.getContext(), wallet, false, totalAmt);
                dialog.show();
            }
        });
    }

    public int getItemCount(){
        return data.size();
    }

}