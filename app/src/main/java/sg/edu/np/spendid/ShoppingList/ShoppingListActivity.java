package sg.edu.np.spendid.ShoppingList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import sg.edu.np.spendid.ShoppingList.Adapters.CartItems.CartItemDialog;
import sg.edu.np.spendid.ShoppingList.Adapters.CartItems.CartItemsAdapter;
import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.R;

//display list of items in shopping list
public class ShoppingListActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private int cartId;
    private CartItemsAdapter myAdapter;
    private TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        dbHandler = new DBHandler(this, null, null, 1);
        Intent intent = getIntent();
        cartId = intent.getIntExtra("cartId", 0);
        FloatingActionButton addItemBtn = findViewById(R.id.addCartItem_fab);
        empty = findViewById(R.id.cartItemsEmpty_textView);

        initToolbar(); //set toolbar

        RecyclerView recyclerView = findViewById(R.id.cartItems_RV);
        myAdapter = new CartItemsAdapter(dbHandler.getCartItems(cartId), empty, dbHandler);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(myLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItemDialog dialog = new CartItemDialog(ShoppingListActivity.this, false, myAdapter, dbHandler);
                dialog.setCartId(cartId);
                dialog.show();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initToolbar(){
        TextView activityTitle = findViewById(R.id.mainToolbarTitle_textView);
        ImageView backArrow = findViewById(R.id.mainToolbarMenu_imageView);
        ImageView more = findViewById(R.id.mainToolbarMore_imageView);
        backArrow.setImageResource(R.drawable.ic_back_arrow_32);
        activityTitle.setText("Shopping Cart");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initPopupMenu(more);

    }

    private void initPopupMenu(ImageView more){
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ShoppingListActivity.this, more);
                popupMenu.getMenu().add("Clear All");
                popupMenu.getMenu().add("Delete Shopping List");
                popupMenu.getMenu().add("Add to Transaction");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getTitle().toString()){
                            case "Clear All":
                                clearCart();
                                break;
                            case "Delete Shopping List":
                                deleteShoppingCart();
                                finish();
                                break;
                            case "Add to Transaction":
                                AddCartToRecord addCartToRecord = new AddCartToRecord(ShoppingListActivity.this, cartId, dbHandler);
                                addCartToRecord.add();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), "Unknown Page", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    //delete all items in shopping list
    private void clearCart(){
        dbHandler.deleteCartItems(cartId);
        myAdapter.clear();
        Toast.makeText(getApplicationContext(), "Cleared", Toast.LENGTH_SHORT).show();
    }

    //delete shopping list with associated items
    private void deleteShoppingCart(){
        dbHandler.deleteCartItems(cartId);
        dbHandler.deleteShoppingCart(cartId);
        Toast.makeText(getApplicationContext(), "Shopping List Deleted", Toast.LENGTH_SHORT).show();
    }

}