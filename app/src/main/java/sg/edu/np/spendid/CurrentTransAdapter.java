//Created by Glenn

package sg.edu.np.spendid;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class CurrentTransAdapter extends RecyclerView.Adapter<CurrentTransAdapter.CurrentTransViewHolder> {
    HashMap<String, ArrayList<Record>> data;
    ArrayList<String> keys;
    String baseCurrency;
    CategoryHandler categoryHandler = new CategoryHandler();
    DecimalFormat df2 = new DecimalFormat("#0.00");

    public CurrentTransAdapter(HashMap<String, ArrayList<Record>> input, String baseCurrency){
        data = input;
        keys = new ArrayList<>(data.keySet());
        this.baseCurrency = baseCurrency;
    }

    public CurrentTransViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_transaction_layout, parent, false);
        return new CurrentTransViewHolder(item);
    }

    public void onBindViewHolder(CurrentTransViewHolder holder, int position){
        String cat = keys.get(position);
        ArrayList<Record> records = data.get(cat);
        holder.image.setImageResource(categoryHandler.setIcon(cat));
        holder.cat.setText(cat);
        holder.amt.setText(df2.format(calAmt(records)));
        holder.currency.setText(baseCurrency);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cat = keys.get(position);
                CustomDialog customDialog = new CustomDialog(v.getContext());
                customDialog.showCurrentTrans(baseCurrency, cat, records);
            }
        });
    }

    public int getItemCount(){
        return data.size();
    }

    private double calAmt(ArrayList<Record> r){
        double amt = 0;
        for (int i = 0; i < r.size(); i++){
            amt += r.get(i).getAmount();
        }
        return amt;
    }
    public class CurrentTransViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView cat, amt, currency;
        CardView cardView;
        public CurrentTransViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.currentTrans_imageView);
            cat = itemView.findViewById(R.id.currentTransCat_textView);
            amt = itemView.findViewById(R.id.currentTransAmt_textView);
            currency = itemView.findViewById(R.id.currentTransCur_textView);
            cardView = itemView.findViewById(R.id.currentTrans);
        }
    }
}
