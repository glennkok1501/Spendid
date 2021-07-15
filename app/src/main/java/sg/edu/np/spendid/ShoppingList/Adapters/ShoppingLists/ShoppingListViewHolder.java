package sg.edu.np.spendid.ShoppingList.Adapters.ShoppingLists;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.spendid.R;

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