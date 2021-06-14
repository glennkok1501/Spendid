package sg.edu.np.spendid;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class CurrentTransAdapter extends RecyclerView.Adapter<CurrentTransViewHolder> {
    HashMap<String, ArrayList<Record>> data;
    ArrayList<String> keys;
    ArrayList<ArrayList<Record>> values;

    public CurrentTransAdapter(HashMap<String, ArrayList<Record>> input){
        data = input;
        keys = new ArrayList<>(data.keySet());
        values = new ArrayList<>(data.values());
    }

    public CurrentTransViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_transaction_layout, parent, false);
        Log.v("TAG", ""+true);
        return new CurrentTransViewHolder(item);
    }

    public void onBindViewHolder(CurrentTransViewHolder holder, int position){
        String s = keys.get(position);
        setIcon(holder, s);
        holder.cat.setText(s);
        holder.amt.setText(String.valueOf(calAmt(values.get(position))));
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

    private void setIcon(CurrentTransViewHolder holder, String cat){
        switch (cat){
            case "Shopping":
                holder.image.setImageResource(R.drawable.ic_shopping_24);
                break;
            case "Food":
                holder.image.setImageResource(R.drawable.ic_food_24);
                break;
            case "Entertainment":
                holder.image.setImageResource(R.drawable.ic_entertainment_24);
                break;
            case "Others":
                holder.image.setImageResource(R.drawable.ic_others_24);
                break;
        }

    }
}
