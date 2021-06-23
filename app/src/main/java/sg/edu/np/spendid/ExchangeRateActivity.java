package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class ExchangeRateActivity extends AppCompatActivity {

    private String from, to, lastUpdated;
    private CustomDialog.SelectCountry selectCountry;
    private double exchangeRate;
    private RequestQueue mQueue;
    private final DecimalFormat df2 = new DecimalFormat("#0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        toggleNightMode();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_rate);

        //Tool Bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        activityTitle.setText("Currency Rates");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mQueue = Volley.newRequestQueue(this);
        CardView selFrom = findViewById(R.id.exchangeRateSelFrom_cardView);
        TextView fromTextView = findViewById(R.id.exchangeRateSelFrom_textView);
        setSelection(selFrom, fromTextView);
        CardView selTo = findViewById(R.id.exchangeRateSelTo_cardView);
        TextView toTextView = findViewById(R.id.exchangeRateSelTo_textView);
        setSelection(selTo, toTextView);
        FloatingActionButton convert_fab = findViewById(R.id.exchangeRateConvert_fab);
        convert_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                from = fromTextView.getText().toString();
                to = toTextView.getText().toString();
                if (!from.equals("Select") && !to.equals("Select")){
                    getExchangeRate();
                }
                Toast.makeText(getApplicationContext(), from+" to "+to, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setSelection(CardView c, TextView t){
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCountry = new CustomDialog(ExchangeRateActivity.this).new SelectCountry(getResources().getStringArray(R.array.countries), t);
                selectCountry.show();
            }
        });
    }

    private void initConvertText(){
        EditText input = findViewById(R.id.exchangeRateAmtFrom_editText);
        TextView output = findViewById(R.id.exchangeRateAmtTo_textView);
        TextView amtCurFrom = findViewById(R.id.exchangeRateCurFrom_textView);
        TextView amtCurTo = findViewById(R.id.exchangeRateCurTo_textView);
        TextView date = findViewById(R.id.exchangeRateDate_textView);

        amtCurFrom.setText(from);
        amtCurTo.setText(to);
        input.setText("1.00");
        output.setText(df2.format(1 * exchangeRate));
        date.setText(lastUpdated);
        input.addTextChangedListener(new TextWatcher() {
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
                    output.setText(df2.format(Double.parseDouble(input.getText().toString()) * exchangeRate));
                }
                else{
                    output.setText("0.00");
                }
            }
        });
    }

    private void getExchangeRate(){
        String url = String.format("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/%s/%s.json", from.toLowerCase(), to.toLowerCase());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            exchangeRate = response.getDouble(to.toLowerCase());
                            lastUpdated = String.format("Last Updated: %s", response.getString("date"));
                            initConvertText();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ExchangeRateActivity.this, "Data Unavailable", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ExchangeRateActivity.this, "Service Temporarily Unavailable", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private void toggleNightMode(){
        if (getSharedPreferences("sharedPrefs", MODE_PRIVATE).getBoolean("nightMode", false)){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}