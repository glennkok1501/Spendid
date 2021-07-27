package sg.edu.np.spendid.Statistics;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Models.ShoppingCart;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.ShoppingList.Adapters.ShoppingLists.ShoppingListAdapter;

/*
dialog to show amount and date
used for category amounts and charts
 */

public class AmountDialog {
    private Context context;
    private Dialog dialog;
    private final SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
    private final DecimalFormat df2 = new DecimalFormat("#0.00");

    public AmountDialog(Context context) {
        this.context = context;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.stats_graph_amount_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(true);
    }

    public void show(double amount, Date date){
        TextView amountText = dialog.findViewById(R.id.graph_amt_textView);
        TextView dateText = dialog.findViewById(R.id.graph_date_textView);
        RelativeLayout layout = dialog.findViewById(R.id.graph_dialog_layout);

        if (date != null){
            dateText.setText(sdf.format(date));
        }
        else{
            dateText.setVisibility(View.GONE);
        }
        amountText.setText(df2.format(amount));
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dialog.dismiss();
                return false;
            }
        });

        dialog.show();
    }

}

