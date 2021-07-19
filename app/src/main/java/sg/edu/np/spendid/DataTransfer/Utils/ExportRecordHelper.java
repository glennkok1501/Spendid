package sg.edu.np.spendid.DataTransfer.Utils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import sg.edu.np.spendid.Models.Record;
import sg.edu.np.spendid.Utils.Security.CryptographyBase64;

public class ExportRecordHelper {
    private ArrayList<Record> records;
    private String delimiter = ",";

    public ExportRecordHelper(ArrayList<Record> records) {
        this.records = records;
    }

    public StringBuilder ToCSV(){
        StringBuilder data = new StringBuilder(); //initiate string builder to store data
        CryptographyBase64 b64 = new CryptographyBase64(); //used Base64 for encoding images

        //iterate through records array list and add to string builder
        for (Record r : records){
            //remove characters that can cause issue to file or importing
            String title = r.getTitle().replaceAll("[\n,]", "");
            String des = r.getDescription().replaceAll("[\n,]","");

            //encode image
            String encodedImg = (r.getImage() != null) ? b64.bytesToB64(r.getImage()).replace("\n", "") : "null";
            data.append(title+delimiter+
                    des+delimiter+
                    r.getAmount()+delimiter+
                    r.getCategory()+delimiter+
                    r.getDateCreated()+delimiter+
                    r.getTimeCreated()+delimiter+
                    encodedImg+"\n");
        }
        return data;
    }
}
