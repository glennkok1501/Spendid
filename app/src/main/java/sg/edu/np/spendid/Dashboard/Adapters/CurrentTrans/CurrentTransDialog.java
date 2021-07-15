package sg.edu.np.spendid.Dashboard.Adapters.CurrentTrans;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Models.Record;

//class to display transactions for the day grouped by category
public class CurrentTransDialog {
    private Context context;
    private String Title;
    private ArrayList<Record> recordArrayList;

    public CurrentTransDialog(Context context, String title, ArrayList<Record> recordArrayList) {
        this.context = context;
        Title = title;
        this.recordArrayList = recordArrayList;
    }

    public void show() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.current_transactions_dialog_layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(true);

        TextView title = dialog.findViewById(R.id.currentTransDialogTitle_textView);
        RelativeLayout bg = dialog.findViewById(R.id.currentTransDialog_relativeLayout);

        title.setText(Title);
        RecyclerView dialogRV = dialog.findViewById(R.id.currentTransDialog_RV);
        CurrentTransChildAdapter currentTransChildAdapter = new CurrentTransChildAdapter(recordArrayList, dialog);
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
}
