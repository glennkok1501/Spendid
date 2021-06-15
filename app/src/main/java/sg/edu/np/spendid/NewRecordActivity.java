package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

public class NewRecordActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private TextView selectedCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
        dbHandler = new DBHandler(this, null, null, 1);
        selectedCat = findViewById(R.id.newRecordCat_textView);

        RecyclerView catRV = findViewById(R.id.newRecordCat_RV);
        CatAdapter myCatAdapter = new CatAdapter(dbHandler.getCategories(), selectedCat);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        catRV.setLayoutManager(myLayoutManager);
        myLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        catRV.setItemAnimator(new DefaultItemAnimator());
        catRV.setAdapter(myCatAdapter);
    }
}