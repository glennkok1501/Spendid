package sg.edu.np.spendid;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class SelectCurrencyAdapter extends RecyclerView.Adapter<SelectCurrencyViewHolder> {
    String[] data;
    private Dialog dialog;
    private TextView textView;
    private CountryFlagsHandler countryFlagsHandler = new CountryFlagsHandler();

    public SelectCurrencyAdapter(String[] input, Dialog dialog, TextView textView){
        data = input;
        this.dialog = dialog;
        this.textView = textView;
    }

    public SelectCurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_currency_layout, parent, false);
        return new SelectCurrencyViewHolder(item);
    }

    public void onBindViewHolder(SelectCurrencyViewHolder holder, int position){
        String[] split = data[position].split(";");
        //set TextView of chosen currency
        holder.txt.setText(split[0]);
        holder.img.setImageResource(countryFlagsHandler.setIcon(split[1]));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText((split[1]).toUpperCase());
                dialog.dismiss();
            }
        });
    }

    public int getItemCount(){
        return data.length;
    }
}
