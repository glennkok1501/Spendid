package sg.edu.np.spendid.ExchangeRates;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.ExchangeRates.Adapters.ExchangeRatesAdapter;
import sg.edu.np.spendid.R;

public class ExchangeRateActivity extends AppCompatActivity {

    private String from, to, lastUpdated;
    private double exchangeRate;
    private RequestQueue mQueue;
    private final DecimalFormat df2 = new DecimalFormat("#0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_rate);

        initToolbar(); //set toolbar

        mQueue = Volley.newRequestQueue(this);
        CardView selFrom = findViewById(R.id.exchangeRateSelFrom_cardView);
        TextView fromTextView = findViewById(R.id.exchangeRateSelFrom_textView);
        setSelection(selFrom, fromTextView);
        CardView selTo = findViewById(R.id.exchangeRateSelTo_cardView);
        TextView toTextView = findViewById(R.id.exchangeRateSelTo_textView);
        setSelection(selTo, toTextView);
        FloatingActionButton convert_fab = findViewById(R.id.exchangeRateConvert_fab);

        //select conversion from to to
        convert_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                from = fromTextView.getText().toString();
                to = toTextView.getText().toString();
                //check if both currencies are selected before calling API
                if (!from.equals("Select") && !to.equals("Select")){
                    getExchangeRate();
                    Toast.makeText(getApplicationContext(), from+" to "+to, Toast.LENGTH_SHORT).show();
                }
            }
        });

        DBHandler dbHandler = new DBHandler(this, null, null, 1);
        RecyclerView recyclerView = findViewById(R.id.exchangeRateSgd_RV);
        ExchangeRatesAdapter myAdapter = new ExchangeRatesAdapter(dbHandler.getCurrencies(), getString(R.string.baseCurrency).toLowerCase());
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(myLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //prompt dialog to select currency
    private void setSelection(CardView c, TextView textView){
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectCountryDialog dialog = new SelectCountryDialog(ExchangeRateActivity.this, getResources().getStringArray(R.array.countries), textView);
                dialog.show();
            }
        });
    }

    //initiate field to input values for currency conversion
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
        //converts as input changes
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0) {
                    output.setText(df2.format(Double.parseDouble(input.getText().toString()) * exchangeRate));
                }
                else{
                    output.setText("0.00");
                }
            }
        });
    }

    //retrieve conversion data from API
    private void getExchangeRate(){
        String url = String.format("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/%s/%s.json", from.toLowerCase(), to.toLowerCase());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            exchangeRate = response.getDouble(to.toLowerCase()); //get rate value
                            lastUpdated = String.format("Last Updated: %s", response.getString("date")); //get last updated
                            initConvertText(); //start fields with data retrieved
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


    private void initToolbar(){
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
    }
}