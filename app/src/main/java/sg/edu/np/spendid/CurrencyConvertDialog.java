package sg.edu.np.spendid;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class CurrencyConvertDialog {
    private Context context;
    private EditText amt;
    private double forFixedAmt;
    private Currency currency;
    private DBHandler dbHandler;
    private String foreign;
    private final DecimalFormat df2 = new DecimalFormat("#0.00");

    public void setAmt(EditText amt) {
        this.amt = amt;
    }

    public void setForFixedAmt(double forFixedAmt) {
        this.forFixedAmt = forFixedAmt;
    }

    public CurrencyConvertDialog(Context context, String foreign) {
        this.context = context;
        forFixedAmt = 0;
        this.foreign = foreign;
    }

    public void show(){
        dbHandler = new DBHandler(context, null,null,1);
        currency = dbHandler.getCurrency(foreign);
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.currency_exchange_layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);

        TextView forCur = dialog.findViewById(R.id.foreignAmt_textView);
        TextView baseCur = dialog.findViewById(R.id.baseCurrency_textView);
        TextView baseAmt = dialog.findViewById(R.id.baseCurrencyAmt_textView);
        TextView updateDate = dialog.findViewById(R.id.rateLastUpdate);
        EditText forAmt = dialog.findViewById(R.id.foreignAmt_editText);
        FloatingActionButton convertBtn = dialog.findViewById(R.id.convertCurrrency_fab);
        TextView cancelDialog = dialog.findViewById(R.id.currencyExchangeCancel_textView);

        baseCur.setText("SGD");
        forCur.setText(foreign.toUpperCase());
        updateDate.setText(currency.getDate());
        if (forFixedAmt > 0){
            forAmt.setText(df2.format(forFixedAmt));
            baseAmt.setText(df2.format(convert(Double.parseDouble(forAmt.getText().toString()))));
        }

        forAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0) {
                    baseAmt.setText(df2.format(convert(Double.parseDouble(forAmt.getText().toString()))));
                }
                else{
                    baseAmt.setText("0.00");
                }
            }
        });

        convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amt.setText(baseAmt.getText());
                dialog.dismiss();
            }
        });

        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private double convert(double amt){
        return amt / currency.getRate();
    }
}
