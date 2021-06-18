package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class CountryCurrency extends AppCompatActivity {
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String[] countries = getResources().getStringArray(R.array.countries);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_currency);

        RecyclerView recyclerView = findViewById(R.id.listOfCountryRV);
        CurrencyAdapter cAdapter = new CurrencyAdapter(countries);
        LinearLayoutManager cLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(cLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cAdapter);

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
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}