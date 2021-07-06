package sg.edu.np.spendid;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ExchangeRatesViewHolder extends RecyclerView.ViewHolder {
    TextView foreign, rate;
    public ExchangeRatesViewHolder(View itemView){
        super(itemView);
        foreign = itemView.findViewById(R.id.exchangeRate_foreign_textView);
        rate = itemView.findViewById(R.id.exchangeRate_rate_textView);
    }
}
