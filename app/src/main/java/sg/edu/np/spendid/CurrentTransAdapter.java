//Created by Glenn

package sg.edu.np.spendid;

import android.app.Dialog;
import android.content.Context;
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
        setIcon(holder, s);
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

    private void setIcon(CurrentTransViewHolder holder, String cat){
        switch (cat) {
            case "Shopping":
                holder.image.setImageResource(R.drawable.ic_shopping_24);
                break;
            case "Food":
                holder.image.setImageResource(R.drawable.ic_food_24);
                break;
            case "Entertainment":
                holder.image.setImageResource(R.drawable.ic_entertainment_24);
                break;
            case "Leisure":
                holder.image.setImageResource(R.drawable.ic_leisure_24);
                break;
            case "Transport":
                holder.image.setImageResource(R.drawable.ic_transport_24);
                break;
            case "Housing":
                holder.image.setImageResource(R.drawable.ic_housing_24);
                break;
            case "Vehicle":
                holder.image.setImageResource(R.drawable.ic_vehicle_24);
                break;
            case "Income":
                holder.image.setImageResource(R.drawable.ic_income_24);
                break;
            case "Salary":
                holder.image.setImageResource(R.drawable.ic_salary_24);
                break;
            case "Others":
                holder.image.setImageResource(R.drawable.ic_others_24);
                break;
        }
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
