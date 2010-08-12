package app.financoid;

import java.io.*;
import java.util.ArrayList;

import android.content.Context;
import android.database.SQLException;
import android.database.Cursor;
import android.database.sqlite.*;
import android.content.ContentValues;

/*
public class FinancoidOpenDb extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "financoid.db";
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
		
	}*/

public class FinancoidOpenDb extends SQLiteOpenHelper{
		 
	    //The Android's default system path of your application database.
	    private static String DB_PATH = "/data/data/app.financoid/databases/";
	 
	    private static String DB_NAME = "financoiddb";
	 
	    private SQLiteDatabase myDataBase; 
	 
	    private final Context myContext;
	 
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
	    	
			return myDataBase.insert(dbTable, null, contentValues);
		}
	    
	    public Cursor getAllEntries(String dbTable, String[] columns, String selection, String[] selectionArgs,
				String groupBy, String having, String sortBy, String sortOption) {
			return myDataBase.query(dbTable, columns, selection, selectionArgs, groupBy,
					having, sortBy + " " + sortOption);
		}


	 
	    public SQLiteDatabase openDataBase() throws SQLException{
	 
	    	//Open the database
	        String myPath = DB_PATH + DB_NAME;
	    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	    	
	    	return myDataBase;
	 
	    }
	 
	    @Override
		public synchronized void close() {
	 
	    	    if(myDataBase != null)
	    		    myDataBase.close();
	 
	    	    super.close();
	 
		}
	 
		@Override
		public void onCreate(SQLiteDatabase db) {
	 
		}
	 
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	 
		}	 
	}


