package sg.edu.np.spendid.Friends.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.spendid.R;

public class FriendsListViewHolder extends RecyclerView.ViewHolder {
    TextView name;

    public FriendsListViewHolder(View itemView){
        super(itemView);
        name = itemView.findViewById(R.id.friendsLayout_name_textView);
    }
}
