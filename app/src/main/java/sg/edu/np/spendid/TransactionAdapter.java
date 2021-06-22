package sg.edu.np.spendid;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionViewHolder>{
    ArrayList<Record> transactions;
    String baseCurrency;
    boolean showDate;
    CategoryHandler categoryHandler = new CategoryHandler();
    DecimalFormat df2 = new DecimalFormat("#0.00");

    public TransactionAdapter(ArrayList<Record> transactionList, String baseCurrency, boolean showDate) {
        transactions = transactionList;
        this.baseCurrency = baseCurrency;
        this.showDate = showDate;
    }

    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_layout, parent, false);
        TransactionViewHolder transactionViewHolder = new TransactionViewHolder(item);
        return transactionViewHolder;
    }

    public void onBindViewHolder(TransactionViewHolder vh, int pos) {
        Record r = transactions.get(pos);
        vh.title.setText(r.getTitle());
        vh.time.setText(formatTime(r.getTimeCreated()));
        vh.cur.setText(baseCurrency);
        vh.amt.setText(df2.format(r.getAmount()));
        if (showDate){vh.date.setText(formatDate(r.getDateCreated()));}
        else{vh.date.setText("");}
        vh.cat.setImageResource(categoryHandler.setIcon(r.getCategory()));
        vh.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewTransactionActivity.class);
                intent.putExtra("recordId", r.getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    public int getItemCount() { return transactions.size(); }

    public void filter(ArrayList<Record> rList) {
        transactions = rList;
        notifyDataSetChanged();
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
