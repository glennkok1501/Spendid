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

public class CurrencyAPI {
    private Context context;
    private String base;
    private String foreign;
    private double exchangeRate;
    private String lastUpdate;
    private RequestQueue mQueue;
    private EditText amt;
    private double forFixedAmt;
    private final DecimalFormat df2 = new DecimalFormat("#0.00");

    public double getExchangeRate() {
        return exchangeRate;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setAmt(EditText amt) {
        this.amt = amt;
    }

    public void setForFixedAmt(double forFixedAmt) {
        this.forFixedAmt = forFixedAmt;
    }

    public CurrencyAPI(Context context, String foreign, String base) {
        this.context = context;
        this.base = base;
        this.foreign = foreign;
        this.mQueue = Volley.newRequestQueue(context);
    }

    public void call(boolean fixedAmt){
        String url = String.format("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/%s/%s.json", foreign, base);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            exchangeRate = response.getDouble(base.toLowerCase());
                            lastUpdate = response.getString("date");
                            currencyDialog(fixedAmt);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Data Unavailable", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Service Temporarily Unavailable", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    public void currencyDialog(boolean fixedAmt){
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

        baseCur.setText(base.toUpperCase());
        forCur.setText(foreign.toUpperCase());
        updateDate.setText("Last Updated: "+lastUpdate);
        if (fixedAmt){
            forAmt.setText(df2.format(forFixedAmt));
            baseAmt.setText(df2.format(Double.parseDouble(forAmt.getText().toString()) * exchangeRate));
        }

        forAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0) {
                    baseAmt.setText(df2.format(Double.parseDouble(forAmt.getText().toString()) * exchangeRate));
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
}
