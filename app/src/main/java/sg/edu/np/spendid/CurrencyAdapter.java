package sg.edu.np.spendid;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class CurrencyAdapter extends RecyclerView.Adapter<SelectCurrencyViewHolder>{
    String[] data;

    public CurrencyAdapter(String[] input){
        data = input;
    }

    public SelectCurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_currency_layout, parent, false);
        return new SelectCurrencyViewHolder(item);
    }
    public void onBindViewHolder(SelectCurrencyViewHolder holder, int position){
        String[] s = data[position].split(";");
        holder.txt.setText(s[0]);
        holder.txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddWalletActivity.class);
                intent.putExtra("Currency", s[1]);
                v.getContext().startActivity(intent);
            }
        });
    }
    public int getItemCount(){
        return data.length;
    }

}