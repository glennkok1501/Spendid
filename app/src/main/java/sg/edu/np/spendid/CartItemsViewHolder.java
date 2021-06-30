package sg.edu.np.spendid;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

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