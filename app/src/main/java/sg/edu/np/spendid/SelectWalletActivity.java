//Created by Glenn

package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

public class SelectWalletActivity extends AppCompatActivity {
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_wallet);
        dbHandler = new DBHandler(this, null, null, 1);

        RecyclerView selWalletRV = findViewById(R.id.sel_wallet_RV);
        WalletSelectAdapter WalletSelectAdapter = new WalletSelectAdapter(dbHandler.getWallets(), this);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        selWalletRV.setLayoutManager(myLayoutManager);
        selWalletRV.setItemAnimator(new DefaultItemAnimator());
        selWalletRV.setAdapter(WalletSelectAdapter);
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
        finish();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}