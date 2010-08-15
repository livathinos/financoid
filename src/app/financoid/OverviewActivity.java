package app.financoid;

import java.io.IOException;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;

public class OverviewActivity extends Activity {
    
	private ListView ovListView;
	/*private DateFormat ovDateFormatter = null;
	
	private static final String abHeader = "Account balance";
	private static final String abIncome = "Income";
	private static final String abExpenditure = "Expenditure";
	private static final String abRemaining = "Remaining";
	private static final String abStatus = "Status";
	
	private static final String mbHeader = "Monthly balance";
	private static final String mbIncome = "Income";
	private static final String mbExpenditure = "Expenditure";
	private static final String mbRemaining = "Remaining";
	private static final String mbStatus = "Status";*/
	
	public static final String KEY_TITLE="transaction_title";
	public static final String KEY_VALUE="transaction_value";
	public static final String KEY_PREFIX="transaction_prefix";
	public static final String KEY_CATEGORY = "transaction_category";
	public static final String KEY_DATE = "transaction_date";
	public static final String KEY_ROWID="_id";
	
	private SQLiteDatabase dbConn;
	private Cursor balanceCursor;
	
	//private static final int PROGRESS = 0x1;
	private ProgressBar abProgress;
	private int abProgressStatus = 0;
	
	//private Handler abHandler = new Handler();
	
	private ProgressBar mbProgress;
	private int mbProgressStatus = 0;
	
	//private Handler mbHandler = new Handler();
	
	/*
	 * FUNCTION: public void onCreate(Bundle savedInstanceState)
	 * 
	 * DESCRIPTION: Override of native function. Called when the application
	 * 				is first initialized.
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * 
	 * 		INPUTS: Bundle savedInstanceState
	 * 		OUTPUTS: (none)
	 * 
	 */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview);
        
        dbConn = connectToDb();
        
        displayAccountBalance();
        displayMonthlyBudgetFlow();
        
        ovListView = (ListView) findViewById(android.R.id.list);

        balanceCursor = getBalanceList();
        this.startManagingCursor(this.balanceCursor);

        ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.balance_row, balanceCursor,
                new String[] {"transaction_title", "transaction_value", "transaction_category"}, new int[] { R.id.TRANS_TITLE, R.id.TRANS_VALUE, R.id.TRANS_CATEGORY});

        ovListView.setAdapter(adapter);

        /*
         * TODO: Implement callback function to close the database connection once
         * 		 the activity is closed.
         */
        
    }
    
    /*
     * FUNCTION: public void displayAccountBalance()
     * 
     * DESCRIPTION: Displays this month's expenses total.
     * 
     * 		INPUTS: (none)
     * 		OUTPUTS: (none)
     * 
     * 
     */
    public void displayAccountBalance() {
    	
    	abProgress = (ProgressBar) findViewById(R.id.abProgressBar);
        abProgressStatus = 70;
        abProgress.setMax(100);
        
        abProgress.setProgressDrawable(getResources().getDrawable(R.drawable.ab_progress_layout));
        abProgress.setIndeterminate(false);
        abProgress.setProgress(abProgressStatus);
    	
    }
    
    /*
     * FUNCTION: public void displayMonthlyBalance()
     * 
     * DESCRIPTION: Displays this month's balance, and balance minus expenses
     * 
     * 		INPUTS: (none)
     * 		OUTPUTS: (none)
     * 
     * 
     */
    public void displayMonthlyBudgetFlow() {
    	
    	mbProgress = (ProgressBar) findViewById(R.id.mbProgressBar);
        mbProgressStatus = 40;
        mbProgress.setMax(100);
        
        mbProgress.setProgressDrawable(getResources().getDrawable(R.drawable.mb_progress_layout));
        mbProgress.setIndeterminate(false);
        mbProgress.setProgress(mbProgressStatus);
        
    }
    
    /*
     * FUNCTION: public void displayLastTransaction()
     * 
     * DESCRIPTION: Displays the last transaction made
     * 
     * 		INPUTS: (none)
     * 		OUTPUTS: (none)
     * 
     */
    public void displayLastTransaction() {
    	
    }
    
	public Cursor getBalanceList() {
		String tableName = "transactions";

        return dbConn.query(tableName, new String[] { KEY_ROWID, KEY_TITLE, KEY_VALUE, KEY_PREFIX, KEY_CATEGORY, KEY_DATE}, null, null, null, null, KEY_DATE);
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
    public SQLiteDatabase connectToDb() {
    	
    	FinancoidOpenDb dbHelper = new FinancoidOpenDb(this);
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
    
  
    
}//end of class OverviewActivity
