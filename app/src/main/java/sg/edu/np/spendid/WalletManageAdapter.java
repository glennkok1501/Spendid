package sg.edu.np.spendid;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

import static android.content.Context.MODE_PRIVATE;

public class WalletManageAdapter extends RecyclerView.Adapter<WalletSelectViewHolder> {
    ArrayList<Wallet> data;
    ArrayList<View> itemViewList;
    Context context;
    String baseCurrency;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

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
                ManageWalletDialog.walletDialog(v.getContext(), w, true);
            }
        });
        return holder;
    }

    public void onBindViewHolder(WalletSelectViewHolder holder, int position){
        DBHandler dbHandler = new DBHandler(context, null, null, 1);
        Wallet s = data.get(position);
        holder.name.setText(s.getName());
        holder.amount.setText(df2.format(dbHandler.getWalletTotal(s.getWalletId()))+" "+baseCurrency);
        holder.date.setText("Last Updated: "+dbHandler.lastMadeTransaction(s.getWalletId()));
    }

    public int getItemCount(){
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}
