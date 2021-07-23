package sg.edu.np.spendid.RecurringEntry.Adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import sg.edu.np.spendid.Models.Recurring;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.RecurringEntry.EditRecurringEntryActivity;
import sg.edu.np.spendid.Utils.Helpers.CategoryHelper;


public class RecurringSelectAdapter extends RecyclerView.Adapter<RecurringSelectViewHolder>{
    ArrayList<Recurring> data;
    private DecimalFormat df2 = new DecimalFormat("#0.00");
    private final CategoryHelper categoryHelper = new CategoryHelper();

    public RecurringSelectAdapter(ArrayList<Recurring> input){
        data=input;
    }

    public RecurringSelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_recurring_layout, parent, false);
        return new RecurringSelectViewHolder(item);
    }
    public void onBindViewHolder(RecurringSelectViewHolder holder, int position){
        Recurring r = data.get(position);
        holder.name.setText(r.getRecurringtitle());

        if (r.getRecurringdescription() == null){
            holder.des.setText("");
        }
        else{
            holder.des.setText(r.getRecurringdescription());
        }

        holder.amount.setText(formatAmountFrequency(r.getAmount(), r.getFrequency()));

        if (r.getRecurringendDate() == null){
            holder.status.setText("Active");
        }
        else{
            holder.status.setText("Stopped on "+r.getRecurringendDate());

        }

        holder.cat.setImageResource(categoryHelper.setIcon(r.getCategory()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditRecurringEntryActivity.class);
                intent.putExtra("recurID", r.getRecurringId());
                v.getContext().startActivity(intent);
            }
        });

    }
    public int getItemCount(){return data.size();}

    private String formatAmountFrequency(double amount, String freq){
        String amountString = df2.format(amount);
        String formatted = "$"+amountString;
        switch (freq) {
            case "Daily":
                formatted += " per Day";
                break;
            case "Monthly":
                formatted += " per Month";
                break;
            default:
                formatted += " per Year";
                break;
        }
        return  formatted;
    }
}