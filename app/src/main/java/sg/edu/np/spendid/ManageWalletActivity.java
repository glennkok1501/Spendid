package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

public class ManageWalletActivity extends AppCompatActivity {
    private final static String PREF_NAME = "sharedPrefs";
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private DBHandler dbHandler;
    private TextView bal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_wallet);
        dbHandler = new DBHandler(this, null, null, 1);
        bal = findViewById(R.id.totalWalletBal_textView);
        bal.setText(df2.format(dbHandler.getTotalBalance()));

        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int favWallet = prefs.getInt("firstWallet", 0);

        RecyclerView recyclerView = findViewById(R.id.manageWallet_RV);
        WalletManageAdapter myAdapter = new WalletManageAdapter(dbHandler.getWallets(), favWallet);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(myLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);
    }

}