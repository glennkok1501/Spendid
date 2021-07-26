package sg.edu.np.spendid.Wallets;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Models.Record;
import sg.edu.np.spendid.Models.Wallet;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Utils.WalletNameList;


public class TransferWalletDialog {
    private Context context;
    private DBHandler dbHandler;
    private ArrayList<Wallet> wallets;
    private int walletId;

    public TransferWalletDialog(Context context, DBHandler dbHandler, int walletId) {
        this.context = context;
        this.dbHandler = dbHandler;
        this.walletId = walletId;
        wallets = dbHandler.getWallets();
    }

    public void show(){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.transfer_wallet_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);

        Spinner walletSpinner = dialog.findViewById(R.id.transferWallet_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,
                new WalletNameList(wallets).getList());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        walletSpinner.setAdapter(adapter);

        Button transferBtn = dialog.findViewById(R.id.transferWallet_btn);
        transferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wallets.size() > 1){
                    int newWalletId = wallets.get(walletSpinner.getSelectedItemPosition()).getWalletId();
                    if (transfer(newWalletId) && newWalletId != walletId){
                        Toast.makeText(context, "Successfully transferred", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    else{
                        Toast.makeText(context, "Unable to transfer wallet", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(context, "No wallets available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView close = dialog.findViewById(R.id.transferWalletClose_imageView);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private boolean transfer(int newWalletId){
        try{
            ArrayList<Record> records = dbHandler.getWalletRecords(walletId);

            //change current walletId of records to new walletId
            for (Record record : records){
                record.setWalletId(newWalletId);
                dbHandler.updateRecord(record);
            }
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}

