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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Models.Recurring;
import sg.edu.np.spendid.Models.Wallet;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.RecurringEntry.Adapters.RecurringSelectAdapter;

public class RecurringEntryPage extends AppCompatActivity {
    private Animation open, close, up, down;
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
        open = AnimationUtils.loadAnimation(this, R.anim.rotate_open_animation);
        close = AnimationUtils.loadAnimation(this, R.anim.rotate_close_animation);
        up = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top_animation);
        down = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom_animation);

        initToolbar(); //set toolbar

        AddRecurringFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecurringEntryPage.this, AddRecurringEntry.class);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() { super.onPause(); }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView showRecurringRV = findViewById(R.id.show_recurring_RV);
        ArrayList<Recurring> recurringArrayList = dbHandler.getAllRecurring();
        checkEmpty(recurringArrayList);
        RecurringSelectAdapter recurringSelectAdapter = new RecurringSelectAdapter(recurringArrayList, this, dbHandler);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        showRecurringRV.setLayoutManager(myLayoutManager);
        showRecurringRV.setItemAnimator(new DefaultItemAnimator());
        showRecurringRV.setAdapter(recurringSelectAdapter);
    }

    private void initToolbar(){
        //Tool bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        activityTitle.setText("Select a Recurring Entry");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

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