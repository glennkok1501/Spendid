package sg.edu.np.spendid.RecurringEntry;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Models.Recurring;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Wallets.EditWalletActivity;


public class ManageRecurringDialog {
    private Context context;
    private Recurring recurring;
    private DecimalFormat df2 = new DecimalFormat("#0.00");

    public ManageRecurringDialog(Context context, Recurring recurring){
        this.context = context;
        this.recurring = recurring;
    }

    public void show(){
        DBHandler dbHandler = new DBHandler(context, null, null, 1);
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.manage_view_recurring_layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(true);

        TextView name = dialog.findViewById(R.id.viewRecurTitle_textView);
        TextView amt = dialog.findViewById(R.id.viewRecurAmt_textView);
        TextView cur = dialog.findViewById(R.id.viewRecurCur_textView);
        TextView date = dialog.findViewById(R.id.viewRecurDate_textView);
        TextView des = dialog.findViewById(R.id.viewRecurDes_textView);
        FloatingActionButton editBtn = dialog.findViewById(R.id.viewRecurEdit_fab);
        RelativeLayout bg = dialog.findViewById(R.id.viewRecur_relativeLayout);
        name.setText(recurring.getRecurringtitle());
        cur.setText("SGD");
        amt.setText(df2.format(recurring.getAmount()));
        date.setText("Date Started: " + recurring.getRecurringstartDate());

        String des_text = recurring.getRecurringdescription();

        if (des_text.length() == 0){
            des.setText("No Description");
        }
        else{
            des.setText(des_text);
        }

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditRecurringEntry.class);
                intent.putExtra("recurID", recurring.getRecurringId());
                v.getContext().startActivity(intent);
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