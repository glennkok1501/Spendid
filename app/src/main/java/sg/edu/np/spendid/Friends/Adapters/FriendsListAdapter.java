package sg.edu.np.spendid.Friends.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.spendid.Friends.ViewFriendActivity;
import sg.edu.np.spendid.Models.Friend;
import sg.edu.np.spendid.R;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListViewHolder> {

    ArrayList<Friend> data;

    public FriendsListAdapter(ArrayList<Friend> input){
        data = input;
    }

    public FriendsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_layout, parent, false);
        return new FriendsListViewHolder(item);
    }

    public void onBindViewHolder(FriendsListViewHolder holder, int position){
        Friend friend = data.get(position);
        holder.name.setText(friend.getName());
        holder.date.setText(friend.getDateAdded());
        Context context = holder.itemView.getContext();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewFriendActivity.class);
                intent.putExtra("friendId", friend.getFriendId());
                context.startActivity(intent);
            }
        });
    }

    public int getItemCount(){
        return data.size();
    }
}
