   package sg.edu.np.spendid.Records;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Models.Record;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Utils.Helpers.CategoryHelper;
import sg.edu.np.spendid.Models.Wallet;

   public class ViewTransactionActivity extends AppCompatActivity {
    private Record record;
    private Wallet wallet;
    private DBHandler dbHandler;
    private final DecimalFormat df2 = new DecimalFormat("#0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction);
        dbHandler = new DBHandler(this, null, null, 1);

        initToolbar(); //set toolbar

        //retrieve record Id
        Intent intent = getIntent();
        record = dbHandler.getRecord(intent.getIntExtra("recordId", 0));
        wallet = dbHandler.getWallet(record.getWalletId()); //get wallet based on record Id
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
        TextView viewImage = findViewById(R.id.viewTransImage_textView);

        title.setText(record.getTitle());
        amt.setText(df2.format(record.getAmount()));
        walletName.setText(wallet.getName());

        //change image based on category
        String category = record.getCategory();
        catImg.setImageResource(new CategoryHelper().setIcon(category));
        catName.setText(category);

        //set expense or income image
        checkExpense(dbHandler.catIsExpense(category), walletExpense);

        dateTime.setText("Made Transaction on "+record.getDateCreated()+" at "+record.getTimeCreated());
        cur.setText(getString(R.string.baseCurrency));

        //validate description
        String des_text = record.getDescription();
        if (des_text.length() == 0){
            des.setText("No Description");
        }
        else{
            des.setText(des_text);
        }

        loadImage(viewImage);


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

    private void checkExpense(boolean isExpense, ImageView walletExpense){
        //check if record is an expense or income to change image accordingly
        if (isExpense){
            walletExpense.setImageResource(R.drawable.ic_expense_down);
        }
        else{
            walletExpense.setImageResource(R.drawable.ic_income_up);
        }
    }

    private void loadImage(TextView viewImage){
        if (record.getImage() != null){
            viewImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewTransactionActivity.this, ViewImageActivity.class);
                    intent.putExtra("recordId", record.getId());
                    startActivity(intent);
                }
            });
        }
        else{
            viewImage.setVisibility(View.GONE);
        }
    }

    private void initToolbar(){
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
    }

}