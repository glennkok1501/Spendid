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
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public CurrentTransAdapter(HashMap<String, ArrayList<Record>> input, String currency){
        data = input;
        keys = new ArrayList<>(data.keySet());
        baseCurrency = currency;
    }

    public CurrentTransViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_transaction_layout, parent, false);
        CurrentTransViewHolder holder = new CurrentTransViewHolder(item);
        item.findViewById(R.id.currentTrans).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cat = keys.get(viewType);
                CustomDialog customDialog = new CustomDialog(v.getContext());
                customDialog.showCurrentTrans(baseCurrency, cat, data.get(cat));
            }
        });
        return holder;
    }

    public void onBindViewHolder(CurrentTransViewHolder holder, int position){
        String s = keys.get(position);
        holder.image.setImageResource(categoryHandler.setIcon(s));
        holder.cat.setText(s);
        holder.amt.setText(df2.format(calAmt(data.get(s))));
        holder.currency.setText(baseCurrency);
    }

    public int getItemCount(){
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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
        public CurrentTransViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.currentTrans_imageView);
            cat = itemView.findViewById(R.id.currentTransCat_textView);
            amt = itemView.findViewById(R.id.currentTransAmt_textView);
            currency = itemView.findViewById(R.id.currentTransCur_textView);
        }
    }
}
