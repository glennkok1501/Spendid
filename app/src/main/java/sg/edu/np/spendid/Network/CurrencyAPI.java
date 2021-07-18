package sg.edu.np.spendid.Network;

import android.content.Context;
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

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Models.Currency;
import sg.edu.np.spendid.R;

//class to call currency API
//Github repo: https://github.com/fawazahmed0/currency-api

public class CurrencyAPI {
    private String[] currencyList;
    private DBHandler dbHandler;
    private ArrayList<Currency> currencyArrayList;
    private RequestQueue mQueue;
    private boolean currencyIsEmpty;
    private Context context;

    public CurrencyAPI(Context context, DBHandler dbHandler) {
        this.context = context;
        this.dbHandler = dbHandler;
        currencyArrayList = new ArrayList<>();
        currencyList = this.context.getResources().getStringArray(R.array.countries);
        mQueue = Volley.newRequestQueue(this.context);
        currencyIsEmpty = dbHandler.getCurrency("sgd") == null;
    }

    //retrieve json data from API
    public void getData(String currency){
        String url = String.format("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/%s.json", currency.toLowerCase());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject exchangeRates = response.getJSONObject(currency); //get json object from request
                            //get only currencies needed based on currency list
                            for (String s : currencyList){
                                String[] split = s.split(";"); //split to use currency code
                                currencyArrayList.add(new Currency(split[1], exchangeRates.getDouble(split[1]), response.getString("date")));
                            }
                            //add currencies if database is empty and update if not based on currencyArrayList
                            if (currencyIsEmpty){
                                dbHandler.addCurrencies(currencyArrayList);
                            }
                            else{
                                dbHandler.updateCurrencies(currencyArrayList);
                            }

                        } catch (JSONException e) {
                            Toast.makeText(context, "Data unavailable", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            useBackUp();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Currency exchange temporarily unavailable", Toast.LENGTH_LONG).show();
                error.printStackTrace();
                useBackUp();
            }
        });
        mQueue.add(request);
    }

    //use backup currency data if no Internet connection or API downtime
    private void useBackUp(){
        //only add if database is empty if not last updated will be used
        if (currencyIsEmpty){
            retrieveBackup();
            dbHandler.addCurrencies(currencyArrayList);
            Toast.makeText(context, "Retrieved backup", Toast.LENGTH_LONG).show();
        }
    }

    private void retrieveBackup() {
        //read raw json data
        InputStream stream = context.getResources().openRawResource(R.raw.rates_2021_06_29);
        BufferedReader reader=new BufferedReader(new InputStreamReader(stream));
        String jsonStr = "";
        String line;
        try {
            while ((line=reader.readLine())!=null){
                jsonStr += line; //append to line for each next line
            }

            JSONObject jsonObject = new JSONObject(jsonStr); //create json object from string
            JSONObject exchangeRates = jsonObject.getJSONObject("sgd");
            //get only currencies needed based on currency list
            for (String s : currencyList) {
                String[] split = s.split(";");
                currencyArrayList.add(new Currency(split[1], exchangeRates.getDouble(split[1]), jsonObject.getString("date")));
            }
        }
        catch (JSONException | IOException e){
            e.printStackTrace();
            Toast.makeText(context, "Unable to retrieved backup", Toast.LENGTH_LONG).show();
        }
    }
}
