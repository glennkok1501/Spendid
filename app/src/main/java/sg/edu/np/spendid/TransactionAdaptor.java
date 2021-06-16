package sg.edu.np.spendid;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TransactionAdaptor extends RecyclerView.Adapter<TransactionViewHolder>{
    ArrayList<Record> transactions;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public TransactionAdaptor(ArrayList<Record> transactionList) {
        transactions = transactionList;
    }

    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_layout, parent, false);
        TransactionViewHolder transactionViewHolder = new TransactionViewHolder(item);

        return transactionViewHolder;
    }

    public void onBindViewHolder(TransactionViewHolder vh, int pos) {
        Record r = transactions.get(pos);
        vh.title.setText(r.getTitle());
        vh.time.setText(r.getTimeCreated());
        vh.amt.setText(df2.format(r.getAmount()));
        /*
        vh.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionHistoryActivity.this, EditRecordActivity.class);
            }
        });
         */
    }

    public int getItemCount() { return transactions.size(); }
}
