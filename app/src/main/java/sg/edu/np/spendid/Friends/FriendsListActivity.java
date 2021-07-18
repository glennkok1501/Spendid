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

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Friends.Adapters.FriendsListAdapter;
import sg.edu.np.spendid.R;

public class FriendsListActivity extends AppCompatActivity {

    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        dbHandler = new DBHandler(this, null, null, 1);
        initToolbar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        RecyclerView friendsListRV = findViewById(R.id.friendsList_RV);
        FriendsListAdapter friendsListAdapter = new FriendsListAdapter(dbHandler.getFriends());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FriendsListActivity.this);
        friendsListRV.setLayoutManager(linearLayoutManager);
        friendsListRV.setItemAnimator(new DefaultItemAnimator());
        friendsListRV.setAdapter(friendsListAdapter);
    }

    private void initToolbar(){
        //Tool bar
        TextView activityTitle = findViewById(R.id.mainToolbarTitle_textView);
        ImageView backArrow = findViewById(R.id.mainToolbarMenu_imageView);
        ImageView scan = findViewById(R.id.mainToolbarMore_imageView);
        backArrow.setImageResource(R.drawable.ic_back_arrow_32);
        scan.setImageResource(R.drawable.ic_scanner_24);
        activityTitle.setText("Friends List");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //direct to scanner activity
                startActivity(new Intent(FriendsListActivity.this, FriendScannerActivity.class));
            }
        });
    }
}