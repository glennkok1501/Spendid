package sg.edu.np.spendid;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CalendarViewHolder extends RecyclerView.ViewHolder {
    TextView day;
    public CalendarViewHolder(View itemView){
        super(itemView);
        day = itemView.findViewById(R.id.calendarDay_textView);
    }
}
