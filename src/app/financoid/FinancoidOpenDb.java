package app.financoid;

import java.io.*;
import java.util.ArrayList;

import org.json.JSONArray;

import android.content.Context;
import android.database.SQLException;
import android.database.Cursor;
import android.database.sqlite.*;
import android.content.ContentValues;

public class FinancoidOpenDb extends SQLiteOpenHelper{
		 
	    //The Android's default system path of your application database.
	    private static String DB_PATH = "/data/data/app.financoid/databases/";
	 
	    private static String DB_NAME = "financoiddb";
	 
	    private static SQLiteDatabase dbConn; 
	 
	    private final Context myContext;
	    
	    private static final String TABLE_TRANSACTIONS = null;
	    private static final String TABLE_ACCOUNTS = null;
	    
	    private static final String TABLE_TRANSACTIONS_ID = "_id";
	    private static final String TABLE_TRANSACTIONS_TITLE = "transaction_title";
	    private static final String TABLE_TRANSACTIONS_VALUE = "transaction_value";
	    private static final String TABLE_TRANSACTIONS_CATEGORY = "transaction_category";
	    private static final String TABLE_TRANSACTIONS_DATE = "transactions_date";
	    
	    private static final String TABLE_ACCOUNTS_ID = "_id";
	    private static final String TABLE_ACCOUNTS_TITLE = "account_name";
	    private static final String TABLE_ACCOUNTS_BALANCE = "account_balance";
	    private static final String TABLE_ACCOUNTS_BUDGET = "account_monthly_budget";
	 
	    /**
	     * Constructor
	     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
	     * @param context
	     */
	    public FinancoidOpenDb(Context context) {
	 
	    	super(context, DB_NAME, null, 1);
	        this.myContext = context;
	    }	
	 
	    /**
	     * Creates a empty database on the system and rewrites it with your own database.
	     * */
	    public void createDataBase() throws IOException{
	 
	    	boolean dbExist = checkDataBase();
	 
	    	if(dbExist){
	    		//do nothing - database already exist
	    	}else{
	 
	    		//By calling this method and empty database will be created into the default system path
	               //of your application so we are gonna be able to overwrite that database with our database.
	        	this.getWritableDatabase();
	 
	        	try {
	 
	    			copyDataBase();
	    			
	 
	    		} catch (IOException e) {
	 
	        		throw new Error("Error copying database");
	 
	        	}
	    	}
	 
	    }
	 
	    /**
	     * Check if the database already exist to avoid re-copying the file each time you open the application.
	     * @return true if it exists, false if it doesn't
	     */
	    private boolean checkDataBase(){
	 
	    	SQLiteDatabase checkDB = null;
	 
	    	try{
	    		String myPath = DB_PATH + DB_NAME;
	    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	 
	    	}catch(SQLiteException e){
	 
	    		//database does't exist yet.
	 
	    	}
	 
	    	if(checkDB != null){
	 
	    		checkDB.close();
	 
	    	}
	 
	    	return checkDB != null ? true : false;
	    }
	 
	    /**
	     * Copies your database from your local assets-folder to the just created empty database in the
	     * system folder, from where it can be accessed and handled.
	     * This is done by transfering bytestream.
	     * */
	    private void copyDataBase() throws IOException{
	 
	    	//Open your local db as the input stream
	    	InputStream myInput = myContext.getAssets().open(DB_NAME);
	 
	    	// Path to the just created empty db
	    	String outFileName = DB_PATH + DB_NAME;
	 
	    	//Open the empty db as the output stream
	    	OutputStream myOutput = new FileOutputStream(outFileName);
	 
	    	//transfer bytes from the inputfile to the outputfile
	    	byte[] buffer = new byte[1024];
	    	int length;
	    	while ((length = myInput.read(buffer))>0){
	    		myOutput.write(buffer, 0, length);
	    	}
	 
	    	//Close the streams
	    	myOutput.flush();
	    	myOutput.close();
	    	myInput.close();
	 
	    }
	    
	    public long insertEntry(String dbTable, ArrayList<String> key, ArrayList<String> value) {
			//String timeStamp = new Timestamp(Calendar.getInstance().getTimeInMillis()).toString();
			ContentValues contentValues = new ContentValues();
			for(int i = 0; key.size() > i; i++){
				contentValues.put(key.get(i), value.get(i));
			}
			//contentValues.put(KEY_TIMESTAMP, timeStamp);
	    	
			return dbConn.insert(dbTable, null, contentValues);
		}
	    
	    public Cursor getAllEntries(String dbTable, String[] columns, String selection, String[] selectionArgs,
				String groupBy, String having, String sortBy, String sortOption) {
			return dbConn.query(dbTable, columns, selection, selectionArgs, groupBy,
					having, sortBy + " " + sortOption);
		}


	 
	    public SQLiteDatabase openDataBase() throws SQLException{
	 
	    	//Open the database
	        String myPath = DB_PATH + DB_NAME;
	    	dbConn = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	    	
	    	return dbConn;
	 
	    }
	 
	    @Override
		public synchronized void close() {
	 
	    	    if(dbConn != null)
	    		    dbConn.close();
	 
	    	    super.close();
	 
		}
	 
		@Override
		public void onCreate(SQLiteDatabase db) {
	 
		}
	 
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	 
		}
		
	    /*
	     * FUNCTION: public void connectToDb()
	     * 
	     * DESCRIPTION: Attempts to create the database from a sample located in the assets directory
	     * 				if a database is not present.
	     * 				If a database exists for this given emulator, it opens a connection with the DB.
	     * 
	     * 		INPUTS: (none)
	     * 		OUTPUTS: SQLiteDatabase object
	     * 
	     * 
	     */
	    public static SQLiteDatabase connectToDb(Context context, SQLiteDatabase dbConn) {
	    	
	    	FinancoidOpenDb dbHelper = new FinancoidOpenDb(context);
	    	try {
	    		 
		        	dbHelper.createDataBase();
		 
		 	} catch (IOException ioe) {
		 
		 		throw new Error("Unable to create database");
		 
		 	}
		 
		 	try {
		 
		 		dbConn = dbHelper.openDataBase();
		 
		 	}catch(SQLException sqle){
		 
		 		throw sqle;
		 
		 	}
		 	
		 	return dbConn;


	    }//end of function connectToDb()
	    
	    
	    /*
	     * Builds this month's statistics returning an array of values that is
	     * used with the JSON object in the StatisticsActivity view.
	     * 
	     */
	    public static JSONArray buildThisMonthsStats() {
			
	    	return null;
	    	
	    }
	    
	    public static JSONArray buildMixedMonthStats() {
	    	
	    	return null;
	    	
	    }
	    
	    /*
	     * FUNCTION: public static JSONArray buildCurrentBalanceStats()
	     * 
	     * DESCRIPTION: Fetches current balance data from the database and returns it
	     * 				in a JSONArray form to be fed into the flot library for display
	     * 				in the pie chart.
	     * 
	     * 		INPUTS: (none)
	     * 		OUTPUTS: JSONArray
	     * 
	     */
	    public static JSONArray buildCurrentBalanceStats() {
			Cursor cur;
			
	    	cur = dbConn.query(TABLE_ACCOUNTS, new String[] { "account_name", "account_balance", "account_monthly_budget", "account_date", "account_extra_date" }, null, null, null, null,null);
	    	
	    	return null;
	    	
	    }
	}


