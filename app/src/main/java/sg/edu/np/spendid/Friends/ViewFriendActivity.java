package sg.edu.np.spendid.Friends;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Dialogs.MyAlertDialog;
import sg.edu.np.spendid.Friends.Utils.GenerateQRCode;
import sg.edu.np.spendid.Friends.Utils.ShareText;
import sg.edu.np.spendid.Models.Friend;
import sg.edu.np.spendid.R;

public class ViewFriendActivity extends AppCompatActivity {

    private DBHandler dbHandler;
    private Friend friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend);
        ImageView qrcode = findViewById(R.id.profileView_qrcode_imageView);
        TextView shareCode = findViewById(R.id.profileView_key_textView);
        TextView friendName = findViewById(R.id.profileView_name_textView);
        TextView dateAdded = findViewById(R.id.profileView_date_textView);
        initToolbar();
        dbHandler = new DBHandler(this, null, null, 1);

        Intent intent = getIntent();

        //get friend object
        friend = dbHandler.getFriend(intent.getIntExtra("friendId", 0));

        //generate friend qr code
        new GenerateQRCode(this, qrcode).run(friend.getName()+";"+friend.getPublicKey());

        //set values
        friendName.setText(friend.getName());
        dateAdded.setText(friend.getDateAdded());

        shareCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareText(ViewFriendActivity.this).start("Spendid Key", setShareText());
            }
        });
    }

    private String setShareText(){
        return "This is "+friend.getName()+"'s key:\n"+friend.getPublicKey();
    }

    //prompt alert dialog for deletion of friend
    private void deleteDialog(){
        MyAlertDialog alert = new MyAlertDialog(this);
        alert.setTitle("Delete Friend");
        alert.setBody("Are you sure you want to delete "+friend.getName()+" from your friends list?");
        alert.setPositive().setText("Delete");
        alert.setPositive().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFriend();
                alert.dismiss();
            }
        });
        alert.setNegative().setText("Cancel");
        alert.setNegative().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    private void deleteFriend(){
        if (dbHandler.deleteFriend(friend.getFriendId())){
            Toast.makeText(getApplicationContext(), "Friend Deleted", Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(), "Unknown Friend", Toast.LENGTH_SHORT).show();
        }
    }

    private void initToolbar(){
        //Tool bar
        TextView activityTitle = findViewById(R.id.toolbarTitle_textView);
        ImageView backArrow = findViewById(R.id.toolbarBtn_imageView1);
        ImageView trash = findViewById(R.id.toolbarBtn_imageView2);
        backArrow.setImageResource(R.drawable.ic_back_arrow_32);
        trash.setImageResource(R.drawable.ic_delete_32);
        activityTitle.setText("Friend");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog();
            }
        });
    }

}