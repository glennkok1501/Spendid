   package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

public class ViewTransactionActivity extends AppCompatActivity {
    private Record record;
    private Wallet wallet;
    private DBHandler dbHandler;
    private final DecimalFormat df2 = new DecimalFormat("#0.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        toggleNightMode();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction);
        dbHandler = new DBHandler(this, null, null, 1);

        //Tool Bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        activityTitle.setText("View Transaction");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        record = dbHandler.getRecord(intent.getIntExtra("recordId", 0));
        wallet = dbHandler.getWallet(record.getWalletId());
        FloatingActionButton editBtn = findViewById(R.id.viewTransEdit_fab);
        ImageView catImg = findViewById(R.id.viewTransCatImg_imageView);
        TextView catName = findViewById(R.id.viewTransCat_textView);
        TextView title = findViewById(R.id.viewTransTitle_textView);
        TextView amt = findViewById(R.id.viewTransAmt_textView);
        TextView walletName = findViewById(R.id.viewTransWalletTitle_textView);
        ImageView walletExpense = findViewById(R.id.viewTransWalletExpense_imageView);
        TextView dateTime = findViewById(R.id.viewTransDateTime_textView);
        TextView cur = findViewById(R.id.viewTransCur_textView);
        TextView des = findViewById(R.id.viewTransDes_textView);

        String category = record.getCategory();
        catImg.setImageResource(new CategoryHandler().setIcon(category));
        catName.setText(category);
        title.setText(record.getTitle());
        amt.setText(df2.format(record.getAmount()));
        walletName.setText(wallet.getName());
        if (dbHandler.catIsExpense(category)){
            walletExpense.setImageResource(R.drawable.ic_expense_down);
        }
        else{
            walletExpense.setImageResource(R.drawable.ic_income_up);
        }
        String PREF_NAME = "sharedPrefs";
        dateTime.setText("Made Transaction on "+record.getDateCreated()+" at "+record.getTimeCreated());
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        cur.setText(prefs.getString("baseCurrency", ""));
        String des_text = record.getDescription();
        if (des_text.length() == 0){
            des.setText("No Description");
        }
        else{
            des.setText(des_text);
        }

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewTransactionActivity.this, EditRecordActivity.class);
                intent.putExtra("recordId", record.getId());
                startActivity(intent);
                finish();
            }
        });
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