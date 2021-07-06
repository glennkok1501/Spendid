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

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsViewHolder> {
    ArrayList<CartItem> data;
    private Context context;
    private DBHandler dbHandler;
    private TextView empty;
    private DecimalFormat df2 = new DecimalFormat("#0.00");

    public CartItemsAdapter(ArrayList<CartItem> input, TextView empty, Context context){
        data = input;
        this.context = context;
        this.empty = empty;
        dbHandler = new DBHandler(this.context, null, null, 1);
        checkEmpty();
    }

    public CartItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
        return new CartItemsViewHolder(item);
    }

    public void onBindViewHolder(CartItemsViewHolder holder, int position){
        CartItem cartItem = data.get(position);
        holder.name.setText(cartItem.getName());
        holder.amt.setText(df2.format(cartItem.getAmount()));
        holder.check.setChecked(cartItem.isCheck());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItemDialog dialog = new CartItemDialog(v.getContext(), true, CartItemsAdapter.this);
                dialog.setCartItem(cartItem);
                dialog.show();
            }
        });
        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dbHandler.updateCartItem(new CartItem(cartItem.getItemId(), cartItem.getName(), cartItem.getAmount(), isChecked, cartItem.getCartId()));
                cartItem.setCheck(isChecked);
            }
        });
    }

    public int getItemCount(){
        return data.size();
    }

    public void update(ArrayList<CartItem> updatedData){
        data = updatedData;
        notifyDataSetChanged();
        checkEmpty();
    }

    public void edit(CartItem c, CartItem newC){
        int pos = data.indexOf(c);
        data.remove(pos);
        data.add(pos, newC);
        notifyItemChanged(pos);
    }

    public void delete(CartItem c){
        int pos = data.indexOf(c);
        data.remove(c);
        notifyItemRemoved(pos);
        checkEmpty();
    }

    public void clear(){
        int size = data.size();
        data.clear();
        notifyItemRangeRemoved(0, size);
        empty.setVisibility(View.VISIBLE);
    }

    //prompt message if not data
    private void checkEmpty(){
        if (data.size() == 0){
            empty.setVisibility(View.VISIBLE);
        }
        else{
            empty.setVisibility(View.INVISIBLE);
        }
    }

}
