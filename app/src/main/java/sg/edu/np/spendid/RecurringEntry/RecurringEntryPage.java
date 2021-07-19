package sg.edu.np.spendid.RecurringEntry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import sg.edu.np.spendid.R;

public class RecurringEntryPage extends AppCompatActivity {
    private Animation open, close, up, down;
    private FloatingActionButton fab, addRecurring;
    private boolean fabClicked, collapse_add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recurring_entry_page);

        fab = findViewById(R.id.RecurringMain_fab);
        addRecurring = findViewById(R.id.RecurringAddEntry_fab);

        open = AnimationUtils.loadAnimation(this, R.anim.rotate_open_animation);
        close = AnimationUtils.loadAnimation(this, R.anim.rotate_close_animation);
        up = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top_animation);
        down = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom_animation);
        hideHiddenFab(); //Init hidden buttons
        initToolbar(); //set toolbar

        addRecurring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecurringEntryPage.this, AddRecurringEntry.class);
                startActivity(intent);
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabClicked = !fabClicked;
                if (fabClicked){
                    addRecurring.startAnimation(up);
                    fab.startAnimation(open);
                    showHiddenFab();
                }
                else{
                    addRecurring.startAnimation(down);
                    fab.startAnimation(close);
                    hideHiddenFab();
                }
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
    protected void onPause() {
        super.onPause();
        resetFab();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    private void showHiddenFab(){
        addRecurring.setVisibility(View.VISIBLE);
        addRecurring.setClickable(true);
    }

    private void hideHiddenFab(){
        addRecurring.setVisibility(View.INVISIBLE);
        addRecurring.setClickable(false);
    }

    private void resetFab(){
        //hide fab if hidden fab shown (used for onPause)
        if (fabClicked){
            addRecurring.startAnimation(down);
            fab.startAnimation(close);
            hideHiddenFab();
            fabClicked = !fabClicked;
        }
    }
}