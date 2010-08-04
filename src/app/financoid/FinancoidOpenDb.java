package app.financoid;

import android.content.Context;
import android.database.sqlite.*; 

public class FinancoidOpenDb extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "FinancoidDb";
    private static final String FINANCOID_TABLE_NAME = "transactions";
	private static int TRANSACTION_ID;
	private static String TRANSACTION_NAME;
    private static final String FINANCOID_TABLE_CREATE =
                "CREATE TABLE " + FINANCOID_TABLE_NAME + " (" +
                TRANSACTION_ID + " TEXT, " +
                TRANSACTION_NAME + " TEXT);";

    FinancoidOpenDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FINANCOID_TABLE_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}


}
