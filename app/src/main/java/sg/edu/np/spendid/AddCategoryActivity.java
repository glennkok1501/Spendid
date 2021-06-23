package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class AddCategoryActivity extends AppCompatActivity {
    private TextInputLayout newCatName_layout;
    private EditText newCatName;
    private Switch ie;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        newCatName_layout = findViewById(R.id.newCatName_layout);
        newCatName = findViewById(R.id.newCatName_editText);
        ie = findViewById(R.id.ie_switch);
        FloatingActionButton fab = findViewById(R.id.newCat_fab);
        dbHandler = new DBHandler(this, null,null, 1);

        //Tool bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        backArrow.setImageResource(R.drawable.ic_back_arrow_32);
        activityTitle.setText("Add New Category");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });

        ie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ie.setText("Expense");
                } else {
                    ie.setText("Income");
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidName()) {
                    Category newCat = new Category(newCatName.getText().toString(), ie.isChecked());
                    dbHandler.addCategory(newCat);
                    Toast.makeText(getApplicationContext(), "Added " + newCatName.getText().toString() +
                            " Category", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private boolean isValidName() {
        boolean valid = false;
        ArrayList<Category> categories = dbHandler.getCategories();

        //Name cannot be blank
        if (newCatName.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
            return valid;
        }

        //Name cannot be longer than 15 characters
        if (newCatName.getText().toString().length() > 15) {
            Toast.makeText(getApplicationContext(), "Please limit number of characters to 15",
                    Toast.LENGTH_SHORT).show();
            return valid;
        }

        //Name cannot already exist
        for (Category c : categories) {
            if (newCatName.getText().toString().equals(c.getTitle())) {
                Toast.makeText(getApplicationContext(), "Category already exists, please try another name",
                        Toast.LENGTH_SHORT).show();
                return valid;
            }
        }

        return true;
    }

}