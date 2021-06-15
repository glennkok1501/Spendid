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
        CurrentTransViewHolder holder = new CurrentTransViewHolder(item);
        item.findViewById(R.id.currentTrans).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("TAG", "current transaction clicked - "+keys.get(holder.getAdapterPosition()));
            }
        });
        return holder;
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
}
