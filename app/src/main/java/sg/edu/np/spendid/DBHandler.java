package sg.edu.np.spendid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "spendidDB.db";
    private static final int DATABASE_VERSION = 1;

    //Wallet Table Attributes
    public static final String TABLE_WALLET = "Wallet";
    public static final String COLUMN_WALLET_ID = "WalletId";
    public static final String COLUMN_WALLET_NAME = "Name";
    public static final String COLUMN_WALLET_DESCRIPTION = "Description";
    public static final String COLUMN_WALLET_CURRENCY = "Currency";
    public static final String COLUMN_WALLET_DATECREATED = "DateCreated";

    //Category Table Attributes
    public static final String TABLE_CATEGORY = "Category";
    public static final String COLUMN_CATEGORY_TITLE = "Title";
    public static final String COLUMN_CATEGORY_EXPENSE = "Expense";

    //Record Table Attributes
    public static final String TABLE_RECORD = "Record";
    public static final String COLUMN_RECORD_ID = "RecordId";
    public static final String COLUMN_RECORD_TITLE = "Title";
    public static final String COLUMN_RECORD_DESCRIPTION = "Description";
    public static final String COLUMN_RECORD_AMOUNT = "Amount";
    public static final String COLUMN_RECORD_CATEGORY = "Category";
    public static final String COLUMN_RECORD_DATECREATED = "DateCreated";
    public static final String COLUMN_RECORD_TIMECREATED = "TimeCreated";

    //Shopping Cart Table Attributes
    public static final String TABLE_CART = "ShoppingCart";
    public static final String COLUMN_CART_ID = "CartId";
    public static final String COLUMN_CART_NAME = "Name";
    public static final String COLUMN_CART_DATECREATED = "DateCreated";

    //Shopping Cart Item Attributes
    public static final String TABLE_CARTITEM = "CartItem";
    public static final String COLUMN_CARTITEM_ID = "ItemId";
    public static final String COLUMN_CARTITEM_NAME = "Name";
    public static final String COLUMN_CARTITEM_AMOUNT = "Amount";
    public static final String COLUMN_CARTITEM_CHECK = "Checked";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WALLET_TABLE = "CREATE TABLE " + TABLE_WALLET +
                " (" + COLUMN_WALLET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WALLET_NAME + " TEXT, " +
                COLUMN_WALLET_DESCRIPTION + " TEXT, " +
                COLUMN_WALLET_CURRENCY + " TEXT, " +
                COLUMN_WALLET_DATECREATED + " TEXT)";
        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY +
                " (" + COLUMN_CATEGORY_TITLE + " TEXT PRIMARY KEY, " +
                COLUMN_CATEGORY_EXPENSE + " INTEGER)";
        String CREATE_RECORD_TABLE = "CREATE TABLE " + TABLE_RECORD +
                " (" + COLUMN_RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RECORD_TITLE + " TEXT, " +
                COLUMN_RECORD_DESCRIPTION + " TEXT, " +
                COLUMN_RECORD_AMOUNT + " REAL, " +
                COLUMN_RECORD_CATEGORY + " TEXT, " +
                COLUMN_RECORD_DATECREATED + " TEXT, " +
                COLUMN_RECORD_TIMECREATED + " TEXT, " +
                COLUMN_WALLET_ID + " INTEGER," +
                "FOREIGN KEY (" + COLUMN_WALLET_ID + ") REFERENCES " + TABLE_WALLET + "(" + COLUMN_WALLET_ID + "), " +
                "FOREIGN KEY (" + COLUMN_RECORD_CATEGORY + ") REFERENCES " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_TITLE + "))";
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART +
                " (" + COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CART_NAME + " TEXT, "+
                COLUMN_CART_DATECREATED+" TEXT)";
        String CREATE_CARTITEM_TABLE = "CREATE TABLE " + TABLE_CARTITEM +
                " (" + COLUMN_CARTITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CARTITEM_NAME + " TEXT, "+
                COLUMN_CARTITEM_AMOUNT + " REAL, "+
                COLUMN_CARTITEM_CHECK+" INTEGER, "+
                COLUMN_CART_ID+" INTEGER, "+
                "FOREIGN KEY ("+COLUMN_CART_ID+") REFERENCES "+TABLE_CART+" ("+COLUMN_CART_ID+"))";
        db.execSQL(CREATE_WALLET_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_RECORD_TABLE);
        db.execSQL(CREATE_CART_TABLE);
        db.execSQL(CREATE_CARTITEM_TABLE);

        String cat1 = "INSERT INTO "+TABLE_CATEGORY+" VALUES (\"Shopping\", 1)";
        String cat2 = "INSERT INTO "+TABLE_CATEGORY+" VALUES (\"Food & Drinks\", 1)";
        String cat3 = "INSERT INTO "+TABLE_CATEGORY+" VALUES (\"Entertainment\", 1)";
        String cat4 = "INSERT INTO "+TABLE_CATEGORY+" VALUES (\"Leisure\", 1)";
        String cat5 = "INSERT INTO "+TABLE_CATEGORY+" VALUES (\"Transport\", 1)";
        String cat6 = "INSERT INTO "+TABLE_CATEGORY+" VALUES (\"Housing\", 1)";
        String cat7 = "INSERT INTO "+TABLE_CATEGORY+" VALUES (\"Vehicle\", 1)";
        String cat8 = "INSERT INTO "+TABLE_CATEGORY+" VALUES (\"Income\", 0)";
        String cat9 = "INSERT INTO "+TABLE_CATEGORY+" VALUES (\"Salary\", 0)";
        String cat10 = "INSERT INTO "+TABLE_CATEGORY+" VALUES (\"Others\", 1)";
        db.execSQL(cat1);
        db.execSQL(cat2);
        db.execSQL(cat3);
        db.execSQL(cat4);
        db.execSQL(cat5);
        db.execSQL(cat6);
        db.execSQL(cat7);
        db.execSQL(cat8);
        db.execSQL(cat9);
        db.execSQL(cat10);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WALLET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORD);
        onCreate(db);
    }

    //Add wallet to DB - Glenn
    public void addWallet(Wallet w) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_WALLET_NAME, w.getName());
        values.put(COLUMN_RECORD_DESCRIPTION, w.getDescription());
        values.put(COLUMN_WALLET_CURRENCY, w.getCurrency());
        values.put(COLUMN_WALLET_DATECREATED, w.getDateCreated());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_WALLET, null, values);
        db.close();
    }

    //Add category to DB - Glenn
    public void addCategory(Category c) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_TITLE, c.getTitle());
        values.put(COLUMN_CATEGORY_EXPENSE, c.isExpense());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_CATEGORY, null, values);
        db.close();
    }

    //Add record to DB - Glenn
    public void addRecord(Record r) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECORD_TITLE, r.getTitle());
        values.put(COLUMN_RECORD_DESCRIPTION, r.getDescription());
        values.put(COLUMN_RECORD_AMOUNT, r.getAmount());
        values.put(COLUMN_RECORD_CATEGORY, r.getCategory());
        values.put(COLUMN_RECORD_DATECREATED, r.getDateCreated());
        values.put(COLUMN_RECORD_TIMECREATED, r.getTimeCreated());
        values.put(COLUMN_WALLET_ID, r.getWalletId());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_RECORD, null, values);
        db.close();
    }

    //returns the balance of a wallet retrieved from the respective records from the wallet - Glenn.
    public double getWalletTotal(int id) {
        String query = "SELECT * FROM " + TABLE_RECORD + " r INNER JOIN " + TABLE_CATEGORY + " c ON c." + COLUMN_CATEGORY_TITLE + " = r." + COLUMN_RECORD_CATEGORY + " WHERE r." + COLUMN_WALLET_ID + " = \"" + id + "\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        double income = 0;
        double expense = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if (cursor.getInt(9) == 1) {
                expense += cursor.getDouble(3);
            } else {
                income += cursor.getDouble(3);
            }
        }
        cursor.close();
        db.close();
        return income - expense;
    }

    public double getTotalBalance() {
        String query = "SELECT * FROM " + TABLE_RECORD + " r INNER JOIN " + TABLE_CATEGORY + " c ON c." + COLUMN_CATEGORY_TITLE + " = r." + COLUMN_RECORD_CATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        double income = 0;
        double expense = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if (cursor.getInt(9) == 1) {
                expense += cursor.getDouble(3);
            } else {
                income += cursor.getDouble(3);
            }
        }
        cursor.close();
        db.close();
        return income - expense;
    }

    //returns the income, expense and balance of all the wallets combined - Glenn
    public HashMap<String, Double> getBalance(String MM) {
        HashMap<String, Double> bal = new HashMap<String, Double>();
        String query = "SELECT * FROM " + TABLE_RECORD + " r INNER JOIN " + TABLE_CATEGORY + " c ON c." + COLUMN_CATEGORY_TITLE + " = r." + COLUMN_RECORD_CATEGORY +
                " WHERE r." + COLUMN_RECORD_DATECREATED + " LIKE \"%-" + MM + "-%\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        double income = 0;
        double expense = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if (cursor.getInt(9) == 1) {
                expense += cursor.getDouble(3);
            } else {
                income += cursor.getDouble(3);
            }
        }
        cursor.close();
        db.close();
        bal.put("income", income);
        bal.put("expense", expense);
        bal.put("balance", income - expense);
        return bal;
    }

    //returns an array of Wallets - Glenn
    public ArrayList<Wallet> getWallets() {
        ArrayList<Wallet> walletList = new ArrayList<Wallet>();
        String query = "SELECT * FROM " + TABLE_WALLET;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String des = cursor.getString(2);
            String cur = cursor.getString(3);
            String date = cursor.getString(4);
            Wallet wallet = new Wallet(id, name, des, cur, date);
            walletList.add(wallet);
        }
        cursor.close();
        db.close();
        return walletList;
    }

    public ArrayList<Category> getCategories() {
        ArrayList<Category> catList = new ArrayList<Category>();
        String query = "SELECT * FROM " + TABLE_CATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String title = cursor.getString(0);
            Boolean exp = cursor.getInt(1) == 1;
            Category cat = new Category(title, exp);
            catList.add(cat);
        }
        cursor.close();
        db.close();
        return catList;
    }

    //return an array of records grouped by its category for a specific date - Glenn
    public HashMap<String, ArrayList<Record>> getGroupedTransaction(String date) {
        HashMap<String, ArrayList<Record>> group = new HashMap<String, ArrayList<Record>>();
        String query = "SELECT * FROM " + TABLE_RECORD + " r INNER JOIN " + TABLE_CATEGORY + " c ON c." + COLUMN_CATEGORY_TITLE + " = r." + COLUMN_RECORD_CATEGORY +
                " WHERE r." + COLUMN_WALLET_DATECREATED + " = " + "\"" + date + "\" ORDER BY " + COLUMN_RECORD_TIMECREATED + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String key = cursor.getString(4);
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String des = cursor.getString(2);
            double amt = cursor.getDouble(3);
            String cat = cursor.getString(4);
            String dateCreated = cursor.getString(5);
            String timeCreated = cursor.getString(6);
            int walletId = cursor.getInt(7);
            Record record = new Record(id, title, des, amt, cat, dateCreated, timeCreated, walletId);
            if (!group.containsKey(key)) {
                group.put(key, new ArrayList<Record>());
            }
            group.get(key).add(record);
        }
        cursor.close();
        db.close();
        return group;
    }

    //returns the last transaction from the wallet - Glenn
    public String lastMadeTransaction(int wId) {
        String query = "SELECT * FROM " + TABLE_RECORD + " WHERE " + COLUMN_WALLET_ID + " = " + wId + " ORDER BY " + COLUMN_RECORD_DATECREATED + " DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            return cursor.getString(5);
        } else {
            return null;
        }
    }

    //return an array of all records sorted in descending order - Hong Li
    public HashMap<String, ArrayList<Record>> getRecordHistory() {
        HashMap<String, ArrayList<Record>> history = new HashMap<>();
        String query = "SELECT * FROM " + TABLE_RECORD + " ORDER BY " + COLUMN_RECORD_DATECREATED + " DESC, "
                + COLUMN_RECORD_TIMECREATED + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String key = cursor.getString(5);
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String des = cursor.getString(2);
            double amt = cursor.getDouble(3);
            String cat = cursor.getString(4);
            String dateCreated = cursor.getString(5);
            String timeCreated = cursor.getString(6);
            int walletId = cursor.getInt(7);
            Record record = new Record(id, title, des, amt, cat, dateCreated, timeCreated, walletId);
            if (!history.containsKey(key)) {
                history.put(key, new ArrayList<Record>());
            }
            history.get(key).add(record);
        }
        cursor.close();
        db.close();
        return history;
    }

    public ArrayList<Record> getAllRecords() {
        ArrayList<Record> recordList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_RECORD + " ORDER BY " + COLUMN_RECORD_DATECREATED + " DESC, "
                + COLUMN_RECORD_TIMECREATED + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String des = cursor.getString(2);
            double amt = cursor.getDouble(3);
            String cat = cursor.getString(4);
            String dateCreated = cursor.getString(5);
            String timeCreated = cursor.getString(6);
            int walletId = cursor.getInt(7);
            Record record = new Record(id, title, des, amt, cat, dateCreated, timeCreated, walletId);
            recordList.add(record);
        }
        cursor.close();
        db.close();
        return recordList;
    }

    public Wallet getWallet(int wId) {
        String query = "SELECT * FROM " + TABLE_WALLET + " WHERE " + COLUMN_WALLET_ID + " = " + wId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String des = cursor.getString(2);
            String cur = cursor.getString(3);
            String date = cursor.getString(4);
            return new Wallet(id, name, des, cur, date);
        } else {
            return null;
        }
    }

    public Record getRecord(int rId) {
        String query = "SELECT * FROM " + TABLE_RECORD + " WHERE " + COLUMN_RECORD_ID + " = " + rId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String des = cursor.getString(2);
            double amt = cursor.getDouble(3);
            String cat = cursor.getString(4);
            String dateCreated = cursor.getString(5);
            String timeCreated = cursor.getString(6);
            int walletId = cursor.getInt(7);
            return new Record(id, title, des, amt, cat, dateCreated, timeCreated, walletId);
        } else {
            return null;
        }
    }

    public void updateRecord(Record r) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        values.put(COLUMN_RECORD_ID, r.getId());
        values.put(COLUMN_RECORD_TITLE, r.getTitle());
        values.put(COLUMN_RECORD_DESCRIPTION, r.getDescription());
        values.put(COLUMN_RECORD_AMOUNT, r.getAmount());
        values.put(COLUMN_RECORD_CATEGORY, r.getCategory());
        values.put(COLUMN_RECORD_DATECREATED, r.getDateCreated());
        values.put(COLUMN_RECORD_TIMECREATED, r.getTimeCreated());
        values.put(COLUMN_WALLET_ID, r.getWalletId());
        db.update(TABLE_RECORD, values, COLUMN_RECORD_ID + " =?", new String[]{String.valueOf(r.getId())});
        db.close();
    }

    public void updateWallet(Wallet w) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        values.put(COLUMN_WALLET_ID, w.getWalletId());
        values.put(COLUMN_WALLET_NAME, w.getName());
        values.put(COLUMN_WALLET_DESCRIPTION, w.getDescription());
        values.put(COLUMN_WALLET_DATECREATED, w.getDateCreated());
        values.put(COLUMN_WALLET_CURRENCY, w.getCurrency());
        db.update(TABLE_WALLET, values, COLUMN_WALLET_ID + " =?", new String[]{String.valueOf(w.getWalletId())});
        db.close();
    }

    public boolean deleteRecord(int rId) {
        String query = "SELECT * FROM " + TABLE_RECORD + " WHERE " + COLUMN_RECORD_ID + " = \"" + rId + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        boolean deleted;
        if (cursor.moveToFirst()) {
            db.delete(TABLE_RECORD, COLUMN_RECORD_ID + "= ?", new String[]{String.valueOf(rId)});
            deleted = true;
        } else {
            deleted = false;
        }
        cursor.close();
        db.close();
        return deleted;
    }

    public boolean deleteWallet(int wId) {
        String query = "SELECT * FROM " + TABLE_WALLET + " WHERE " + COLUMN_WALLET_ID + " = \"" + wId + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        boolean deleted;
        if (cursor.moveToFirst()) {
            db.delete(TABLE_WALLET, COLUMN_WALLET_ID + "= ?", new String[]{String.valueOf(wId)});
            deleted = true;
        } else {
            deleted = false;
        }
        cursor.close();
        db.close();
        return deleted;
    }

    public void deleteWalletRecords(int wId) {
        String query = "SELECT * FROM " + TABLE_RECORD;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            db.delete(TABLE_RECORD, COLUMN_WALLET_ID + "= ?", new String[]{String.valueOf(wId)});
        }
        cursor.close();
        db.close();
    }

    public boolean catIsExpense(String c){
        String query = "SELECT * FROM "+TABLE_CATEGORY+" WHERE "+COLUMN_CATEGORY_TITLE+" = \""+c+"\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        boolean isExpense;
        if (cursor.moveToFirst()){
            isExpense = cursor.getInt(1) == 1;
        }
        else{
            isExpense = false;
        }
        cursor.close();
        db.close();
        return isExpense;
    }


    //Shopping List Feature
    public ArrayList<ShoppingCart> getShoppingCarts(){
        ArrayList<ShoppingCart> cartList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String date = cursor.getString(2);
            ShoppingCart shoppingCart = new ShoppingCart(id, name, date);
            cartList.add(shoppingCart);
        }
        cursor.close();
        db.close();
        return cartList;
    }

    public void addShoppingCart(ShoppingCart s){
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_NAME, s.getName());
        values.put(COLUMN_CART_DATECREATED, s.getDateCreated());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_CART, null, values);
        db.close();
    }

    public void addCartItem(CartItem c){
        ContentValues values = new ContentValues();
        values.put(COLUMN_CARTITEM_NAME, c.getName());
        values.put(COLUMN_CARTITEM_AMOUNT, c.getAmount());
        values.put(COLUMN_CARTITEM_CHECK, c.isCheck());
        values.put(COLUMN_CART_ID, c.getCartId());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_CARTITEM, null, values);
        db.close();
    }

    public ArrayList<CartItem> getCartItems(int cartId){
        ArrayList<CartItem> itemsList = new ArrayList<>();
        String query = "SELECT * FROM "+TABLE_CARTITEM+" WHERE "+COLUMN_CART_ID+" = "+cartId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            double amt = cursor.getDouble(2);
            boolean check = cursor.getInt(3) == 1;
            CartItem cartItem = new CartItem(id, name, amt, check, cartId);
            itemsList.add(cartItem);
        }
        cursor.close();
        db.close();
        return itemsList;
    }

    public boolean deleteCartItem(int itemId){
        String query = "SELECT * FROM " + TABLE_CARTITEM + " WHERE " + COLUMN_CARTITEM_ID + " = \"" + itemId + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        boolean deleted;
        if (cursor.moveToFirst()) {
            db.delete(TABLE_CARTITEM, COLUMN_CARTITEM_ID + "= ?", new String[]{String.valueOf(itemId)});
            deleted = true;
        } else {
            deleted = false;
        }
        cursor.close();
        db.close();
        return deleted;
    }

    public void updateCartItem(CartItem c) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CARTITEM_NAME, c.getName());
        values.put(COLUMN_CARTITEM_AMOUNT, c.getAmount());
        values.put(COLUMN_CARTITEM_CHECK, c.isCheck());
        values.put(COLUMN_CART_ID, c.getCartId());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_CARTITEM, values, COLUMN_CARTITEM_ID + " =?", new String[]{String.valueOf(c.getItemId())});
        db.close();
    }

    public void deleteCartItems(int cartId){
        String query = "SELECT * FROM " + TABLE_CARTITEM;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            db.delete(TABLE_CARTITEM, COLUMN_CART_ID + "= ?", new String[]{String.valueOf(cartId)});
        }
        cursor.close();
        db.close();
    }
    public boolean deleteShoppingCart(int cartId){
        String query = "SELECT * FROM " + TABLE_CART + " WHERE " + COLUMN_CART_ID + " = \"" + cartId + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        boolean deleted;
        if (cursor.moveToFirst()) {
            db.delete(TABLE_CART, COLUMN_CART_ID + "= ?", new String[]{String.valueOf(cartId)});
            deleted = true;
        } else {
            deleted = false;
        }
        cursor.close();
        db.close();
        return deleted;
    }
}
