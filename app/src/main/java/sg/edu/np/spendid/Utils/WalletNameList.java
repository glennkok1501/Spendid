package sg.edu.np.spendid.Utils;

import java.util.ArrayList;

import sg.edu.np.spendid.Models.Wallet;

/*
converts an array of wallet objects
to a string array of wallet names,
commonly used for choosing wallets using spinner
 */

public class WalletNameList {
    private ArrayList<Wallet> wallets;

    public WalletNameList(ArrayList<Wallet> wallets) {
        this.wallets = wallets;
    }

    public String[] getList(){
        String[] walletList = new String[wallets.size()];
        for (int i = 0; i < wallets.size(); i++){
            walletList[i] = wallets.get(i).getName();
        }
        return walletList;
    }
}
