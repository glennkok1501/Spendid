package sg.edu.np.spendid;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Base64;

public class ImportCSV {
    private Context context;
    private Uri uri;
    private Wallet wallet;
    private DBHandler dbHandler;
    private final String delimiter = ",";

    public ImportCSV(Context context, Uri uri, Wallet wallet, DBHandler dbHandler) {
        this.context = context;
        this.uri = uri;
        this.wallet = wallet;
        this.dbHandler = dbHandler;
    }

    public void run() throws Exception {
        //resolved Uri to filepath
        InputStream inputStream = context.getContentResolver().openInputStream(uri);

        //read file
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        //initiate empty array list to store records retrieved
        ArrayList<Record> newRecords = new ArrayList<>();

        CryptographyBase64 b64 = new CryptographyBase64();

        //read file line by line
        String line;
        while ((line = reader.readLine()) != null ){
            String[] data = line.split(delimiter); //separate data by delimiter

            byte[] decodedImg = (!data[6].equals("null")) ? b64.b64ToBytes(data[6]) : null;

            Log.v("TAG", data[6]);
            //add record to arrayList
            newRecords.add(new Record(data[0], data[1],Double.parseDouble(data[2]), data[3], data[4],data[5], decodedImg, wallet.getWalletId()));
        }

        //insert records to database in bulk when completed
        dbHandler.addRangeRecord(newRecords);
        Toast.makeText(context, "File successfully imported", Toast.LENGTH_SHORT).show();
    }
}

