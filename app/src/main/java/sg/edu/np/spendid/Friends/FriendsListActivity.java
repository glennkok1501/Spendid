package sg.edu.np.spendid.Friends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Friends.Adapters.FriendsListAdapter;
import sg.edu.np.spendid.Models.Friend;
import sg.edu.np.spendid.R;

public class FriendsListActivity extends AppCompatActivity {

    private DBHandler dbHandler;
    private TextView emptyListText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        emptyListText = findViewById(R.id.friendsList_empty_textView);
        dbHandler = new DBHandler(this, null, null, 1);
        initToolbar();

        FloatingActionButton fab = findViewById(R.id.friendsList_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FriendsListActivity.this, FriendScannerActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        ArrayList<Friend> friends = dbHandler.getFriends();

        RecyclerView friendsListRV = findViewById(R.id.friendsList_RV);
        FriendsListAdapter friendsListAdapter = new FriendsListAdapter(friends, emptyListText);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FriendsListActivity.this);
        friendsListRV.setLayoutManager(linearLayoutManager);
        friendsListRV.setItemAnimator(new DefaultItemAnimator());
        friendsListRV.setAdapter(friendsListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initToolbar(){
        //Tool bar
        TextView activityTitle = findViewById(R.id.activityTitle_toolBar);
        ImageView backArrow = findViewById(R.id.activityImg_toolBar);
        backArrow.setImageResource(R.drawable.ic_back_arrow_32);
        activityTitle.setText("Friends List");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}