package sg.edu.np.spendid.DataTransfer.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import sg.edu.np.spendid.Friends.Adapters.SelectFriendAdapter;
import sg.edu.np.spendid.Models.Friend;
import sg.edu.np.spendid.R;

import static android.content.Context.MODE_PRIVATE;

public class SelectFriendDialog {
    private ArrayList<Friend> friendsList;
    private Dialog dialog;
    private Context context;
    private Friend selected;
    private final String PUBLIC_KEY = "publicKey";

    public Friend getSelected() {
        return selected;
    }

    public void setSelected(Friend selected) {
        this.selected = selected;
    }

    public SelectFriendDialog (Context context, ArrayList<Friend> friendsList) {
        this.context = context;
        this.friendsList = friendsList;
        this.selected = null;

        //Insert user into friends list
        SharedPreferences prefs = context.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        this.friendsList.add(0, new Friend("Myself", currentDate(), prefs.getString(PUBLIC_KEY, null)));

        dialog = new Dialog(this.context);
        dialog.setContentView(R.layout.select_friend_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);
    }

    public void show(){
        RecyclerView recyclerView = dialog.findViewById(R.id.selFriendsList_RV);
        SelectFriendAdapter myAdapter = new SelectFriendAdapter(friendsList, this);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(myLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }

    private String currentDate(){
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(currentTime.getTime());
    }
}