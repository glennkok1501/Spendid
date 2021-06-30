package sg.edu.np.spendid;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CurrentTransChildAdapter extends RecyclerView.Adapter<CurrentTransChildAdapter.CurrentTransChildViewHolder> {
    ArrayList<Record> data;
    Dialog dialog;
    DecimalFormat df2 = new DecimalFormat("#0.00");

    public CurrentTransChildAdapter(ArrayList<Record> input, Dialog dialog){
        data = input;
        this.dialog = dialog;
    }

    public CurrentTransChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_transaction_child_layout, parent, false);
        return new CurrentTransChildViewHolder(item);
    }

    public void onBindViewHolder(CurrentTransChildViewHolder holder, int position){
        Record r = data.get(position);
        holder.name.setText(r.getTitle());
        holder.time.setText(formatTime(r.getTimeCreated()));
        holder.amt.setText(df2.format(r.getAmount()));
        holder.currency.setText("SGD");
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(v.getContext(), ViewTransactionActivity.class);
                intent.putExtra("recordId", r.getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    public int getItemCount(){
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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

    public class CurrentTransChildViewHolder extends RecyclerView.ViewHolder {
        TextView name, time, amt, currency;
        LinearLayout layout;
        public CurrentTransChildViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.curTransChildName_textView);
            time = itemView.findViewById(R.id.curTransChildTime_textView);
            amt = itemView.findViewById(R.id.curTransChildAmt_textView);
            currency = itemView.findViewById(R.id.curTransChildCur_textView);
            layout = itemView.findViewById(R.id.curTransChild_linearLayout);
        }
    }
}
