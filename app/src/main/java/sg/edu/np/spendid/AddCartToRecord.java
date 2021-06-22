package sg.edu.np.spendid;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddCartToRecord {
    private Context context;
    private double exchangeRate;
    private String baseCurrency;
    private int cartId;
    private Wallet wallet;
    private ArrayList<CartItem> cartItems;
    private DBHandler dbHandler;
    private String[] walletList;
    private ArrayList<Wallet> walletArrayList;
    private EditText name, amt;
    private double amount;
    private String des;
    private DecimalFormat df2 = new DecimalFormat("#0.00");

    public AddCartToRecord(Context context, String baseCurrency, int cartId) {
        this.context = context;
        this.baseCurrency = baseCurrency.toLowerCase();
        this.cartId = cartId;
        des = "";
        dbHandler = new DBHandler(context, null, null, 1);
        cartItems = dbHandler.getCartItems(cartId);
        walletArrayList = dbHandler.getWallets();
        walletList = new String[walletArrayList.size()];
        for(int i = 0; i < walletList.length; i++){
            walletList[i] = walletArrayList.get(i).getName();
        }
    }

    public void add(){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.cart_to_wallet_layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);

        name = dialog.findViewById(R.id.cartToRecordName_editText);
        amt = dialog.findViewById(R.id.cartToRecordAmt_editText);
        ImageView close = dialog.findViewById(R.id.cartToRecordClose_imageView);
        Button addBtn = dialog.findViewById(R.id.cartToRecord_btn);
        Spinner spinner = dialog.findViewById(R.id.cartToRecordWallet_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, walletList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        String selected = spinner.getSelectedItem().toString();
        wallet = findWallet(selected);
        for (CartItem c : cartItems){
            des += c.getName()+" "+c.getAmount()+" "+wallet.getCurrency();
            if (c.isCheck()){
                des +=" - Bought\n";
                amount += c.getAmount();
            }
            else{
                des +=" - Not Bought\n";
            }
        }
        amt.setText(df2.format(amount));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selected = spinner.getSelectedItem().toString();
                wallet = findWallet(selected);
                if (!baseCurrency.equals(wallet.getCurrency().toLowerCase())){
                    CurrencyAPI currencyAPI = new CurrencyAPI(context, wallet.getCurrency().toLowerCase(), baseCurrency);
                    currencyAPI.setAmt(amt);
                    currencyAPI.setForFixedAmt(amount);
                    currencyAPI.call(true);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // pass
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler.addRecord(createRecordFromCart());
                Toast.makeText(context, "Transaction Added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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

    private Wallet findWallet(String selected){
        Wallet wallet = null;
        for (Wallet w : walletArrayList){
            if (selected.equals(w.getName())){
                wallet = w;
                break;
            }
        }
        return wallet;
    }

    private Record createRecordFromCart(){
        String title = name.getText().toString();
        //TO DO error checking

        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String date = dateFormat.format(currentTime.getTime());
        String time = timeFormat.format(currentTime.getTime());
        Record  r = new Record(title, des, Double.parseDouble(amt.getText().toString()), "Shopping", date, time, wallet.getWalletId());
        return r;
    }
}
