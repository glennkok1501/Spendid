package sg.edu.np.spendid;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>{
    ArrayList<ShoppingCart> data;

    public ShoppingListAdapter(ArrayList<ShoppingCart> input){
        data = input;
    }

    public ShoppingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_cart_layout, parent, false);
        item.findViewById(R.id.shopListMainCart_cardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ShoppingListActivity.class);
                intent.putExtra("cartId", data.get(viewType).getCartId());
                v.getContext().startActivity(intent);
            }
        });
        return new ShoppingListViewHolder(item);
    }

    public void onBindViewHolder(ShoppingListViewHolder holder, int position){
        ShoppingCart s = data.get(position);
        holder.name.setText(s.getName());
        holder.amt.setText("amount");
        holder.items.setText("items");
    }

    public int getItemCount(){
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ShoppingListViewHolder extends RecyclerView.ViewHolder {
        TextView name, amt, items;
        public ShoppingListViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.shopListMainCartName_textView);
            amt = itemView.findViewById(R.id.shopListMainCartAmt_textView);
            items = itemView.findViewById(R.id.shopListMainCartItems_textView);
        }
    }
}
