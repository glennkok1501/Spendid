//Created by Glenn

package sg.edu.np.spendid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class CurrentTransAdapter extends RecyclerView.Adapter<CurrentTransViewHolder> {
    HashMap<String, ArrayList<Record>> data;
    private ArrayList<String> keys;
    private CategoryHandler categoryHandler = new CategoryHandler();
    private DecimalFormat df2 = new DecimalFormat("#0.00");

    public CurrentTransAdapter(HashMap<String, ArrayList<Record>> input){
        data = input;
        keys = new ArrayList<>(data.keySet());
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
        holder.currency.setText(holder.itemView.getContext().getString(R.string.baseCurrency));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cat = keys.get(position);
                CurrentTransDialog dialog = new CurrentTransDialog(v.getContext(), cat, records);
                dialog.show();
            }
        });
    }

    public int getItemCount(){
        return data.size();
    }

    //calculate total amount in for category
    private double calAmt(ArrayList<Record> r){
        double amt = 0;
        for (int i = 0; i < r.size(); i++){
            amt += r.get(i).getAmount();
        }
        return amt;
    }
}
