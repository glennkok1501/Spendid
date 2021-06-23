package sg.edu.np.spendid;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class SelectCurrencyAdapter extends RecyclerView.Adapter<CurrencyViewHolder> {
    String[] data;
    private Dialog dialog;
    private TextView textView;

    public SelectCurrencyAdapter(String[] input, Dialog dialog, TextView textView){
        data = input;
        this.dialog = dialog;
        this.textView = textView;
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
                textView.setText((s[1]).toUpperCase());
                dialog.dismiss();
            }
        });
    }

    public int getItemCount(){
        return data.length;
    }
}
