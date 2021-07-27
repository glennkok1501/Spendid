package sg.edu.np.spendid.ShoppingList.Adapters.CartItems;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Models.CartItem;
import sg.edu.np.spendid.R;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsViewHolder> {
    ArrayList<CartItem> data;
    private DBHandler dbHandler;
    private TextView empty;
    private DecimalFormat df2 = new DecimalFormat("#0.00");

    public CartItemsAdapter(ArrayList<CartItem> input, TextView empty, DBHandler dbHandler){
        data = input;
        this.empty = empty;
        this.dbHandler = dbHandler;
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
        setStrikeThrough(holder.name, cartItem.isCheck());

        //show dialog when item clicked
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItemDialog dialog = new CartItemDialog(v.getContext(), true, CartItemsAdapter.this, dbHandler);
                dialog.setCartItem(cartItem);
                dialog.show();
            }
        });

        //change checked value of item
        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dbHandler.updateCartItem(new CartItem(cartItem.getItemId(), cartItem.getName(), cartItem.getAmount(), isChecked, cartItem.getCartId()));
                cartItem.setCheck(isChecked);
                setStrikeThrough(holder.name, isChecked);
            }
        });
    }

    public int getItemCount(){
        return data.size();
    }


    //update data set
    public void update(ArrayList<CartItem> updatedData){
        data = updatedData;
        notifyDataSetChanged();
        checkEmpty();
    }

    //change specific item in data set
    public void edit(CartItem c, CartItem newC){
        int pos = data.indexOf(c);
        data.remove(pos);
        data.add(pos, newC);
        notifyItemChanged(pos);
    }

    //delete item in data set
    public void delete(CartItem c){
        int pos = data.indexOf(c);
        data.remove(c);
        notifyItemRemoved(pos);
        checkEmpty();
    }

    //remove all items
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

    private void setStrikeThrough(TextView text, boolean strike){
        if (strike){
            text.setPaintFlags(text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else{
            text.setPaintFlags(text.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

}
