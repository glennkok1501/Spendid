package sg.edu.np.spendid;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class CatAdaptor extends RecyclerView.Adapter<CatAdaptor.CatViewHolder> {
    ArrayList<Category> categories;
    TextView noDeletable;
    CategoryHandler categoryHandler = new CategoryHandler();
    DBHandler dbHandler;
    boolean filter;
    int selected = -1;

    public CatAdaptor(ArrayList<Category> cList, boolean f, TextView tv) {
        categories = cList;
        filter = f;
        noDeletable = tv;
        isEmpty();
    }

    public CatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        dbHandler = new DBHandler(parent.getContext(), null, null, 1);
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_category_layout, parent, false);
        ImageView deleteBtn = item.findViewById(R.id.delete_Category_imageView);
        deleteBtn.setClickable(false);
        deleteBtn.setVisibility(View.INVISIBLE);
        if (filter) {
            if (viewType > 9) {
                deleteBtn.setClickable(true);
                deleteBtn.setVisibility(View.VISIBLE);
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selected = viewType;
                        deleteDialog(parent, categories.get(viewType));
                    }
                });
            } else {
                item.findViewById(R.id.deleteCategory_linearLayout).setVisibility(View.GONE);
            }
        }

        CatViewHolder catViewHolder = new CatViewHolder(item);

        return catViewHolder;
    }

    public void onBindViewHolder(CatViewHolder vh, int pos) {
        Category c = categories.get(pos);
        vh.image.setImageResource(categoryHandler.setIcon(c.getTitle()));
        vh.catName.setText(c.getTitle());
    }

    public int getItemCount(){
        return categories.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class CatViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView catName;
        public CatViewHolder(View item){
            super(item);
            image = item.findViewById(R.id.cat_imageView);
            catName = item.findViewById(R.id.name_Category_textView);
        }
    }

    private void isEmpty() {
        if (noDeletable != null) {
            if (categories.size() == 10) {
                noDeletable.setVisibility(View.VISIBLE);
            } else {
                noDeletable.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void deleteDialog(ViewGroup parent, Category toDelete) {
        AlertDialog alert = new AlertDialog(parent.getContext());
        alert.setTitle("Delete Category");
        alert.setBody("Are you sure you want to permanently delete this category?");
        alert.setPositive().setText("Delete");
        alert.setPositive().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbHandler.catDeletable(toDelete.getTitle())) {
                    if (dbHandler.deleteCategory(toDelete.getTitle())) {
                        categories.remove(selected);
                        notifyItemRemoved(selected);
                        isEmpty();
                        Toast.makeText(parent.getContext(), toDelete.getTitle() + " deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(parent.getContext(), "No such category", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(parent.getContext(), toDelete.getTitle() + " cannot be deleted as it contains transactions",
                            Toast.LENGTH_SHORT).show();
                }
                alert.dismiss();

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
