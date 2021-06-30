package sg.edu.np.spendid;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;

public class CartItemDialog {
    private Dialog dialog;
    private CartItem cartItem;
    private boolean editable;
    private int cartId;
    private CartItemsAdapter adapter;
    private DBHandler dbHandler;
    private Context context;
    private DecimalFormat df2 = new DecimalFormat("#0.00");

    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public CartItemDialog(Context context, boolean editable, CartItemsAdapter adapter) {
        this.context = context;
        this.editable = editable;
        this.adapter = adapter;
        dbHandler = new DBHandler(this.context, null, null, 1);

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.add_cart_item_layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);
    }

    public void show() {
        EditText name = dialog.findViewById(R.id.addCartItemName_editText);
        EditText amt = dialog.findViewById(R.id.addCartItemAmt_editText);
        ImageView close = dialog.findViewById(R.id.addCartItemClose_imageView);
        Button btn = dialog.findViewById(R.id.addCartItem_btn);
        FloatingActionButton delBtn = dialog.findViewById(R.id.deleteCartItem_fab);

        if (editable) {
            name.setText(cartItem.getName());
            amt.setText(df2.format(cartItem.getAmount()));
            setDeleteBtn(delBtn, true);
            btn.setText("Edit Item");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String n = name.getText().toString();
                    if (checkInput(n)){
                        CartItem c = new CartItem(cartItem.getItemId(), n, Double.parseDouble(checkAmt(amt.getText().toString())), cartItem.isCheck(), cartItem.getCartId());
                        dbHandler.updateCartItem(c);
                        Toast.makeText(v.getContext(), "Item Updated", Toast.LENGTH_SHORT).show();
                        adapter.edit(cartItem, c);
                        dialog.dismiss();
                    }
                }
            });
        } else {
            setDeleteBtn(delBtn, false);
            btn.setText("Add to Cart");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String n = name.getText().toString();
                    if (checkInput(n)) {
                        CartItem c = new CartItem(n, Double.parseDouble(checkAmt(amt.getText().toString())), false, cartId);
                        dbHandler.addCartItem(c);
                        Toast.makeText(v.getContext(), "Item Added", Toast.LENGTH_SHORT).show();
                        adapter.update(dbHandler.getCartItems(cartId));
                        dialog.dismiss();
                    }
                }
            });
        }

        close.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dialog.dismiss();
                return false;
            }
        });
        dialog.show();
    }

    private void setDeleteBtn(FloatingActionButton delBtn, boolean visible) {
        if (visible) {
            delBtn.setVisibility(View.VISIBLE);
            delBtn.setClickable(true);
            delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dbHandler.deleteCartItem(cartItem.getItemId())) {
                        Toast.makeText(v.getContext(), "Item Deleted", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        adapter.delete(cartItem);
                    }
                }
            });
        } else {
            delBtn.setVisibility(View.INVISIBLE);
            delBtn.setClickable(false);
            delBtn.setOnClickListener(null);
        }
    }

    private boolean checkInput(String name) {
        if (name.length() == 0 || name.length() > 15) {
            TextInputLayout editLayout = dialog.findViewById(R.id.addCartItemName_layout);
            editLayout.setError("Invalid Name");
            return false;
        }
        return true;
    }

    private String checkAmt(String amt) {
        if (amt.length() == 0) {
            amt = "0";
        }
        return amt;
    }
}
