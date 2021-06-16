//Created by Glenn

package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class NewRecordActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private TextView selectedCat, selectWallet;
    private EditText title, description, amt;
    private FloatingActionButton fab;
    private TextInputLayout title_layout;
    private HashMap<String, Boolean> checkValues;
    private String baseCurrency, walletCurrency;
    private RequestQueue mQueue;
    private double exchangeRate = 0;
    private String lastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
        mQueue = Volley.newRequestQueue(this);
        dbHandler = new DBHandler(this, null, null, 1);
        selectedCat = findViewById(R.id.newRecordCat_textView);
        selectWallet = findViewById(R.id.newRecordWallet_textView);
        title = findViewById(R.id.newRecordTitle_editText);
        amt = findViewById(R.id.newRecordAmt_editText);
        description = findViewById(R.id.newRecordDes_editText);
        fab = findViewById(R.id.newRecord_fab);
        title_layout = findViewById(R.id.newRecordTitle_layout);

        checkValues = initCheckValues();

        Intent intent = getIntent();
        String walletName = intent.getStringExtra("walletName");
        walletCurrency = intent.getStringExtra("walletCurrency");
        selectWallet.setText(walletName);

        baseCurrency = "sgd";
        promptConversion();

        RecyclerView catRV = findViewById(R.id.newRecordCat_RV);
        CatSliderAdapter myCatAdapter = new CatSliderAdapter(dbHandler.getCategories(), selectedCat);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        catRV.setLayoutManager(myLayoutManager);
        myLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        catRV.setItemAnimator(new DefaultItemAnimator());
        catRV.setAdapter(myCatAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title_txt = checkTitle();
                String des_txt = description.getText().toString();
                String cat = checkCat();
                double amount = checkAmt();
                Calendar currentTime = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                String date = dateFormat.format(currentTime.getTime());
                String time = timeFormat.format(currentTime.getTime());

                if (validRecord()){
                    dbHandler.addRecord(new Record(title_txt, des_txt, amount, cat, date, time, intent.getIntExtra("walletId", 0)));
                    Toast.makeText(getApplicationContext(), "Transaction Saved", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please fill in", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

    private void promptConversion(){
        if (!baseCurrency.equals(walletCurrency.toLowerCase())){
            getExchangeRate(walletCurrency.toLowerCase(), baseCurrency);
        }
    }

    private void currencyDialog(){
        Dialog dialog = new Dialog(NewRecordActivity.this);
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

        baseCur.setText(baseCurrency.toUpperCase());
        forCur.setText(walletCurrency.toUpperCase());
        updateDate.setText("Last Updated: "+lastUpdate);

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
                    baseAmt.setText(new DecimalFormat("0.00").format(Math.round((Double.parseDouble(forAmt.getText().toString()) * exchangeRate) * 100.0) / 100.0));
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

    private void getExchangeRate(String from, String to){
        String url = String.format("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/%s/%s.json", from, to);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            exchangeRate = response.getDouble(to);
                            lastUpdate = response.getString("date");
                            currencyDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Data Unavailable", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Service Temporarily Unavailable", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private HashMap<String, Boolean> initCheckValues(){
        HashMap<String, Boolean> m = new HashMap<String, Boolean>();
        m.put("amount", false);
        m.put("title", false);
        m.put("category", false);
        return m;
    }

    private double checkAmt(){
        String amt_txt = amt.getText().toString();
        if (amt_txt.length() > 0){
            checkValues.put("amount", true);
            return Double.parseDouble(amt_txt);
        }
        return 0;
    }

    private String checkTitle(){
        String title_txt = title.getText().toString();
        if (title_txt.length() > 30){
            title_layout.setError("Character limit exceeded");
            return null;
        }
        else{
            checkValues.put("title", true);
            return title_txt;
        }
    }

    private String checkCat(){
        String cat = selectedCat.getText().toString();
        if (!cat.equals("Select a Category")){
            checkValues.put("category", true);
            return cat;
        }
        else{
            return null;
        }
    }

    private boolean validRecord(){
        boolean valid = true;
        for (String key : checkValues.keySet()) {
            if (!checkValues.get(key)){
                valid = false;
            }
        }
        return valid;
    }

}