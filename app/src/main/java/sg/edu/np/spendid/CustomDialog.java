package sg.edu.np.spendid;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class CustomDialog {
    private final static String PREF_NAME = "sharedPrefs";
    private DecimalFormat df2 = new DecimalFormat("#0.00");

    private Context context;

    public CustomDialog(Context context) {
        this.context = context;
    }

    public void showManageWallet(Wallet wallet, boolean selectFav) {
        DBHandler dbHandler = new DBHandler(context, null, null, 1);
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.manage_view_wallet_layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(true);

        TextView name = dialog.findViewById(R.id.viewWalletTitle_textView);
        TextView amt = dialog.findViewById(R.id.viewWalletAmt_textView);
        TextView cur = dialog.findViewById(R.id.viewWalletCur_textView);
        TextView date = dialog.findViewById(R.id.viewWalletDate_textView);
        TextView des = dialog.findViewById(R.id.viewWalletDes_textView);
        TextView wCur = dialog.findViewById(R.id.viewWalletCurrency_textView);
        ImageView star = dialog.findViewById(R.id.manageWalletFav_imageView);
        FloatingActionButton editBtn = dialog.findViewById(R.id.viewWalletEdit_fab);
        RelativeLayout bg = dialog.findViewById(R.id.viewWallet_relativeLayout);
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        name.setText(wallet.getName());
        wCur.setText(wallet.getCurrency());
        amt.setText(df2.format(dbHandler.getWalletTotal(wallet.getWalletId())));
        cur.setText(prefs.getString("baseCurrency", "").toUpperCase());
        date.setText("Date Created: " + wallet.getDateCreated());
        String des_text = wallet.getDescription();
        if (des_text.length() == 0) {
            des.setText("No Description");
        } else {
            des.setText(des_text);
        }

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditWalletActivity.class);
                intent.putExtra("walletId", wallet.getWalletId());
                v.getContext().startActivity(intent);
                dialog.dismiss();
            }
        });

        if (selectFav){
            if (prefs.getInt("firstWallet", 0) == wallet.getWalletId()){
                star.setColorFilter(ContextCompat.getColor(context, R.color.pinkish_red));
            }
            else{
                star.setColorFilter(ContextCompat.getColor(context, R.color.light_grey));
            }

            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    star.setColorFilter(ContextCompat.getColor(context, R.color.pinkish_red));
                    SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
                    editor.putInt("firstWallet", wallet.getWalletId());
                    editor.apply();
                }
            });
        }
        else{
            star.setColorFilter(ContextCompat.getColor(context, android.R.color.transparent));
        }

        bg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dialog.dismiss();
                return false;
            }
        });
        dialog.show();
    }

    public void showCurrentTrans(String baseCurrency, String Title, ArrayList<Record> r) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.current_transactions_dialog_layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(true);

        TextView title = dialog.findViewById(R.id.currentTransDialogTitle_textView);
        RelativeLayout bg = dialog.findViewById(R.id.currentTransDialog_relativeLayout);

        title.setText(Title);
        RecyclerView dialogRV = dialog.findViewById(R.id.currentTransDialog_RV);
        CurrentTransChildAdapter currentTransChildAdapter = new CurrentTransChildAdapter(r, baseCurrency, dialog);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(context);
        dialogRV.setLayoutManager(myLayoutManager);
        dialogRV.setItemAnimator(new DefaultItemAnimator());
        dialogRV.setAdapter(currentTransChildAdapter);

        bg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dialog.dismiss();
                return false;
            }
        });

        dialog.show();
    }

    public class Alert {
        private String title;
        private String body;
        private TextView positiveBtn;
        private TextView negativeBtn;
        private Dialog dialog;

        public void setTitle(String title) {
            this.title = title;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public TextView setPositive(){
            return this.positiveBtn;
        }

        public TextView setNegative(){
            return this.negativeBtn;
        }

        public Alert() {
            this.dialog = new Dialog(context);
            dialog.setContentView(R.layout.pos_neg_dialog);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.setCancelable(false);
            RelativeLayout bg = dialog.findViewById(R.id.pos_neg_dialog_relativeLayout);
            bg.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    dialog.dismiss();
                    return false;
                }
            });
            this.positiveBtn = dialog.findViewById(R.id.pos_neg_dialog_yes);
            this.negativeBtn = dialog.findViewById(R.id.pos_neg_dialog_no);
            positiveBtn.setText("Yes");
            negativeBtn.setText("No");
        }

        public void show(){
            TextView dialogTitle = dialog.findViewById(R.id.pos_neg_dialog_title);
            TextView dialogBody = dialog.findViewById(R.id.pos_neg_dialog_body);
            dialogTitle.setText(title);
            dialogBody.setText(body);
            dialog.show();
        }

        public void dismiss(){
            dialog.dismiss();
        }
    }

    public void showItem(CartItem cartItem, boolean editable, int cartId, CartItemsAdapter adapter){
        DBHandler dbHandler = new DBHandler(context, null, null, 1);
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.add_cart_item_layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);

        EditText name = dialog.findViewById(R.id.addCartItemName_editText);
        EditText amt = dialog.findViewById(R.id.addCartItemAmt_editText);
        ImageView close = dialog.findViewById(R.id.addCartItemClose_imageView);
        Button btn = dialog.findViewById(R.id.addCartItem_btn);
        FloatingActionButton delBtn = dialog.findViewById(R.id.deleteCartItem_fab);

        if (editable){
            name.setText(cartItem.getName());
            amt.setText(df2.format(cartItem.getAmount()));
            delBtn.setVisibility(View.VISIBLE);
            delBtn.setClickable(true);
            delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dbHandler.deleteCartItem(cartItem.getItemId())){
                        Toast.makeText(v.getContext(), "Item Deleted", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        adapter.update(cartId);
                    }
                }
            });
            btn.setText("Edit Item");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CartItem c = new CartItem(cartItem.getItemId(), name.getText().toString(), Double.parseDouble(amt.getText().toString()), cartItem.isCheck(), cartId);
                    dbHandler.updateCartItem(c);
                    Toast.makeText(v.getContext(), "Item Updated", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    adapter.update(cartId);
                }
            });
        }
        else{
            delBtn.setVisibility(View.INVISIBLE);
            delBtn.setClickable(false);
            delBtn.setOnClickListener(null);
            btn.setText("Add to Cart");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CartItem c = new CartItem(name.getText().toString(), Double.parseDouble(amt.getText().toString()), false, cartId);
                    dbHandler.addCartItem(c);
                    Toast.makeText(v.getContext(), "Item Added", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    adapter.update(cartId);
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

}
