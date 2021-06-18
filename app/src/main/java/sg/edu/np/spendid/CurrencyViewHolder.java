package sg.edu.np.spendid;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CurrencyViewHolder extends RecyclerView.ViewHolder{
    TextView txt;
    public CurrencyViewHolder(View itemView){
        super(itemView);
        txt=itemView.findViewById(R.id.countryCurrencies);
    }
    public TextView getTextView(){
        return txt;
    }
}