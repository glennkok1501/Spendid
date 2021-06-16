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

    //

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WALLET_TABLE = "CREATE TABLE "+TABLE_WALLET+
                " ("+COLUMN_WALLET_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_WALLET_NAME+" TEXT, "+
                COLUMN_WALLET_DESCRIPTION+" TEXT, "+
                COLUMN_WALLET_CURRENCY+" TEXT, "+
                COLUMN_WALLET_DATECREATED+" TEXT)";
        String CREATE_CATEGORY_TABLE = "CREATE TABLE "+TABLE_CATEGORY+
                " ("+COLUMN_CATEGORY_TITLE+" TEXT PRIMARY KEY, "+
                COLUMN_CATEGORY_EXPENSE+" INTEGER)";
        String CREATE_RECORD_TABLE = "CREATE TABLE "+TABLE_RECORD+
                " ("+COLUMN_RECORD_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_RECORD_TITLE+" TEXT, "+
                COLUMN_RECORD_DESCRIPTION+" TEXT, "+
                COLUMN_RECORD_AMOUNT+" REAL, "+
                COLUMN_RECORD_CATEGORY+" TEXT, "+
                COLUMN_RECORD_DATECREATED+" TEXT, "+
                COLUMN_RECORD_TIMECREATED+" TEXT, " +
                COLUMN_WALLET_ID+" INTEGER," +
                "FOREIGN KEY ("+COLUMN_WALLET_ID+") REFERENCES "+TABLE_WALLET+"("+COLUMN_WALLET_ID+"), " +
                "FOREIGN KEY ("+COLUMN_RECORD_CATEGORY+") REFERENCES "+TABLE_CATEGORY+"("+COLUMN_CATEGORY_TITLE+"))";
        db.execSQL(CREATE_WALLET_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_RECORD_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_WALLET);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_RECORD);
        onCreate(db);
    }

    //Add wallet to DB - Glenn
    public void addWallet(Wallet w){
        ContentValues values = new ContentValues();
        values.put(COLUMN_WALLET_NAME, w.getName());
        values.put(COLUMN_RECORD_DESCRIPTION, w.getDescription());
        values.put(COLUMN_WALLET_CURRENCY, w.getCurrency());
        values.put(COLUMN_WALLET_DATECREATED, w.getDateCreated());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_WALLET,null, values);
        db.close();
    }

    //Add category to DB - Glenn
    public void addCategory(Category c){
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_TITLE, c.getTitle());
        values.put(COLUMN_CATEGORY_EXPENSE, c.isExpense());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_CATEGORY, null, values);
        db.close();
    }

    //Add record to DB - Glenn
    public void addRecord(Record r){
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECORD_TITLE, r.getTitle());
        values.put(COLUMN_RECORD_DESCRIPTION, r.getDescription());
        values.put(COLUMN_RECORD_AMOUNT, r.getAmount());
        values.put(COLUMN_RECORD_CATEGORY, r.getCategory());
        values.put(COLUMN_RECORD_DATECREATED, r.getDateCreated());
        values.put(COLUMN_RECORD_TIMECREATED, r.getTimeCreated());
        values.put(COLUMN_WALLET_ID, r.getWalletId());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_RECORD,null, values);
        db.close();
    }

    //returns the balance of a wallet retrieved from the respective records from the wallet - Glenn.
    public double getWalletTotal(int id){
        String query = "SELECT * FROM "+TABLE_RECORD+" r INNER JOIN "+TABLE_CATEGORY+" c ON c."+COLUMN_CATEGORY_TITLE+" = r."+COLUMN_RECORD_CATEGORY+" WHERE r."+COLUMN_WALLET_ID+" = \""+id+"\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        double income = 0;
        double expense = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            if (cursor.getInt(9) == 1){
                expense += cursor.getDouble(3);
            }
            else{
                income += cursor.getDouble(3);
            }
        }
        cursor.close();
        db.close();
        return income-expense;
    }

    public double getTotalBalance(){
        String query = "SELECT * FROM "+TABLE_RECORD+" r INNER JOIN "+TABLE_CATEGORY+" c ON c."+COLUMN_CATEGORY_TITLE+" = r."+COLUMN_RECORD_CATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        double income = 0;
        double expense = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            if (cursor.getInt(9) == 1){
                expense += cursor.getDouble(3);
            }
            else{
                income += cursor.getDouble(3);
            }
        }
        cursor.close();
        db.close();
        return income-expense;
    }

    //returns the income, expense and balance of all the wallets combined - Glenn
    public HashMap<String, Double> getBalance(String MM){
        HashMap<String, Double> bal = new HashMap<String, Double>();
        String query = "SELECT * FROM "+TABLE_RECORD+" r INNER JOIN "+TABLE_CATEGORY+" c ON c."+COLUMN_CATEGORY_TITLE+" = r."+COLUMN_RECORD_CATEGORY+
                " WHERE r."+COLUMN_RECORD_DATECREATED+" LIKE \"%-"+MM+"-%\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        double income = 0;
        double expense = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            if (cursor.getInt(9) == 1){
                expense += cursor.getDouble(3);
            }
            else{
                income += cursor.getDouble(3);
            }
        }
        cursor.close();
        db.close();
        bal.put("income", income);
        bal.put("expense", expense);
        bal.put("balance", income-expense);
        return bal;
    }

    //returns an array of Wallets - Glenn
    public ArrayList<Wallet> getWallets(){
        ArrayList<Wallet> walletList = new ArrayList<Wallet>();
        String query = "SELECT * FROM "+TABLE_WALLET;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            int id = Integer.parseInt(cursor.getString(0));
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

    public ArrayList<Category> getCategories(){
        ArrayList<Category> catList = new ArrayList<Category>();
        String query = "SELECT * FROM "+TABLE_CATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String title = cursor.getString(0);
            Boolean exp = cursor.getInt(1)==1;
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
        String query = "SELECT * FROM "+TABLE_RECORD+" r INNER JOIN "+TABLE_CATEGORY+" c ON c."+COLUMN_CATEGORY_TITLE+" = r."+COLUMN_RECORD_CATEGORY+" WHERE r."+COLUMN_WALLET_DATECREATED+" = " + "\""+date+"\"";
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

    public String lastMadeTransaction(int wId){
        String query = "SELECT * FROM "+TABLE_RECORD+" WHERE "+COLUMN_WALLET_ID+" = "+wId+" ORDER BY "+COLUMN_RECORD_DATECREATED+" DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            return cursor.getString(5);
        }
        else{
            return null;
        }
    }

}
