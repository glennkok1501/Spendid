package sg.edu.np.spendid;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CatAdaptor extends RecyclerView.Adapter<CatViewHolder> {
    ArrayList<Category> categories;
    CategoryHandler categoryHandler = new CategoryHandler();
    DBHandler dbHandler;
    int selected = -1;

    public CatAdaptor(ArrayList<Category> cList) {
        categories = cList;
    }

    public CatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        dbHandler = new DBHandler(parent.getContext(), null, null, 1);
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_category_layout, parent, false);
        CatViewHolder catViewHolder = new CatViewHolder(item);

        return catViewHolder;
    }

    public void onBindViewHolder(CatViewHolder vh, int pos) {
        Category c = categories.get(pos);
        vh.image.setImageResource(categoryHandler.setIcon(c.getTitle()));
        vh.catName.setText(c.getTitle());
        vh.catCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditCategoryActivity.class);
                if (pos > 9) {
                    intent.putExtra("Editable", true);
                } else {
                    intent.putExtra("Editable", false);
                }
                intent.putExtra("ImageResource", categoryHandler.setIcon(c.getTitle()));
                intent.putExtra("Name", c.getTitle());
                intent.putExtra("Expense", c.isExpense());
                v.getContext().startActivity(intent);
            }
        });
    }

    public int getItemCount(){
        return categories.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
