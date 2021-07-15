package sg.edu.np.spendid.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

import sg.edu.np.spendid.Models.CartItem;
import sg.edu.np.spendid.Models.Category;
import sg.edu.np.spendid.Models.Currency;
import sg.edu.np.spendid.Models.Record;
import sg.edu.np.spendid.Models.ShoppingCart;
import sg.edu.np.spendid.Models.Wallet;

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
    public static final String COLUMN_RECORD_IMAGE = "Image";

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

    //Currencies
    public static final String TABLE_CURRENCY = "Currency";
    public static final String COLUMN_CURRENCY_FOREIGNCURRENCY = "ForeignCurrency";
    public static final String COLUMN_CURRENCY_RATE = "Rate";
    public static final String COLUMN_CURRENCY_DATE = "LastUpdated";

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
                COLUMN_RECORD_IMAGE+ " BLOB, " +
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

        String CREATE_CURRENCY_TABLE = "CREATE TABLE " + TABLE_CURRENCY +
                " (" + COLUMN_CURRENCY_FOREIGNCURRENCY + " TEXT PRIMARY KEY, " +
                COLUMN_CURRENCY_RATE + " REAL, " +
                COLUMN_CURRENCY_DATE + " TEXT)";
        db.execSQL(CREATE_WALLET_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_RECORD_TABLE);
        db.execSQL(CREATE_CART_TABLE);
        db.execSQL(CREATE_CARTITEM_TABLE);
        db.execSQL(CREATE_CURRENCY_TABLE);
        initCategories(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WALLET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARTITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENCY);
        onCreate(db);
    }

    private void initCategories(SQLiteDatabase db){
        String[] queries = new String[] {
                "INSERT INTO "+TABLE_CATEGORY+" VALUES (\'Shopping\', 1)",
                "INSERT INTO "+TABLE_CATEGORY+" VALUES (\'Food & Drinks\', 1)",
                "INSERT INTO "+TABLE_CATEGORY+" VALUES (\'Entertainment\', 1)",
                "INSERT INTO "+TABLE_CATEGORY+" VALUES (\'Leisure\', 1)",
                "INSERT INTO "+TABLE_CATEGORY+" VALUES (\'Transport\', 1)",
                "INSERT INTO "+TABLE_CATEGORY+" VALUES (\'Housing\', 1)",
                "INSERT INTO "+TABLE_CATEGORY+" VALUES (\'Vehicle\', 1)",
                "INSERT INTO "+TABLE_CATEGORY+" VALUES (\'Income\', 0)",
                "INSERT INTO "+TABLE_CATEGORY+" VALUES (\'Salary\', 0)",
                "INSERT INTO "+TABLE_CATEGORY+" VALUES (\'Bills\', 1)",
                "INSERT INTO "+TABLE_CATEGORY+" VALUES (\'Rental\', 1)",
                "INSERT INTO "+TABLE_CATEGORY+" VALUES (\'Education\', 1)",
                "INSERT INTO "+TABLE_CATEGORY+" VALUES (\'Healthcare\', 1)",
                "INSERT INTO "+TABLE_CATEGORY+" VALUES (\'Fitness\', 1)",
                "INSERT INTO "+TABLE_CATEGORY+" VALUES (\'Debt\', 1)",
                "INSERT INTO "+TABLE_CATEGORY+" VALUES (\'Stocks\', 1)",
                "INSERT INTO "+TABLE_CATEGORY+" VALUES (\'Others\', 1)"
        };
        for (String query : queries) {
            db.execSQL(query);
        }

    }


    //Wallet methods

    //Add wallet to DB
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

    //retrieve wallet by walletId
    public Wallet getWallet(int wId) {
        String query = "SELECT * FROM " + TABLE_WALLET + " WHERE " + COLUMN_WALLET_ID + " = " + wId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Wallet w;
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String des = cursor.getString(2);
            String cur = cursor.getString(3);
            String date = cursor.getString(4);
            w = new Wallet(id, name, des, cur, date);
        } else {
            w = null;
        }
        cursor.close();
        db.close();
        return w;
    }

    //returns the balance of a wallet retrieved from the respective records from the wallet - Glenn.
    public double getWalletTotal(int id) {
        String query = "SELECT * FROM " + TABLE_RECORD + " r INNER JOIN " + TABLE_CATEGORY + " c ON c." + COLUMN_CATEGORY_TITLE + " = r." + COLUMN_RECORD_CATEGORY + " WHERE r." + COLUMN_WALLET_ID + " = "+id;
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

    //returns an array of Wallets
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

    public boolean deleteWallet(int wId) {
        String query = "SELECT * FROM " + TABLE_WALLET + " WHERE " + COLUMN_WALLET_ID + " = " + wId;
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

    //delete all records associated with a specific wallet
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

    //get all records associated with a wallet
    public ArrayList<Record> getWalletRecords(int wId) {
        ArrayList<Record> recordList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_RECORD + " WHERE " + COLUMN_WALLET_ID + " = " + wId
                + " ORDER BY " + COLUMN_RECORD_DATECREATED + " DESC, "+ COLUMN_RECORD_TIMECREATED + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String des = cursor.getString(2);
            double amt = cursor.getDouble(3);
            String cat = cursor.getString(4);
            String dateCreated = cursor.getString(5);
            String timeCreated = cursor.getString(6);
            byte[] image = cursor.getBlob(7);
            int walletId = cursor.getInt(8);
            recordList.add(new Record(id, title, des, amt, cat, dateCreated, timeCreated, image, walletId));
        }
        cursor.close();
        db.close();
        return recordList;
    }


    //Category Methods

    //returns an array of categories
    public ArrayList<Category> getCategories() {
        ArrayList<Category> catList = new ArrayList<Category>();
        String query = "SELECT * FROM " + TABLE_CATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String title = cursor.getString(0);
            boolean exp = cursor.getInt(1)==1;
            Category cat = new Category(title, exp);
            catList.add(cat);
        }
        cursor.close();
        db.close();
        return catList;
    }

    //check if category is expense or income
    public boolean catIsExpense(String c){
        String query = "SELECT * FROM "+TABLE_CATEGORY+" WHERE "+COLUMN_CATEGORY_TITLE+" = \'"+c+"\'";
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


    //Record Methods

    //Add record to DB
    public void addRecord(Record r) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECORD_TITLE, r.getTitle());
        values.put(COLUMN_RECORD_DESCRIPTION, r.getDescription());
        values.put(COLUMN_RECORD_AMOUNT, r.getAmount());
        values.put(COLUMN_RECORD_CATEGORY, r.getCategory());
        values.put(COLUMN_RECORD_DATECREATED, r.getDateCreated());
        values.put(COLUMN_RECORD_TIMECREATED, r.getTimeCreated());
        values.put(COLUMN_RECORD_IMAGE, r.getImage());
        values.put(COLUMN_WALLET_ID, r.getWalletId());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_RECORD, null, values);
        db.close();
    }

    //Add records in bulk
    public void addRangeRecord(ArrayList<Record> recordArrayList){
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try{
            for (Record record : recordArrayList){
                ContentValues values = new ContentValues();
                values.put(COLUMN_RECORD_TITLE, record.getTitle());
                values.put(COLUMN_RECORD_DESCRIPTION, record.getDescription());
                values.put(COLUMN_RECORD_AMOUNT, record.getAmount());
                values.put(COLUMN_RECORD_CATEGORY, record.getCategory());
                values.put(COLUMN_RECORD_DATECREATED, record.getDateCreated());
                values.put(COLUMN_RECORD_TIMECREATED, record.getTimeCreated());
                values.put(COLUMN_RECORD_IMAGE, record.getImage());
                values.put(COLUMN_WALLET_ID, record.getWalletId());
                db.insert(TABLE_RECORD, null, values);
            }
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }
        db.close();
    }

    //return an array of all records sorted in descending order
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
            byte[] image = cursor.getBlob(7);
            int walletId = cursor.getInt(8);
            Record record = new Record(id, title, des, amt, cat, dateCreated, timeCreated, image, walletId);
            if (!history.containsKey(key)) {
                history.put(key, new ArrayList<Record>());
            }
            history.get(key).add(record);
        }
        cursor.close();
        db.close();
        return history;
    }

    //returns an array of all records
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
            byte[] image = cursor.getBlob(7);
            int walletId = cursor.getInt(8);
            Record record = new Record(id, title, des, amt, cat, dateCreated, timeCreated, image, walletId);
            recordList.add(record);
        }
        cursor.close();
        db.close();
        return recordList;
    }

    //get a specific record by recordId
    public Record getRecord(int rId) {
        String query = "SELECT * FROM " + TABLE_RECORD + " WHERE " + COLUMN_RECORD_ID + " = " + rId;
        SQLiteDatabase db = this.getReadableDatabase();
        Record r;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String des = cursor.getString(2);
            double amt = cursor.getDouble(3);
            String cat = cursor.getString(4);
            String dateCreated = cursor.getString(5);
            String timeCreated = cursor.getString(6);
            byte[] image = cursor.getBlob(7);
            int walletId = cursor.getInt(8);
            r = new Record(id, title, des, amt, cat, dateCreated, timeCreated, image, walletId);
        } else {
            r = null;
        }
        cursor.close();
        db.close();
        return r;
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
        values.put(COLUMN_RECORD_IMAGE, r.getImage());
        values.put(COLUMN_WALLET_ID, r.getWalletId());
        db.update(TABLE_RECORD, values, COLUMN_RECORD_ID + " =?", new String[]{String.valueOf(r.getId())});
        db.close();
    }

    public boolean deleteRecord(int rId) {
        String query = "SELECT * FROM " + TABLE_RECORD + " WHERE " + COLUMN_RECORD_ID + " = " + rId;
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


    //Statistics Methods

    //returns the total balance of all overall records and wallets combined
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

    //returns the income, expense and balance of all the wallets combined
    public HashMap<String, Double> getBalance(String MM) {
        HashMap<String, Double> bal = new HashMap<String, Double>();
        String query = "SELECT * FROM " + TABLE_RECORD + " r INNER JOIN " + TABLE_CATEGORY + " c ON c." + COLUMN_CATEGORY_TITLE + " = r." + COLUMN_RECORD_CATEGORY +
                " WHERE r." + COLUMN_RECORD_DATECREATED + " LIKE \'%-" + MM + "-%\'";
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

    //return an array of records grouped by its category for a specific date
    public HashMap<String, ArrayList<Record>> getGroupedTransaction(String date) {
        HashMap<String, ArrayList<Record>> group = new HashMap<String, ArrayList<Record>>();
        String query = "SELECT * FROM " + TABLE_RECORD + " r INNER JOIN " + TABLE_CATEGORY + " c ON c." + COLUMN_CATEGORY_TITLE + " = r." + COLUMN_RECORD_CATEGORY +
                " WHERE r." + COLUMN_WALLET_DATECREATED + " = " + "\'" + date + "\' ORDER BY " + COLUMN_RECORD_TIMECREATED + " DESC";
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
            byte[] image = cursor.getBlob(7);
            int walletId = cursor.getInt(8);
            Record record = new Record(id, title, des, amt, cat, dateCreated, timeCreated, image, walletId);
            if (!group.containsKey(key)) {
                group.put(key, new ArrayList<Record>());
            }
            group.get(key).add(record);
        }
        cursor.close();
        db.close();
        return group;
    }

    //Shopping List Methods

    //returns an array of shopping carts
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

    public boolean deleteShoppingCart(int cartId){
        String query = "SELECT * FROM " + TABLE_CART + " WHERE " + COLUMN_CART_ID + " = " + cartId;
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

    //Cart items Methods

    //returns an array of shopping cart items
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

    public boolean deleteCartItem(int itemId){
        String query = "SELECT * FROM " + TABLE_CARTITEM + " WHERE " + COLUMN_CARTITEM_ID + " = " + itemId;
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

    //Currency Methods

    //returns an array of currencies
    public void addCurrencies(ArrayList<Currency> currencyList){
        SQLiteDatabase db = this.getWritableDatabase();
        for (Currency c : currencyList){
            ContentValues values = new ContentValues();
            values.put(COLUMN_CURRENCY_FOREIGNCURRENCY, c.getForeign());
            values.put(COLUMN_CURRENCY_RATE, c.getRate());
            values.put(COLUMN_CURRENCY_DATE, c.getDate());
            db.insert(TABLE_CURRENCY, null, values);
        }
        db.close();
    }

    public Currency getCurrency(String c){
        String query = "SELECT * FROM " + TABLE_CURRENCY + " WHERE " + COLUMN_CURRENCY_FOREIGNCURRENCY + " = " + "\'"+c+"\'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Currency currency = new Currency();

        if (cursor.moveToFirst()){
            currency.setForeign(cursor.getString(0));
            currency.setRate(cursor.getDouble(1));
            currency.setDate(cursor.getString(2));
        }
        else{
            currency = null;
        }
        cursor.close();
        db.close();
        return currency;
    }

    //update all currencies in database based on currencyList
    public void updateCurrencies(ArrayList<Currency> currencyList){
        SQLiteDatabase db = this.getWritableDatabase();
        for (Currency c : currencyList){
            ContentValues values = new ContentValues();
            values.put(COLUMN_CURRENCY_RATE, c.getRate());
            values.put(COLUMN_CURRENCY_DATE, c.getDate());
            db.update(TABLE_CURRENCY, values, COLUMN_CURRENCY_FOREIGNCURRENCY + " =?", new String[]{String.valueOf(c.getForeign())});
        }
        db.close();
    }

    //returns an array of all currencies
    public ArrayList<Currency> getCurrencies(){
        ArrayList<Currency> currencyArrayList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_CURRENCY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String foreign = cursor.getString(0);
            double rate = cursor.getDouble(1);
            String date = cursor.getString(2);
            currencyArrayList.add(new Currency(foreign,rate,date));
        }
        cursor.close();
        db.close();
        return currencyArrayList;
    }

}


