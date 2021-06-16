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
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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

public class WalletManageAdapter extends RecyclerView.Adapter<WalletManageViewHolder> {
    ArrayList<Wallet> data;
    int firstWallet;
    ArrayList<View> itemViewList;
    private final static String PREF_NAME = "sharedPrefs";
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public WalletManageAdapter(ArrayList<Wallet> input, int fav){
        data = input;
        firstWallet = fav;
        itemViewList = new ArrayList<View>();
    }

    public WalletManageViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_wallet_layout, parent, false);
        WalletManageViewHolder holder = new WalletManageViewHolder(item);
        itemViewList.add(item);
        int id = data.get(viewType).getWalletId();
        ImageView star = item.findViewById(R.id.manageWalletFav_imageView);
        if (id == firstWallet){
            star.setColorFilter(ContextCompat.getColor(parent.getContext(), R.color.fire_bush));
        }
        else{
            star.setColorFilter(ContextCompat.getColor(parent.getContext(), R.color.light_grey));
        }

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walletDialog(v.getContext(), data.get(viewType));
            }
        });

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(View i : itemViewList) {
                    ImageView track = i.findViewById(R.id.manageWalletFav_imageView);
                    if (itemViewList.get(viewType) == i) {
                        track.setColorFilter(ContextCompat.getColor(parent.getContext(), R.color.fire_bush));
                        SharedPreferences.Editor editor = parent.getContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
                        editor.putInt("firstWallet", id);
                        editor.apply();
                    } else {
                        track.setColorFilter(ContextCompat.getColor(parent.getContext(), R.color.light_grey));
                    }
                }
            }
        });
        return holder;
    }

    public void onBindViewHolder(WalletManageViewHolder holder, int position){
        Wallet s = data.get(position);
        holder.name.setText(s.getName());
        holder.currency.setText(s.getCurrency().toUpperCase());
    }

    public int getItemCount(){
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void walletDialog(Context context, Wallet w){
        DBHandler dbHandler = new DBHandler(context, null, null, 1);
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.manage_view_wallet_layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(true);

        TextView name = dialog.findViewById(R.id.viewWalletTitle_textView);
        TextView amt = dialog.findViewById(R.id.viewWalletAmt_textView);
        TextView cur = dialog.findViewById(R.id.viewWalletCur_textView);
        TextView date = dialog.findViewById(R.id.viewWalletDate_textView);
        TextView total = dialog.findViewById(R.id.viewWalletTrans_textView);
        TextView des = dialog.findViewById(R.id.viewWalletDes_textView);
        FloatingActionButton editBtn = dialog.findViewById(R.id.viewWalletEdit_fab);

        name.setText(w.getName());
        amt.setText(df2.format(dbHandler.getWalletTotal(w.getWalletId())));
        cur.setText(w.getCurrency().toUpperCase());
        date.setText("Date Created: "+w.getDateCreated());
        total.setText("Total Records: ");
        des.setText(w.getDescription());

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("TAG", "Edit Activity");
            }
        });
        dialog.show();
    }
}
