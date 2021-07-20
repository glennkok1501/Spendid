package sg.edu.np.spendid.ShoppingList;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import sg.edu.np.spendid.Dialogs.CurrencyConvertDialog;
import sg.edu.np.spendid.Database.DBHandler;
import sg.edu.np.spendid.Models.CartItem;
import sg.edu.np.spendid.R;
import sg.edu.np.spendid.Models.Record;
import sg.edu.np.spendid.Models.Wallet;

//Class to compile cart items and to create a transaction

public class AddCartToRecord {
    private Context context;
    private StringBuilder des;
    private Wallet wallet;
    private ArrayList<CartItem> cartItems;
    private DBHandler dbHandler;
    private String[] walletList;
    private ArrayList<Wallet> walletArrayList;
    private EditText name, amt;
    private double amount;
    private Dialog dialog;
    private final DecimalFormat df2 = new DecimalFormat("#0.00");

    public AddCartToRecord(Context context, int cartId, DBHandler dbHandler) {
        this.context = context;
        this.dbHandler = dbHandler;

        //initialize description
        des = new StringBuilder();

        //Get an array list of cart items based on the cart Id
        cartItems = dbHandler.getCartItems(cartId);

        //Get all wallets
        walletArrayList = dbHandler.getWallets();
        walletList = getWalletList();
    }

    public void add(){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.cart_to_wallet_layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);

        name = dialog.findViewById(R.id.cartToRecordName_editText);
        amt = dialog.findViewById(R.id.cartToRecordAmt_editText);
        ImageView close = dialog.findViewById(R.id.cartToRecordClose_imageView);
        Button addBtn = dialog.findViewById(R.id.cartToRecord_btn);
        Spinner spinner = dialog.findViewById(R.id.cartToRecordWallet_spinner);

        //initiate spinner with string array of wallets for user to select
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, walletList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //get wallet object on first selected choice based on name
        if (walletArrayList.size() > 0){

            //get wallet object based on selected index
            wallet = walletArrayList.get(spinner.getSelectedItemPosition());

            //check if wallet is not SGD then prompt for exchange
            checkCurrency();

            //reassign selected wallet when spinner index change
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    checkCurrency();
                    wallet = walletArrayList.get(spinner.getSelectedItemPosition());
                }
                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    //pass
                }
            });
        }
        else{
            wallet = null;
            Toast.makeText(context, "No wallets available", Toast.LENGTH_SHORT).show();
        }

        //gather information about the cart such as cost, items, and bought or not bought
        initDetails();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Record r = createRecordFromCart();
                //insert record if valid
                if (r != null){
                    dbHandler.addRecord(r);
                    Toast.makeText(context, "Transaction Added", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //create a string array of wallet names for spinner to use
    private String[] getWalletList(){
        String[] walletList = new String[walletArrayList.size()];
        for (int i = 0; i < walletArrayList.size(); i++){
            walletList[i] = walletArrayList.get(i).getName();
        }
        return walletList;
    }

    //calculate information to create a transaction from cart items
    private void initDetails(){
        Resources res = context.getResources();
        for (CartItem c : cartItems){
            //add cart item into description with amount
            des.append(String.format("%s %s (%s %s)", Html.fromHtml(res.getString(R.string.dot)), c.getName(), df2.format(c.getAmount()), wallet.getCurrency()));

            //check if item is bought
            if (c.isCheck()){
                des.append(String.format("%s\n", Html.fromHtml(res.getString(R.string.tick))));
                amount += c.getAmount();
            }
            //item not bought
            else{
                des.append(String.format("%s\n", Html.fromHtml(res.getString(R.string.cross))));
            }
            des.append("\n");
        }
        amt.setText(df2.format(amount));
    }

    //prompt currency exchange dialog if wallet currency is not in SGD
    private void checkCurrency(){
        if (!wallet.getCurrency().equals(context.getString(R.string.baseCurrency))){
            CurrencyConvertDialog currencyConvertDialog = new CurrencyConvertDialog(context, wallet.getCurrency().toLowerCase(), dbHandler);
            currencyConvertDialog.setAmt(amt);
            currencyConvertDialog.setForFixedAmt(amount);
            currencyConvertDialog.show();
        }
    }

    //create transaction based on values calculated
    private Record createRecordFromCart(){
        String title = name.getText().toString();
        //validate name
        if (title.length() == 0 || title.length() > 15){
            TextInputLayout editLayout = dialog.findViewById(R.id.cartToRecordName_layout);
            editLayout.setError("Invalid Name");
            return null;
        }
        else if (wallet == null){
            return null;
        }
        else{
            Calendar currentTime = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String date = dateFormat.format(currentTime.getTime());
            String time = timeFormat.format(currentTime.getTime());
            String a = amt.getText().toString();
            //validate if amount is blank
            if (a.length() == 0){
                a = "0";
            }
            return new Record(title, des.toString(), Double.parseDouble(a), "Shopping", date, time, null,  wallet.getWalletId());
        }
    }
}
