package sg.edu.np.spendid;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

//class for creating custom alert dialog
// with positive and negative decision
public class AlertDialog {
    private String title;
    private String body;
    private TextView positiveBtn;
    private TextView negativeBtn;
    private Dialog dialog;
    private Context context;

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

    public AlertDialog(Context context) {
        this.context = context;
        dialog = new Dialog(this.context);
        dialog.setContentView(R.layout.pos_neg_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);
        ;
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