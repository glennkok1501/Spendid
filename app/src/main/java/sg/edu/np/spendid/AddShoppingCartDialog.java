package sg.edu.np.spendid;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddShoppingCartDialog {
    private Context context;
    private DBHandler dbHandler;
    private Dialog dialog;
    private ShoppingListAdapter shoppingListAdapter;

    public AddShoppingCartDialog(Context context, ShoppingListAdapter shoppingListAdapter) {
        this.context = context;
        this.shoppingListAdapter = shoppingListAdapter;
        dbHandler = new DBHandler(context, null, null, 1);

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.add_shopping_list_layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);
    }

    public void show(){
        EditText name = dialog.findViewById(R.id.addShoppingCartName_editText);
        Button addBtn = dialog.findViewById(R.id.addShoppingCart_btn);
        ImageView close = dialog.findViewById(R.id.addShoppingCartClose_imageView);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCart(name.getText().toString());
                shoppingListAdapter.update(dbHandler.getShoppingCarts());
                dialog.dismiss();
            }
        });

        close.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dialog.dismiss();
                return false;
            }
        });
        dialog.show();
    }

    private void addCart(String name){
        dbHandler.addShoppingCart(new ShoppingCart(name, currentDate()));
        Toast.makeText(this.context, "Cart Added", Toast.LENGTH_SHORT).show();
    }

    private String currentDate(){
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(currentTime.getTime());
    }


}

