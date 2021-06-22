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

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>{
    ArrayList<ShoppingCart> data;
    private Context context;
    private DBHandler dbHandler;
    private DecimalFormat df2 = new DecimalFormat("#0.00");

    public ShoppingListAdapter(ArrayList<ShoppingCart> input, Context getContext){
        data = input;
        context = getContext;
        dbHandler = new DBHandler(context, null, null, 1);
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

    public class ShoppingListViewHolder extends RecyclerView.ViewHolder {
        TextView name, amt, items;
        CardView cardView;
        public ShoppingListViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.shopListMainCartName_textView);
            amt = itemView.findViewById(R.id.shopListMainCartAmt_textView);
            items = itemView.findViewById(R.id.shopListMainCartItems_textView);
            cardView = itemView.findViewById(R.id.shopListMainCart_cardView);
        }
    }
}
