package sg.edu.np.spendid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder>{
    HashMap<String, ArrayList<Record>> recordData;
    private ArrayList<String> dates;

    public HistoryAdapter(HashMap<String, ArrayList<Record>> recordList) {
        recordData = recordList;
        dates = sortDates(new ArrayList<>(recordList.keySet()));
    }

    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_layout, parent, false);
        return new HistoryViewHolder(item);
    }

    public void onBindViewHolder(HistoryViewHolder vh, int pos) {
        String s = dates.get(pos);
        vh.date.setText(formatDate(s));
        TransactionAdapter ta = new TransactionAdapter(recordData.get(s), false);
        LinearLayoutManager lm = new LinearLayoutManager(vh.itemView.getContext());
        vh.rv.setLayoutManager(lm);
        vh.rv.setItemAnimator(new DefaultItemAnimator());
        vh.rv.setAdapter(ta);
    }

    public int getItemCount() { return recordData.size(); }

    //sort record dates from most recent
    private ArrayList<String> sortDates(ArrayList<String> dates){
        Date[] newDates = new Date[dates.size()];
        ArrayList<String> dateList = new ArrayList<>();
        //parse dates to Date object
        for (int i = 0; i < dates.size(); i++) {
            try {
                newDates[i] = new SimpleDateFormat("yyyy-MM-dd").parse(dates.get(i));
            }
            catch (ParseException e) {}
        }
        //sort using Arrays method
        Arrays.sort(newDates, Collections.reverseOrder());

        //append back to array in order
        for (int i = 0; i < newDates.length; i++) {
            dateList.add(new SimpleDateFormat("yyyy-MM-dd").format(newDates[i]));
        }
        return dateList;
    }

    //format date for better presentation
    private String formatDate(String d){
        String dateFormat;
        try{
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(d);
            dateFormat = new SimpleDateFormat("dd MMMM yyyy").format(date);
        }
        catch (ParseException e) {
            dateFormat = d;
        }
        return dateFormat;
    }
}

