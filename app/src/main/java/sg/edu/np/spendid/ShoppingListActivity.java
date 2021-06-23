package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private int cartId;
    private CartItemsAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        dbHandler = new DBHandler(this, null, null, 1);
        Intent intent = getIntent();
        cartId = intent.getIntExtra("cartId", 0);
        FloatingActionButton addItemBtn = findViewById(R.id.addCartItem_fab);
        //Tool bar
        initToolbar();

        RecyclerView recyclerView = findViewById(R.id.shopList_RV);
        myAdapter = new CartItemsAdapter(dbHandler.getCartItems(cartId), this);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(myLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.ShowCartItem showCartItem = new CustomDialog(ShoppingListActivity.this).new ShowCartItem(false, myAdapter);
                showCartItem.setCartId(cartId);
                showCartItem.show();
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
//        for (CartItem c : dbHandler.getCartItems(cartId)){
//            Log.v("TAG", c.getName()+" - "+c.isCheck());
//        }
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
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ShoppingListActivity.this, more);
                popupMenu.getMenu().add("Clear");
                popupMenu.getMenu().add("Delete");
                popupMenu.getMenu().add("Create Transaction");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getTitle().toString()){
                            case "Clear":
                                clearCart();
                                break;
                            case "Delete":
                                deleteShoppingCart();
                                finish();
                                break;
                            case "Create Transaction":
                                String PREF_NAME = "sharedPrefs";
                                SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                                AddCartToRecord addCartToRecord = new AddCartToRecord(ShoppingListActivity.this, prefs.getString("baseCurrency", ""), cartId);
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

    private void clearCart(){
        Log.v("TAG", "ID: "+cartId);
        dbHandler.deleteCartItems(cartId);
        myAdapter.clear();
        Toast.makeText(getApplicationContext(), "Cleared", Toast.LENGTH_SHORT).show();
    }

    private void deleteShoppingCart(){
        dbHandler.deleteCartItems(cartId);
        dbHandler.deleteShoppingCart(cartId);
        Toast.makeText(getApplicationContext(), "Shopping Cart Deleted", Toast.LENGTH_SHORT).show();
    }

}