package sg.edu.np.spendid;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
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

public class ManageWalletDialog {
    private final static String PREF_NAME = "sharedPrefs";
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public static void walletDialog(Context context, Wallet w, boolean selectFav) {
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
        ImageView star = dialog.findViewById(R.id.manageWalletFav_imageView);
        FloatingActionButton editBtn = dialog.findViewById(R.id.viewWalletEdit_fab);
        RelativeLayout bg = dialog.findViewById(R.id.viewWallet_relativeLayout);
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        name.setText(w.getName());
        amt.setText(df2.format(dbHandler.getWalletTotal(w.getWalletId())));
        cur.setText(w.getCurrency().toUpperCase());
        date.setText("Date Created: " + w.getDateCreated());
        String des_text = w.getDescription();
        if (des_text.length() == 0) {
            des.setText("No Description");
        } else {
            des.setText(des_text);
        }

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("TAG", "Edit Activity");
            }
        });

        if (selectFav){
            if (prefs.getInt("firstWallet", 0) == w.getWalletId()){
                star.setColorFilter(ContextCompat.getColor(context, R.color.fire_bush));
            }
            else{
                star.setColorFilter(ContextCompat.getColor(context, R.color.light_grey));
            }

            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    star.setColorFilter(ContextCompat.getColor(context, R.color.fire_bush));
                    SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
                    editor.putInt("firstWallet", w.getWalletId());
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
}
