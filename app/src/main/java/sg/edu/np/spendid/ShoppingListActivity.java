package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private int cartId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        dbHandler = new DBHandler(this, null, null, 1);
        Intent intent = getIntent();
        cartId = intent.getIntExtra("cartId", 0);
        FloatingActionButton addItemBtn = findViewById(R.id.addCartItem_fab);

        RecyclerView recyclerView = findViewById(R.id.shopList_RV);
        CartItemsAdapter myAdapter = new CartItemsAdapter(dbHandler.getCartItems(cartId), this);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(myLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog customDialog = new CustomDialog(ShoppingListActivity.this);
                customDialog.showItem(null, false, cartId, myAdapter);
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
        for (CartItem c : dbHandler.getCartItems(cartId)){
            Log.v("TAG", c.getName()+" - "+c.isCheck());
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}