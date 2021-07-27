package sg.edu.np.spendid.Friends.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.spendid.Friends.ViewFriendActivity;
import sg.edu.np.spendid.Models.Friend;
import sg.edu.np.spendid.R;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListViewHolder> {

    ArrayList<Friend> data;
    private TextView emptyText;

    public FriendsListAdapter(ArrayList<Friend> input, TextView emptyText){
        data = input;
        this.emptyText = emptyText;
        checkEmpty();
    }

    public FriendsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_layout, parent, false);
        return new FriendsListViewHolder(item);
    }

    public void onBindViewHolder(FriendsListViewHolder holder, int position){
        Friend friend = data.get(position);
        holder.name.setText(friend.getName());
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

    private void checkEmpty(){
        if (data.size() == 0){
            emptyText.setVisibility(View.VISIBLE);
            emptyText.setText("Your friends list is empty");
        }
        else{
            emptyText.setVisibility(View.GONE);
        }
    }
}
