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
        holder.image.setImageResource(R.drawable.ic_shopping_24);
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
}
