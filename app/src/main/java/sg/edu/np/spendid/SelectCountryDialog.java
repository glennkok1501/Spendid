package sg.edu.np.spendid;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectCountryDialog{
    private String[] countries;
    private Dialog dialog;
    private TextView textView;
    private Context context;

    public SelectCountryDialog (Context context, String[] countries, TextView textView) {
        this.context = context;
        this.countries = countries;
        this.textView = textView;
        dialog = new Dialog(this.context);
        dialog.setContentView(R.layout.country_currency_dialog_layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(true);
    }

    public void show(){
        RecyclerView recyclerView = dialog.findViewById(R.id.selCurrency_RV);
        SelectCurrencyAdapter myAdapter = new SelectCurrencyAdapter(countries, dialog, textView);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(myLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);

        RelativeLayout layout = dialog.findViewById(R.id.selCurrency_relativeLayout);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dialog.dismiss();
                return false;
            }
        });

        dialog.show();
    }
}