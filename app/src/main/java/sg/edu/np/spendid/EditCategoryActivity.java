package sg.edu.np.spendid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditCategoryActivity extends AppCompatActivity {
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        ImageView catLogo = findViewById(R.id.editCatImage_imageView);
        EditText catName = findViewById(R.id.editCatName_editText);
        Switch expSwitch = findViewById(R.id.editIE_switch);
        FloatingActionButton fab = findViewById(R.id.editCat_fab);
        dbHandler = new DBHandler(this, null, null, 1);

        Intent intent = getIntent();
        boolean editable = intent.getBooleanExtra("Editable", false);
        int img = intent.getIntExtra("ImageResource", new CategoryHandler().setIcon(""));
        String name = intent.getStringExtra("Name");
        Boolean catExp = intent.getBooleanExtra("Expense", false);

        catLogo.setImageResource(img);
        catName.setText(name);
        if (catExp) {
            expSwitch.setText("Expense");
        }
        expSwitch.setChecked(catExp);

        expSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    expSwitch.setText("Expense");
                } else {
                    expSwitch.setText("Income");
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler.updateCategory(name, catName.getText().toString(), expSwitch.isChecked());
                Toast.makeText(EditCategoryActivity.this, "Changes saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //Tool bar
        TextView activityTitle = findViewById(R.id.mainToolbarTitle_textView);
        ImageView backArrow = findViewById(R.id.mainToolbarMenu_imageView);
        ImageView delete = findViewById(R.id.mainToolbarMore_imageView);
        activityTitle.setText("Edit Category");
        backArrow.setImageResource(R.drawable.ic_back_arrow_32);
        delete.setImageResource(R.drawable.ic_delete_32);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog(name);
            }
        });

        if (!editable) {
            delete.setVisibility(View.INVISIBLE);
            catName.setEnabled(false);
            expSwitch.setClickable(false);
            fab.setVisibility(View.INVISIBLE);
        }
    }

    private void deleteDialog(String toDelete) {
        AlertDialog alert = new AlertDialog(this);
        alert.setTitle("Delete Category");
        alert.setBody("Are you sure you want to permanently delete this category?");
        alert.setPositive().setText("Delete");
        alert.setPositive().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbHandler.catDeletable(toDelete)) {
                    if (dbHandler.deleteCategory(toDelete)) {
                        Toast.makeText(EditCategoryActivity.this, toDelete + " deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditCategoryActivity.this, "No such category", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditCategoryActivity.this, toDelete + " cannot be deleted as there are" +
                                    " transactions under this category",
                            Toast.LENGTH_SHORT).show();
                }
                alert.dismiss();
                finish();
            }
        });
        alert.setNegative().setText("Cancel");
        alert.setNegative().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }
}