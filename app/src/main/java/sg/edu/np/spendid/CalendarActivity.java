package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {
    private Calendar calendar;
    private int maxDays;
    private CalendarAdapter calendarAdapter;
    private ImageView backArrow, nextArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        backArrow = findViewById(R.id.calendarBack_imageView);
        nextArrow = findViewById(R.id.calendarNext_imageView);
        calendar = Calendar.getInstance();
//        calendar.add(Calendar.MONTH, -5);
        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        TextView month = findViewById(R.id.calendarMonth_textView);
        month.setText(new SimpleDateFormat("MMMM yyyy").format(calendar.getTime()));

        RecyclerView recyclerView = findViewById(R.id.dailyGoal_RV);
        calendarAdapter = new CalendarAdapter(maxDays, month);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 7);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(calendarAdapter);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarAdapter.changeCalendar(-1);
            }
        });

        nextArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarAdapter.changeCalendar(1);
            }
        });
    }
}