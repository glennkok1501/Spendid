package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewShoppingCartActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_shopping_cart);
        //Tool Bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        activityTitle.setText("New Shopping List");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dbHandler = new DBHandler(this, null, null, 1);
        name = findViewById(R.id.addShoppingCartName_editText);
        Button addBtn = findViewById(R.id.addShoppingCart_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCart();
                finish();
            }
        });
    }

    private void addCart(){
        dbHandler.addShoppingCart(new ShoppingCart(name.getText().toString(), currentDate()));
        Toast.makeText(getApplicationContext(), "Cart Added", Toast.LENGTH_SHORT).show();
    }

    private String currentDate(){
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(currentTime.getTime());
    }
}