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

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.CartItemsViewHolder> {
    ArrayList<CartItem> data;
    Context context;
    DBHandler dbHandler;
    DecimalFormat df2 = new DecimalFormat("#0.00");

    public CartItemsAdapter(ArrayList<CartItem> input, Context context){
        data = input;
        this.context = context;
        dbHandler = new DBHandler(this.context, null, null, 1);
    }

    public CartItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
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
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog customDialog = new CustomDialog(holder.itemView.getContext());
                customDialog.showItem(c, true, c.getCartId(), CartItemsAdapter.this);
            }
        });
        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dbHandler.updateCartItem(new CartItem(c.getItemId(), c.getName(), c.getAmount(), isChecked, c.getCartId()));
            }
        });
    }

    public int getItemCount(){
        return data.size();
    }

    public void update(int cartId){
        data = dbHandler.getCartItems(cartId);
        notifyDataSetChanged();
    }

    public class CartItemsViewHolder extends RecyclerView.ViewHolder{
        TextView name, amt;
        CheckBox check;
        CardView cardView;
        public CartItemsViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.cartItemName_textView);
            amt = itemView.findViewById(R.id.cartItemAmt_textView);
            check = itemView.findViewById(R.id.cartItem_checkBox);
            cardView = itemView.findViewById(R.id.cartItem_cardView);
        }

    }
}
