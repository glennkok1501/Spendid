package sg.edu.np.spendid.Friends.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.spendid.DataTransfer.Utils.SelectFriendDialog;
import sg.edu.np.spendid.Models.Friend;
import sg.edu.np.spendid.R;


public class SelectFriendAdapter extends RecyclerView.Adapter<FriendsListViewHolder> {

    ArrayList<Friend> data;
    private SelectFriendDialog dialog;

    public SelectFriendAdapter(ArrayList<Friend> input, SelectFriendDialog dialog){
        data = input;
        this.dialog = dialog;
    }

    public FriendsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_layout, parent, false);
        return new FriendsListViewHolder(item);
    }

    public void onBindViewHolder(FriendsListViewHolder holder, int position){
        Friend friend = data.get(position);
        holder.name.setText(friend.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setSelected(data.get(position));
                dialog.dismiss();
            }
        });
    }

    public int getItemCount(){
        return data.size();
    }
}

