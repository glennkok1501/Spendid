package sg.edu.np.spendid.RecurringEntry.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Models.Recurring;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.RecurringEntry.EditRecurringEntry;
import sg.edu.np.spendid.RecurringEntry.ManageRecurringDialog;


public class RecurringSelectAdapter extends RecyclerView.Adapter<RecurringSelectViewHolder>{
    ArrayList<Recurring> data;
    private Context context;
    private DBHandler dbHandler;
    private DecimalFormat df2 = new DecimalFormat("#0.00");

    public RecurringSelectAdapter(ArrayList<Recurring> input, Context context, DBHandler dbHandler){
        data=input;
        this.context = context;
        this.dbHandler = dbHandler;
    }

    public RecurringSelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_recurring_layout, parent, false);
        return new RecurringSelectViewHolder(item);
    }
    public void onBindViewHolder(RecurringSelectViewHolder holder, int position){
        Recurring r = data.get(position);
        holder.name.setText(r.getRecurringtitle());
        holder.amount.setText(df2.format(r.getAmount()));
        holder.date.setText("Date Started: "+ r.getRecurringstartDate());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditRecurringEntry.class);
                intent.putExtra("recurID", r.getRecurringId());
                v.getContext().startActivity(intent);
            }
        });

    }
    public int getItemCount(){return data.size();}
}
