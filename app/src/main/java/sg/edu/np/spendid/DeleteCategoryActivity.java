package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DeleteCategoryActivity extends AppCompatActivity {
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_category);

        TextView noDeletable = findViewById(R.id.noDeletable_Category_textView);
        dbHandler = new DBHandler(this, null, null, 1);

        //Tool bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        activityTitle.setText("Delete Category");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });
    }

    protected void onStart() {
        super.onStart();
        RecyclerView catRv = findViewById(R.id.delete_Category_recyclerView);
        CatAdaptor ca = new CatAdaptor(dbHandler.getCategories(), true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        catRv.setLayoutManager(lm);
        catRv.setItemAnimator(new DefaultItemAnimator());
        catRv.setAdapter(ca);
        ca.notifyDataSetChanged();
    }
}