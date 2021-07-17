package sg.edu.np.spendid.ShoppingList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Models.ShoppingCart;
import sg.edu.np.spendid.ShoppingList.Adapters.ShoppingLists.ShoppingListAdapter;

public class ShoppingListMainActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private FloatingActionButton addCartBtn;
    private TextView empty;
    private ShoppingListAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_main);
        dbHandler = new DBHandler(this, null, null, 1);
        addCartBtn = findViewById(R.id.shoppingListMain_fab);
        empty = findViewById(R.id.shoppingListEmpty_textView);

        initToolbar(); //set toolbar

        addCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddShoppingCartDialog dialog = new AddShoppingCartDialog(ShoppingListMainActivity.this, myAdapter);
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
        ArrayList<ShoppingCart> shoppingCarts = dbHandler.getShoppingCarts();
        RecyclerView recyclerView = findViewById(R.id.shoppingListMain_RV);
        myAdapter = new ShoppingListAdapter(shoppingCarts, empty, this);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(myLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);
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
        //Tool Bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        activityTitle.setText("Shopping List");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}