   package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ViewTransactionActivity extends AppCompatActivity {
    private Record record;
    private DBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction);
        dbHandler = new DBHandler(this, null, null, 1);
        Intent intent = getIntent();
        record = dbHandler.getRecord(intent.getIntExtra("recordId", 0));
        Log.v("TAG", ""+record.getId());

        FloatingActionButton editBtn = findViewById(R.id.viewTransactionEdit_fab);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("TAG", ""+record.getId());
                Intent intent = new Intent(ViewTransactionActivity.this, EditRecordActivity.class);
                intent.putExtra("recordId", record.getId());
                startActivity(intent);
                finish();
            }
        });
    }
}