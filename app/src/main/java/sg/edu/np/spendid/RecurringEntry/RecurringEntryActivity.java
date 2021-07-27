package sg.edu.np.spendid.RecurringEntry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.util.ArrayList;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Models.Recurring;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.RecordsHistory.SearchActivity;
import sg.edu.np.spendid.RecordsHistory.TransactionHistoryActivity;
import sg.edu.np.spendid.RecurringEntry.Adapters.RecurringSelectAdapter;

public class RecurringEntryActivity extends AppCompatActivity {
    private FloatingActionButton AddRecurringFab;
    private DBHandler dbHandler;
    private TextView emptyRecurring;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recurring_entry_page);

        AddRecurringFab = findViewById(R.id.RecurringAddEntry_fab);
        dbHandler = new DBHandler(this, null, null, 1);
        emptyRecurring = findViewById(R.id.showRecurring_empty_textView);

        initToolbar(); //set toolbar
        //bring user from recurring entry activity to add recurring entry activity
        AddRecurringFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on FAB click, move from this class to another class
                Intent intent = new Intent(RecurringEntryActivity.this, AddRecurringEntryActivity.class);
                startActivity(intent);
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
        //Recycler view to display all recurring entries
        RecyclerView showRecurringRV = findViewById(R.id.show_recurring_RV);
        ArrayList<Recurring> recurringArrayList = dbHandler.getAllRecurring();
        checkEmpty(recurringArrayList);
        RecurringSelectAdapter recurringSelectAdapter = new RecurringSelectAdapter(recurringArrayList);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        showRecurringRV.setLayoutManager(myLayoutManager);
        showRecurringRV.setItemAnimator(new DefaultItemAnimator());
        showRecurringRV.setAdapter(recurringSelectAdapter);
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

    //Tool bar
    private void initToolbar(){
        TextView activityTitle = findViewById(R.id.toolbarTitle_textView);
        ImageView backArrow = findViewById(R.id.toolbarBtn_imageView1);
        ImageView sync = findViewById(R.id.toolbarBtn_imageView2);
        backArrow.setImageResource(R.drawable.ic_back_arrow_32);
        sync.setImageResource(R.drawable.ic_sync_32);
        activityTitle.setText("Recurring Entry");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update recurring entries
                try {
                    new UpdateEntryToWallet(dbHandler).UpdateRecurring();
                    Toast.makeText(getApplicationContext(), "Entries updated", Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //check if arraylist recurring is empty
    private void checkEmpty(ArrayList<Recurring> recurringArrayList){
        if (recurringArrayList.size() > 0){
            emptyRecurring.setVisibility(View.GONE);
        }
        else{
            emptyRecurring.setVisibility(View.VISIBLE);
            emptyRecurring.setText("You have no Recurring Entries");
        }
    }
} 