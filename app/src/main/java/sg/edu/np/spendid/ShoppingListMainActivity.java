package sg.edu.np.spendid;

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

public class ShoppingListMainActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private FloatingActionButton addCartBtn;
    private TextView empty;
    private ShoppingListMainAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_main);
        dbHandler = new DBHandler(this, null, null, 1);
        addCartBtn = findViewById(R.id.shoppingListMain_fab);
        empty = findViewById(R.id.shoppingListEmpty_textView);

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
        ArrayList<ShoppingCart> shoppingCarts = dbHandler.getShoppingCarts();
        RecyclerView recyclerView = findViewById(R.id.shoppingListMain_RV);
        myAdapter = new ShoppingListMainAdapter(shoppingCarts, empty, this);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(myLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);
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

}