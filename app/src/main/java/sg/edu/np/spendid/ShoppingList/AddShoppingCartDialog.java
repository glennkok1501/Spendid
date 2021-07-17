package sg.edu.np.spendid.ShoppingList;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Models.ShoppingCart;
import sg.edu.np.spendid.ShoppingList.Adapters.ShoppingLists.ShoppingListAdapter;

//class to create a shopping cart in shopping list
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
        dialog.setContentView(R.layout.add_shopping_cart_layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);
    }

    public void show(){
        EditText name = dialog.findViewById(R.id.addShoppingCartName_editText);
        Button addBtn = dialog.findViewById(R.id.addShoppingCart_btn);
        ImageView close = dialog.findViewById(R.id.addShoppingCartClose_imageView);
        TextInputLayout layout = dialog.findViewById(R.id.addShoppingCartName_layout);

        //getting name of cart and creating a cart
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cartName = name.getText().toString();
                if (validCartName(cartName)){
                    addCart(cartName);
                    shoppingListAdapter.update(dbHandler.getShoppingCarts());
                    dialog.dismiss();
                }
                else{
                    layout.setError("Invalid Cart Name");
                }
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

    private boolean validCartName(String name){
        return !(name.length() < 1 || name.length() > 15);
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

