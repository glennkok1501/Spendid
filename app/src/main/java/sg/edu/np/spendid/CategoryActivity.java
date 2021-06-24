package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CategoryActivity extends AppCompatActivity {
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        FloatingActionButton fab = findViewById(R.id.addCat_Btn);
        dbHandler = new DBHandler(this, null,null, 1);

        //Tool bar
        TextView activityTitle = findViewById(R.id.mainToolbarTitle_textView);
        ImageView backArrow = findViewById(R.id.mainToolbarMenu_imageView);
        ImageView delete = findViewById(R.id.mainToolbarMore_imageView);
        delete.setImageResource(R.drawable.ic_delete_32);
        backArrow.setImageResource(R.drawable.ic_back_arrow_32);
        activityTitle.setText("Categories");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, DeleteCategoryActivity.class);
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, AddCategoryActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void onStart() {
        super.onStart();
        RecyclerView catRv = findViewById(R.id.delete_Category_recyclerView);
        CatAdaptor ca = new CatAdaptor(dbHandler.getCategories(), false, null);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        catRv.setLayoutManager(lm);
        catRv.setItemAnimator(new DefaultItemAnimator());
        catRv.setAdapter(ca);
    }
}