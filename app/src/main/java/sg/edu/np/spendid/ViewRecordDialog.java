package sg.edu.np.spendid;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ViewRecordDialog {

    public static void showDialog(Context context, Record r, boolean editable) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.view_transaction_layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(true);
        RelativeLayout bg = dialog.findViewById(R.id.viewTrans_relativeLayout);
        FloatingActionButton editBtn = dialog.findViewById(R.id.viewTransactionEdit_fab);

        if (editable){
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), EditRecordActivity.class);
                    intent.putExtra("recordId", r.getId());
                    v.getContext().startActivity(intent);
                    dialog.dismiss();
                }
            });

        }
        else{
            editBtn.setClickable(false);
            editBtn.setVisibility(View.INVISIBLE);
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
