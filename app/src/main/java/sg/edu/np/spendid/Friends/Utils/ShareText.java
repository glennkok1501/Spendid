package sg.edu.np.spendid.Friends.Utils;

import android.content.Context;
import android.content.Intent;

public class ShareText {
    private Context context;

    public ShareText(Context context) {
        this.context = context;
    }

    public void start(String SUBJECT, String TEXT){
        //Create an ACTION_SEND Intent
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);

        //The type of the content is text
        intent.setType("text/plain");

        //Applying information Subject and Body.
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, SUBJECT);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, TEXT);
        context.startActivity(Intent.createChooser(intent, SUBJECT));
    }
}
