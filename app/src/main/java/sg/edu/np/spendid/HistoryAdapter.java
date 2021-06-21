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

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{
    HashMap<String, ArrayList<Record>> recordData;
    ArrayList<String> dates;
    String baseCurrency;

    public HistoryAdapter(HashMap<String, ArrayList<Record>> recordList, String currency) {
        recordData = recordList;
        dates = sortDates(new ArrayList<>(recordList.keySet()));
        baseCurrency = currency;
    }

    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_layout, parent, false);
        HistoryViewHolder historyViewHolder = new HistoryViewHolder(item);

        RecyclerView rv = item.findViewById(R.id.historyRV);
        TransactionAdapter ta = new TransactionAdapter(recordData.get(dates.get(viewType)), baseCurrency, false);
        LinearLayoutManager lm = new LinearLayoutManager(parent.getContext());
        rv.setLayoutManager(lm);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(ta);

        return historyViewHolder;
    }

    public void onBindViewHolder(HistoryViewHolder vh, int pos) {
        String s = dates.get(pos);
        vh.date.setText(formatDate(s));
    }

    public int getItemCount() { return recordData.size(); }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private ArrayList<String> sortDates(ArrayList<String> dates){
        Date[] newDates = new Date[dates.size()];
        ArrayList<String> dateList = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            try {
                newDates[i] = new SimpleDateFormat("yyyy-MM-dd").parse(dates.get(i));
            }
            catch (ParseException e) {}
        }
        Arrays.sort(newDates, Collections.reverseOrder());

        for (int i = 0; i < newDates.length; i++) {
            dateList.add(new SimpleDateFormat("yyyy-MM-dd").format(newDates[i]));
        }
        return dateList;
    }

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

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        RecyclerView rv;

        public HistoryViewHolder(View item) {
            super(item);
            date = item.findViewById(R.id.history_date_textView);
            rv = item.findViewById(R.id.historyRV);
        }
    }
}

