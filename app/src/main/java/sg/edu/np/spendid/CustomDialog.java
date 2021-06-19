package sg.edu.np.spendid;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    private static DecimalFormat df2 = new DecimalFormat("#.##");

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

    public void showDecisionDialog(String Title, String Body, Runnable pos, Runnable neg) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.pos_neg_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);
        RelativeLayout bg = dialog.findViewById(R.id.pos_neg_dialog_relativeLayout);
        TextView title = dialog.findViewById(R.id.pos_neg_dialog_title);
        TextView body = dialog.findViewById(R.id.pos_neg_dialog_body);
        TextView yes = dialog.findViewById(R.id.pos_neg_dialog_yes);
        TextView no = dialog.findViewById(R.id.pos_neg_dialog_no);
        title.setText(Title);
        body.setText(Body);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos.run();
                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                neg.run();
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

    public void blank(){};
}
