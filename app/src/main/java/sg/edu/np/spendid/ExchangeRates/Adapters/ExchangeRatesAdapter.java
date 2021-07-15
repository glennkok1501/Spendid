package sg.edu.np.spendid.ExchangeRates.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.spendid.Models.Currency;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Utils.CountryFlagsHandler;

public class ExchangeRatesAdapter extends RecyclerView.Adapter<ExchangeRatesViewHolder> {
    ArrayList<Currency> data;
    private CountryFlagsHandler countryFlagsHandler = new CountryFlagsHandler();

    public ExchangeRatesAdapter(ArrayList<Currency> input, String baseCurrency){
        data = input;
        baseCurrencyTop(baseCurrency);
    }

    public ExchangeRatesViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.exchange_rate_sgd_layout, parent, false);
        return new ExchangeRatesViewHolder(item);
    }

    public void onBindViewHolder(ExchangeRatesViewHolder holder, int position){
        Currency currency = data.get(position);
        holder.foreign.setText(currency.getForeign().toUpperCase());
        holder.rate.setText(String.valueOf(currency.getRate()));
        holder.flag.setImageResource(countryFlagsHandler.setIcon(currency.getForeign()));
    }

    public int getItemCount(){
        return data.size();
    }

    //insert base currency to top of data
    private void baseCurrencyTop(String baseCurrency){
        int index = -1;
        Currency currency = null;
        //iterate and locate index of base currency by currency code
        for (int i = 0; i < data.size(); i++){
            if (data.get(i).getForeign().equals(baseCurrency)){
                index = i;
                currency = data.get(i);
                break;
            }
        }
        //remove and insert to first if found
        if (index > 0 && currency != null){
            data.remove(index);
            data.add(0, currency);
        }
    }
}
