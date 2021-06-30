package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SplashScreenActivity extends AppCompatActivity {
    private String[] currencyList;
    private DBHandler dbHandler;
    private ArrayList<Currency> currencyArrayList;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        currencyList = getResources().getStringArray(R.array.countries);
        currencyArrayList = new ArrayList<>();
        dbHandler = new DBHandler(this, null, null, 1);
        mQueue = Volley.newRequestQueue(this);
        fetchAPI("sgd");

    }

    private void fetchAPI(String currency){
        String url = String.format("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/%s.json", currency);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject exchangeRates = response.getJSONObject(currency);
                            for (String s : currencyList){
                                String[] split = s.split(";");
                                currencyArrayList.add(new Currency(split[1], exchangeRates.getDouble(split[1]), response.getString("date")));
                            }
                            dbHandler.updateCurrencies(currencyArrayList);
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Data Unavailable", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            useBackUp();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Service Temporarily Unavailable", Toast.LENGTH_LONG).show();
                error.printStackTrace();
                useBackUp();
            }
        });
        mQueue.add(request);
    }


    private void useBackUp(){
        if (dbHandler.getCurrency("sgd") == null) {
            retrieveBackup();
            dbHandler.addCurrencies(currencyArrayList);
            Toast.makeText(getApplicationContext(), "Backup Retrieved", Toast.LENGTH_SHORT).show();
        }
    }

    private void retrieveBackup() {
        InputStream stream = getResources().openRawResource(R.raw.rates_2021_06_29);
        BufferedReader reader=new BufferedReader(new InputStreamReader(stream));
        String jsonStr = "";
        String line="";
        try {
            while ((line=reader.readLine())!=null){
                jsonStr += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONObject exchangeRates = jsonObject.getJSONObject("sgd");
            for (String s : currencyList) {
                String[] split = s.split(";");
                currencyArrayList.add(new Currency(split[1], exchangeRates.getDouble(split[1]), jsonObject.getString("date")));
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

}