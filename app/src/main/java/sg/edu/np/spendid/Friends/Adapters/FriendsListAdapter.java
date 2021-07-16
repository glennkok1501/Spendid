package sg.edu.np.spendid.Friends.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
    }

    public int getItemCount(){
        return data.size();
    }
}
