package sg.edu.np.spendid;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

import static android.content.Context.MODE_PRIVATE;

//Class to display wallet details in dialog
public class ManageWalletDialog {
    private Context context;
    private Wallet wallet;
    private boolean selectFav;
    private final String PREF_NAME = "sharedPrefs";
    private DecimalFormat df2 = new DecimalFormat("#0.00");

    public ManageWalletDialog(Context context, Wallet wallet, boolean selectFav) {
        this.context = context;
        this.wallet = wallet;
        this.selectFav = selectFav;
    }

    public void show() {
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
        ImageView fav = dialog.findViewById(R.id.manageWalletFav_imageView);
        FloatingActionButton editBtn = dialog.findViewById(R.id.viewWalletEdit_fab);
        RelativeLayout bg = dialog.findViewById(R.id.viewWallet_relativeLayout);
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        name.setText(wallet.getName());
        wCur.setText(wallet.getCurrency());
        amt.setText(df2.format(dbHandler.getWalletTotal(wallet.getWalletId())));
        cur.setText(context.getString(R.string.baseCurrency));
        date.setText("Date Created: " + wallet.getDateCreated());
        String des_text = wallet.getDescription();
        //check if description is empty
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

        //check if dialog is able to choose favourite wallet
        if (selectFav){
            //change favourite wallet image color
            if (prefs.getInt("firstWallet", 0) == wallet.getWalletId()){
                fav.setColorFilter(ContextCompat.getColor(context, R.color.pinkish_red));
            }
            else{
                fav.setColorFilter(ContextCompat.getColor(context, R.color.light_grey));
            }

            //update new favourite when selected
            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fav.setColorFilter(ContextCompat.getColor(context, R.color.pinkish_red));
                    SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
                    editor.putInt("firstWallet", wallet.getWalletId());
                    editor.apply();
                }
            });
        }
        else{
            fav.setVisibility(View.INVISIBLE);
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
}
