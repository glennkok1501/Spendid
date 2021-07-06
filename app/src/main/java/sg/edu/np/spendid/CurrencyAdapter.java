package sg.edu.np.spendid;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class CurrencyAdapter extends RecyclerView.Adapter<SelectCurrencyViewHolder>{
    String[] data;
    private CountryFlagsHandler countryFlagsHandler = new CountryFlagsHandler();

    public CurrencyAdapter(String[] input){
        data = input;
    }

    public SelectCurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_currency_layout, parent, false);
        return new SelectCurrencyViewHolder(item);
    }
    public void onBindViewHolder(SelectCurrencyViewHolder holder, int position){
        String[] split = data[position].split(";");
        holder.txt.setText(split[0]);
        holder.img.setImageResource(countryFlagsHandler.setIcon(split[1]));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddWalletActivity.class);
                intent.putExtra("Currency", split[1]);
                v.getContext().startActivity(intent);
            }
        });
    }
    public int getItemCount(){
        return data.length;
    }

}