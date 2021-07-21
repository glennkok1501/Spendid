package sg.edu.np.spendid.Statistics.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Statistics.Charts;

public class ChartsAdapter extends RecyclerView.Adapter<ChartsViewHolder> {

    Context context;
    Date[] months;
    ArrayList<double[]> data;
    String[] types;

    public ChartsAdapter(Context context, ArrayList<double[]> data, Date[] months){
        this.months = months;
        this.data = data;
        this.context = context;
        types = context.getResources().getStringArray(R.array.statistics_graph_label);
    }

    public ChartsViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.stats_graph_layout, parent, false);
        return new ChartsViewHolder(item);
    }

    public void onBindViewHolder(ChartsViewHolder holder, int position){
        double[] dataType = data.get(position);
        holder.title.setText(types[position]);
        setGraphImage(holder, position);
        new Charts(holder.itemView.getContext(), holder.chart, holder.months).init(dataType, months);
    }

    public int getItemCount(){
        return data.size();
    }

    private void setGraphImage(ChartsViewHolder holder, int position){
        switch (position){
            case 0:
                holder.image.setImageResource(R.drawable.ic_money_24);
                holder.image.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.fire_bush));
                break;
            case 1:
                holder.image.setImageResource(R.drawable.ic_income_up);
                holder.image.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
                break;
            case 2:
                holder.image.setImageResource(R.drawable.ic_expense_down);
                holder.image.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
                break;
        }
    }
}
