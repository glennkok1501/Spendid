package sg.edu.np.spendid;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.CartItemsViewHolder> {
    ArrayList<CartItem> data;
    private Context context;
    private DBHandler dbHandler;
    private DecimalFormat df2 = new DecimalFormat("#0.00");

    public CartItemsAdapter(ArrayList<CartItem> input, Context getContext){
        data = input;
        context = getContext;
        dbHandler = new DBHandler(context, null, null, 1);
    }

    public CartItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
        item.findViewById(R.id.cartItem_cardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog customDialog = new CustomDialog(v.getContext());
                CartItem c = data.get(viewType);
                customDialog.showItem(c, true, c.getCartId(), CartItemsAdapter.this);
            }
        });
        CheckBox check = item.findViewById(R.id.cartItem_checkBox);
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CartItem c = data.get(viewType);
                dbHandler.updateCartItem(new CartItem(c.getItemId(), c.getName(), c.getAmount(), isChecked, c.getCartId()));
            }
        });
        return new CartItemsViewHolder(item);
    }

    public void onBindViewHolder(CartItemsViewHolder holder, int position){
        CartItem c = data.get(position);
        holder.name.setText(c.getName());
        holder.amt.setText(df2.format(c.getAmount()));
        if (c.isCheck()){
            holder.check.setChecked(true);
        }
        else{
            holder.check.setChecked(false);
        }
    }

    public int getItemCount(){
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void update(int cartId){
        data = dbHandler.getCartItems(cartId);
        notifyDataSetChanged();
    }

    public class CartItemsViewHolder extends RecyclerView.ViewHolder{
        TextView name, amt;
        CheckBox check;
        public CartItemsViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.cartItemName_textView);
            amt = itemView.findViewById(R.id.cartItemAmt_textView);
            check = itemView.findViewById(R.id.cartItem_checkBox);
        }
    }
}
