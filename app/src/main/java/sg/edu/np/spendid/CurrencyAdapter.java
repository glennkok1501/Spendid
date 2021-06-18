package sg.edu.np.spendid;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Currency;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyViewHolder>{
    String[] data;

    public CurrencyAdapter(String[] input){
        data = input;
    }
    public CurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_currency_adapter, parent, false);
        item.findViewById(R.id.countryCurrencies).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddWalletActivity.class);
                intent.putExtra("Currency", data[viewType].split(";")[1]);
                v.getContext().startActivity(intent);
            }
        });

        return new CurrencyViewHolder(item);
    }
    public void onBindViewHolder(CurrencyViewHolder holder, int position){
        holder.getTextView().setText(data[position].split(";")[0]);
    }
    public int getItemCount(){
        return data.length;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
}