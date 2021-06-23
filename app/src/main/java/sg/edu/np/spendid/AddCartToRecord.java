package sg.edu.np.spendid;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddCartToRecord {
    private Context context;
    private String baseCurrency, des;
    private Wallet wallet;
    private ArrayList<CartItem> cartItems;
    private DBHandler dbHandler;
    private String[] walletList;
    private ArrayList<Wallet> walletArrayList;
    private EditText name, amt;
    private double amount;
    private Dialog dialog;
    private final DecimalFormat df2 = new DecimalFormat("#0.00");

    public AddCartToRecord(Context context, String baseCurrency, int cartId) {
        this.context = context;
        this.baseCurrency = baseCurrency.toLowerCase();
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, walletList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        wallet = findWallet(spinner.getSelectedItem().toString());
        initDetails();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                wallet = findWallet(spinner.getSelectedItem().toString());
                checkCurrency();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // pass
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Record r = createRecordFromCart();
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

    private void initDetails(){
        Resources res = context.getResources();
        for (CartItem c : cartItems){
            des += String.format("%s %s (%s %s)", Html.fromHtml(res.getString(R.string.dot)), c.getName(), df2.format(c.getAmount()), wallet.getCurrency());
            if (c.isCheck()){
                des += String.format("%s\n", Html.fromHtml(res.getString(R.string.tick)));
                amount += c.getAmount();
            }
            else{
                des += String.format("%s\n", Html.fromHtml(res.getString(R.string.cross)));
            }
            des+="\n";
        }
        amt.setText(df2.format(amount));
    }

    private void checkCurrency(){
        if (!baseCurrency.equals(wallet.getCurrency().toLowerCase())){
            CurrencyAPI currencyAPI = new CurrencyAPI(context, wallet.getCurrency().toLowerCase(), baseCurrency);
            currencyAPI.setAmt(amt);
            currencyAPI.setForFixedAmt(amount);
            currencyAPI.call(true);
        }
    }

    private Record createRecordFromCart(){
        String title = name.getText().toString();
        if (title.length() == 0 || title.length() > 15){
            TextInputLayout editLayout = dialog.findViewById(R.id.cartToRecordName_layout);
            editLayout.setError("Invalid Name");
            return null;
        }
        else{
            Calendar currentTime = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String date = dateFormat.format(currentTime.getTime());
            String time = timeFormat.format(currentTime.getTime());
            String a = amt.getText().toString();
            if (a.length() == 0){
                a = "0";
            }
            return new Record(title, des, Double.parseDouble(a), "Shopping", date, time, wallet.getWalletId());
        }
        //TO DO error checking

    }
}
