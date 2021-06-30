package sg.edu.np.spendid;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class SelectCurrencyViewHolder extends RecyclerView.ViewHolder{
    TextView txt;
    public SelectCurrencyViewHolder(View itemView){
        super(itemView);
        txt=itemView.findViewById(R.id.countryCurrencies);
    }
}