package sg.edu.np.spendid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "spendidDB.db";
    private static final int DATABASE_VERSION = 1;

    //Wallet Table Attributes
    public static final String TABLE_WALLET = "Wallet";
    public static final String COLUMN_WALLET_ID = "WalletId";
    public static final String COLUMN_WALLET_NAME = "Name";
    public static final String COLUMN_WALLET_DESCRIPTION = "Description";
    public static final String COLUMN_WALLET_CURRENCY = "Currency";
    public static final String COLUMN_WALLET_DATACREATED = "DateCreated";

    //Record Table Attributes
    public static final String TABLE_RECORD = "Record";
    public static final String COLUMN_RECORD_ID = "RecordId";
    public static final String COLUMN_RECORD_TITLE = "Title";
    public static final String COLUMN_RECORD_DESCRIPTION = "Description";
    public static final String COLUMN_RECORD_AMOUNT = "Currency";
    public static final String COLUMN_RECORD_CATEGORY = "Category";
    public static final String COLUMN_RECORD_DATACREATED = "DateCreated";


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
                COLUMN_WALLET_DATACREATED+" INTEGER DEFAULT CURRENT_TIMESTAMP)";
        String CREATE_RECORD_TABLE = "CREATE TABLE "+TABLE_RECORD+
                " ("+COLUMN_RECORD_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_RECORD_TITLE+" TEXT, "+
                COLUMN_RECORD_DESCRIPTION+" TEXT, "+
                COLUMN_RECORD_AMOUNT+" REAL, "+
                COLUMN_RECORD_CATEGORY+" TEXT, "+
                COLUMN_RECORD_DATACREATED+" INTEGER DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(CREATE_WALLET_TABLE);
        db.execSQL(CREATE_RECORD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_WALLET);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_RECORD);
        onCreate(db);
    }
}
