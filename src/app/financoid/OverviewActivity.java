package app.financoid;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class OverviewActivity extends Activity {
	
	public static final String KEY_TITLE="transaction_title";
	public static final String KEY_VALUE="transaction_value";
	public static final String KEY_PREFIX="transaction_prefix";
	public static final String KEY_CATEGORY = "transaction_category";
	public static final String KEY_DATE = "transaction_date";
	public static final String KEY_ROWID="_id";
	
	private SQLiteDatabase dbConn;
	private Cursor balanceCursor;
	
	private ProgressBar abProgress;
	private int abProgressStatus = 0;
	
	
	private ProgressBar mbProgress;
	private int mbProgressStatus = 0;
	
	private PreferenceFactory f_prefs;
	private ListView ovListView;
	private DateFormat f_dateFormatter = null;
	private Map<Long, Transaction> m_transactionMap;
	
	private int COL_ID = 0;
	private int COL_TITLE = 1;
	private int COL_VALUE = 2;
	private int COL_CATEGORY = 4;
	private int COL_DATE = 5;
	
	
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
        
        f_prefs = PreferenceFactory.getInstance(this);
        setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);

        displayAccountBalance();
        displayMonthlyBudgetFlow();
        
        ovListView = (ListView) findViewById(android.R.id.list);

        balanceCursor = getBalanceList();
        this.startManagingCursor(this.balanceCursor);
        final String[] PROJECTION = Transaction.getProjection();
        
        /*Cursor latestCursor = managedQuery(Transaction.CONTENT_URI, PROJECTION, null, null, Transaction.DEFAULT_SORT_ORDER);
		if (latestCursor.getCount() > 0) {
			latestCursor.moveToFirst();
			
			double total_amount = 0D;
			
			COL_TITLE = latestCursor.getColumnIndex(Transaction.TITLE);
			COL_VALUE = latestCursor.getColumnIndex(Transaction.VALUE);
			COL_CATEGORY = latestCursor.getColumnIndex(Transaction.CATEGORY);
			COL_DATE = latestCursor.getColumnIndex(Transaction.DATE);

			List<Transaction> transactions = new ArrayList<Transaction>();
			m_transactionMap = new HashMap<Long, Transaction>();
			int numTransactions = latestCursor.getCount();
			while (latestCursor.isAfterLast() == false) {
				Transaction f = new Transaction(latestCursor);

				transactions.add(0, f);

				m_transactionMap.put(f.getId(), f);

				if (transactions.size() < numTransactions) {
					total_amount += f.getAmount();
				}

				latestCursor.moveToNext();
			}

			//total_distance = fillups.get(fillups.size() - 1).getOdometer() - fillups.get(0).getOdometer();
			//m_avgMpg = m_calcEngine.calculateEconomy(total_distance, total_fuel);
		}
         */
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.balance_row, balanceCursor,
                new String[] {"transaction_title", "transaction_value", "transaction_category", "transaction_date"}, new int[] { R.id.TRANS_TITLE, R.id.TRANS_VALUE, R.id.TRANS_CATEGORY, R.id.TRANS_DATE});
        
        adapter.setViewBinder(m_viewBinder);
        
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
    
    /*
     * CONSTRUCTOR: private SimpleCursorAdapter
     * 
     * DESCRIPTION: constructor of a SimpleCursorAdapter object instance to extract all the
     * 				necessary information from the transactions tables.
     * 
     * 		INPUTS: (none)
     * 		OUTPUTS: boolean true/false
     * 
     */
    private SimpleCursorAdapter.ViewBinder m_viewBinder = new SimpleCursorAdapter.ViewBinder() {
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			
			String text = null;
			TextView textview = (TextView) view;
			if (textview == null) {
				return false;
			}
			if (columnIndex == COL_TITLE) {
				String title = cursor.getString(columnIndex);
				text = title;
			} else if (columnIndex == COL_VALUE) {
				double amount = cursor.getDouble(columnIndex);
				text = f_prefs.getCurrency() + amount;
			} else if (columnIndex == COL_DATE) {
				long time = cursor.getLong(columnIndex);
				Date date = new Date(time);
				if (f_dateFormatter == null) {
					f_dateFormatter = android.text.format.DateFormat.getMediumDateFormat(OverviewActivity.this);
				}
				text = f_dateFormatter.format(date);
			} else if (columnIndex == COL_CATEGORY) {
				String category = cursor.getString(columnIndex);
				text = category;
			}
			textview.setText(text);
			return true;
		}
	};
  
    
}//end of class OverviewActivity
