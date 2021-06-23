package sg.edu.np.spendid;

import android.app.Dialog;
import android.app.Notification;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CatAdaptor extends RecyclerView.Adapter<CatAdaptor.CatViewHolder> {
    ArrayList<Category> categories;
    ArrayList<Category> deletableCats;
    CategoryHandler categoryHandler = new CategoryHandler();
    boolean filter;
    DBHandler dbHandler;

    public CatAdaptor(ArrayList<Category> cList, boolean f) {
        categories = cList;
        deletableCats = filterDeletable(cList);
        filter = f;
    }

    public CatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item;
        dbHandler = new DBHandler(parent.getContext(), null, null, 1);
        if (!filter) {
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_category_layout, parent, false);
        } else {
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_category_delete_layout, parent, false);
            item.findViewById(R.id.delete_Category_imageView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteDialog(parent, deletableCats.get(viewType).getTitle());
                }
            });
        }
        CatViewHolder catViewHolder = new CatViewHolder(item);

        return catViewHolder;
    }

    public void onBindViewHolder(CatViewHolder vh, int pos) {
        Category c;
        if (!filter) {
            c = categories.get(pos);
        } else {
            c = deletableCats.get(pos);
            if (deletableCats.size() > 0) {
                vh.noD.setVisibility(View.GONE);
            } else {
                vh.noD.setVisibility(View.VISIBLE);
            }
        }
        vh.image.setImageResource(categoryHandler.setIcon(c.getTitle()));
        vh.catName.setText(c.getTitle());
    }

    public int getItemCount(){
        if (!filter) {
            return categories.size();
        }
        return deletableCats.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class CatViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView catName;
        TextView noD;
        public CatViewHolder(View item){
            super(item);
            image = item.findViewById(R.id.cat_imageView);
            catName = item.findViewById(R.id.name_Category_textView);
            if (filter) {
                noD = item.findViewById(R.id.noDeletable_Category_textView);
            }
        }
    }

    public ArrayList<Category> filterDeletable(ArrayList<Category> originalCatList) {
        ArrayList<Category> deletable = new ArrayList<>();
        for (int i = 0; i < originalCatList.size(); i++) {
            if (i > 8 && i != originalCatList.size() - 1) {
                deletable.add(originalCatList.get(i));
            }
        }
        return deletable;
    }

    private void deleteDialog(ViewGroup parent, String toDelete) {
        Dialog dialog = new Dialog(parent.getContext());
        dialog.setContentView(R.layout.pos_neg_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);
        RelativeLayout bg = dialog.findViewById(R.id.pos_neg_dialog_relativeLayout);
        TextView title = dialog.findViewById(R.id.pos_neg_dialog_title);
        TextView body = dialog.findViewById(R.id.pos_neg_dialog_body);
        TextView yes = dialog.findViewById(R.id.pos_neg_dialog_yes);
        TextView no = dialog.findViewById(R.id.pos_neg_dialog_no);

        title.setText("Delete Category");
        body.setText("Are you sure you want to permanently delete this category?");

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("TAG", toDelete);
                ArrayList<String> values = dbHandler.deleteCategory(toDelete);
                if (values.get(0).equals("true")){
                    notifyDataSetChanged();
                }
                Toast.makeText(parent.getContext(), values.get(1), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        bg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dialog.dismiss();
                return false;
            }
        });
        dialog.show();
    }

}
