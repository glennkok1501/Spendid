package sg.edu.np.spendid;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class SelectCurrencyViewHolder extends RecyclerView.ViewHolder{
    TextView txt;
    ImageView img;
    ConstraintLayout layout;
    public SelectCurrencyViewHolder(View itemView){
        super(itemView);
        txt = itemView.findViewById(R.id.countryCurrencies);
        img = itemView.findViewById(R.id.countryCurrenciesFlag);
        layout = itemView.findViewById(R.id.countryCurrenciesLayout);
    }
}