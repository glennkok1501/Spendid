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
import java.util.ArrayList;

public class TransactionAdaptor extends RecyclerView.Adapter<TransactionViewHolder>{
    ArrayList<Record> transactions;
    String baseCurrency;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public TransactionAdaptor(ArrayList<Record> transactionList, String currency) {
        transactions = transactionList;
        baseCurrency = currency;
    }

    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_layout, parent, false);
        TransactionViewHolder transactionViewHolder = new TransactionViewHolder(item);

        item.findViewById(R.id.viewRecord_cardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewTransactionActivity.class);
                intent.putExtra("recordId", transactions.get(viewType).getId());
                v.getContext().startActivity(intent);
            }
        });

        return transactionViewHolder;
    }

    public void onBindViewHolder(TransactionViewHolder vh, int pos) {
        Record r = transactions.get(pos);
        vh.title.setText(r.getTitle());
        vh.time.setText(r.getTimeCreated());
        vh.cur.setText(baseCurrency);
        vh.amt.setText(df2.format(r.getAmount()));
        setIcon(vh, r.getCategory());

    }

    public int getItemCount() { return transactions.size(); }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void setIcon(TransactionViewHolder holder, String cat) {
        switch (cat) {
            case "Shopping":
                holder.cat.setImageResource(R.drawable.ic_shopping_24);
                break;
            case "Food":
                holder.cat.setImageResource(R.drawable.ic_food_24);
                break;
            case "Entertainment":
                holder.cat.setImageResource(R.drawable.ic_entertainment_24);
                break;
            case "Leisure":
                holder.cat.setImageResource(R.drawable.ic_leisure_24);
                break;
            case "Transport":
                holder.cat.setImageResource(R.drawable.ic_transport_24);
                break;
            case "Housing":
                holder.cat.setImageResource(R.drawable.ic_housing_24);
                break;
            case "Vehicle":
                holder.cat.setImageResource(R.drawable.ic_vehicle_24);
                break;
            case "Income":
                holder.cat.setImageResource(R.drawable.ic_income_24);
                break;
            case "Salary":
                holder.cat.setImageResource(R.drawable.ic_salary_24);
                break;
            case "Others":
                holder.cat.setImageResource(R.drawable.ic_others_24);
                break;
        }
    }
}
