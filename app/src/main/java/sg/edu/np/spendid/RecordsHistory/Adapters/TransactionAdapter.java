package sg.edu.np.spendid.RecordsHistory.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import sg.edu.np.spendid.Models.Record;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Records.ViewTransactionActivity;
import sg.edu.np.spendid.Utils.CategoryHandler;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionViewHolder>{
    ArrayList<Record> transactions;
    private boolean showDate;
    private CategoryHandler categoryHandler = new CategoryHandler();
    private DecimalFormat df2 = new DecimalFormat("#0.00");

    public TransactionAdapter(ArrayList<Record> transactionList, boolean showDate) {
        transactions = transactionList;
        this.showDate = showDate;
    }

    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_layout, parent, false);
        TransactionViewHolder transactionViewHolder = new TransactionViewHolder(item);
        return transactionViewHolder;
    }

    public void onBindViewHolder(TransactionViewHolder vh, int pos) {
        Record record = transactions.get(pos);
        vh.title.setText(record.getTitle());
        vh.time.setText(formatTime(record.getTimeCreated()));
        vh.cur.setText(vh.itemView.getContext().getString(R.string.baseCurrency));
        vh.amt.setText(df2.format(record.getAmount()));
        if (showDate){vh.date.setText(formatDate(record.getDateCreated()));}
        else{vh.date.setVisibility(View.INVISIBLE);}
        vh.cat.setImageResource(categoryHandler.setIcon(record.getCategory()));
        vh.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewTransactionActivity.class);
                intent.putExtra("recordId", record.getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    public int getItemCount() { return transactions.size(); }

    //replace data with filtered data
    public void filter(ArrayList<Record> rList) {
        transactions = rList;
        notifyDataSetChanged();
    }

    //change format of date
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

    //change format of time
    private String formatTime(String t){
        String formatted;
        try{
            Date time = new SimpleDateFormat("HH:mm:ss").parse(t);
            formatted = new SimpleDateFormat("h:mm a").format(time);
        }
        catch (ParseException e){
            formatted = t;
        }
        return formatted;
    }
}
