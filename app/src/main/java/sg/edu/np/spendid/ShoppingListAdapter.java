package sg.edu.np.spendid;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListViewHolder>{
    ArrayList<ShoppingCart> data;
    Context context;
    DBHandler dbHandler;
    TextView empty;
    DecimalFormat df2 = new DecimalFormat("#0.00");

    public ShoppingListAdapter(ArrayList<ShoppingCart> input, TextView empty, Context context){
        data = input;
        this.context = context;
        this.empty = empty;
        dbHandler = new DBHandler(this.context, null, null, 1);
        checkEmpty();
    }

    public ShoppingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_cart_layout, parent, false);
        return new ShoppingListViewHolder(item);
    }

    public void onBindViewHolder(ShoppingListViewHolder holder, int position){
        ShoppingCart s = data.get(position);
        holder.name.setText(s.getName());
        ArrayList<CartItem> items = dbHandler.getCartItems(s.getCartId());
        double amount = 0;
        int unchecked = 0;
        for (CartItem c : items){
            amount += c.getAmount();
            if (c.isCheck()){unchecked += 1;}
        }
        holder.amt.setText(df2.format(amount));
        holder.items.setText(""+unchecked+"/"+items.size()+" items");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ShoppingListActivity.class);
                intent.putExtra("cartId", data.get(position).getCartId());
                v.getContext().startActivity(intent);
            }
        });
    }

    public int getItemCount(){
        return data.size();
    }

    public void update(ArrayList<ShoppingCart> updatedData){
        data = updatedData;
        notifyDataSetChanged();
        checkEmpty();
    }

    private void checkEmpty(){
        if (data.size() == 0){
            empty.setVisibility(View.VISIBLE);
        }
        else{
            empty.setVisibility(View.INVISIBLE);
        }
    }

}
