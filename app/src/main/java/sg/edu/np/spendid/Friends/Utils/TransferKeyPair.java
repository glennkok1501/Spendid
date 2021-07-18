package sg.edu.np.spendid.Friends.Utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import sg.edu.np.spendid.Models.Currency;
import sg.edu.np.spendid.R;

public class TransferKeyPair {
    private Context context;
    private String publicKey;
    private String privateKey;
    private final String PUBLIC_KEY = "publicKey";
    private final String PRIVATE_KEY = "privateKey";

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public TransferKeyPair(Context context) {
        this.context = context;
    }

    public void Export() throws Exception {
        Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show();
        String filename = "spendid_keypair_"+new SimpleDateFormat("dd-MM-yyyy_HHmmss").format(Calendar.getInstance().getTime())+".json";
        File fileLocation = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), filename);
        FileOutputStream out = new FileOutputStream(fileLocation);
        out.write(keyPairToJsonString().getBytes());
        out.close();
        Toast.makeText(context, "Downloaded: "+fileLocation, Toast.LENGTH_SHORT).show();
    }


    private String keyPairToJsonString() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put(PUBLIC_KEY, publicKey);
        obj.put(PRIVATE_KEY, privateKey);
        return obj.toString();
    }

    public JSONObject Import(Uri uri) throws Exception{
        HashMap<String, String> keyPairMap = new HashMap<>();

        InputStream inputStream = context.getContentResolver().openInputStream(uri);

        //read file
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder jsonStr = new StringBuilder();
        String line;
        while ((line=reader.readLine())!=null){
            jsonStr.append(line); //append to line for each next line
        }

        return new JSONObject(jsonStr.toString()); //create json object from string

    }
}
