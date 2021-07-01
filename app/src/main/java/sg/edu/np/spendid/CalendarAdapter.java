package sg.edu.np.spendid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    int maxDay;
    TextView month;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    int today = calendar.get(Calendar.DAY_OF_MONTH);

    public CalendarAdapter(int maxDay, TextView month){
        this.maxDay = maxDay;
        this.month = month;
    }

    public CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_layout, parent, false);
        return new CalendarViewHolder(item);
    }

    public void onBindViewHolder(CalendarViewHolder holder, int position){
        holder.day.setText(String.valueOf(position+1));
    }

    public void changeCalendar(int add){
        calendar.add(Calendar.MONTH, add);
        maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        month.setText(monthFormat.format(calendar.getTime()));
        notifyDataSetChanged();
    }

    public int getItemCount(){
        return maxDay;
    }
}
