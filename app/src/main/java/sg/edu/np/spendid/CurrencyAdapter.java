package sg.edu.np.spendid;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyViewHolder>{
    String[] data;

    public CurrencyAdapter(String[] input){
        data = input;
    }
    public CurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_currency_layout, parent, false);
        return new CurrencyViewHolder(item);
    }
    public void onBindViewHolder(CurrencyViewHolder holder, int position){
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