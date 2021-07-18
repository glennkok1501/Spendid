package sg.edu.np.spendid.DataTransfer.Utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Models.Record;
import sg.edu.np.spendid.Utils.Security.Cryptography;
import sg.edu.np.spendid.Utils.Security.CryptographyBase64;
import sg.edu.np.spendid.Models.Wallet;

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

    public void run(boolean encrypted) throws Exception {
        //resolved Uri to filepath
        InputStream inputStream = context.getContentResolver().openInputStream(uri);

        //read file
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        //initiate empty array list to store records retrieved
        ArrayList<Record> newRecords = new ArrayList<>();
        CryptographyBase64 b64 = new CryptographyBase64();

        if (encrypted){
            //read encrypted file
            String line;
            StringBuilder encryptedMessage = new StringBuilder();
            while ((line = reader.readLine()) != null ){
                encryptedMessage.append(line);
            }
            //decrypt message and add transactions to database
            String decrypted = new Cryptography(context).Decrypt(encryptedMessage.toString());
            for (String dataLine : decrypted.split("\n")){
                String[] data = dataLine.split(delimiter); //separate data by delimiter
                byte[] decodedImg = (!data[6].equals("null")) ? b64.b64ToBytes(data[6]) : null;
                //add record to arrayList
                newRecords.add(new Record(data[0], data[1],Double.parseDouble(data[2]), data[3], data[4],data[5], decodedImg, wallet.getWalletId()));
            }
        }
        else{
            //read file line by line
            String line;
            while ((line = reader.readLine()) != null ){
                String[] data = line.split(delimiter); //separate data by delimiter

                byte[] decodedImg = (!data[6].equals("null")) ? b64.b64ToBytes(data[6]) : null;

                //add record to arrayList
                newRecords.add(new Record(data[0], data[1],Double.parseDouble(data[2]), data[3], data[4],data[5], decodedImg, wallet.getWalletId()));
            }
        }

        //insert records to database in bulk when completed
        dbHandler.addRangeRecord(newRecords);
        Toast.makeText(context, "File successfully imported", Toast.LENGTH_SHORT).show();
    }
}

