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
import android.graphics.Color;
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
	public static final String KEY_EXTRA_DATE = "transaction_extra_date";
	public static final String KEY_ROWID="_id";
	
	public static final String KEY_ACC_ID = "_id";
	public static final String KEY_ACC_NAME = "account_name";
	public static final String KEY_ACC_BALANCE = "account_balance";
	public static final String KEY_ACC_MONTHLY = "account_monthly_budget";
	public static final String KEY_ACC_DATE = "account_date";
	public static final String KEY_ACC_EXTRA_DATE = "account_extra_date";
	
	private SQLiteDatabase dbConn;
	private Cursor balanceCursor;
	private Cursor accBalanceCursor;
	private Cursor accMonthlyCursor;
	
	private ProgressBar abProgress;
	private int abProgressStatus = 0;
	
	
	private ProgressBar mbProgress;
	private int mbProgressStatus = 0;
	
	private PreferenceFactory f_prefs;
	private ListView ovListView;
	private TextView ovAccountBalance;
	private TextView ovMonthlyBalance;
	private TextView ovAccountBalanceRemaining;
	private TextView ovMonthlyBalanceRemaining;
	private TextView ovAccountBalanceRate;
	private TextView ovMonthlyBalanceRate;
	private TextView ovAccountBalanceSpent;
	private TextView ovMonthlyBalanceSpent;
	
	private DateFormat f_dateFormatter = null;
	private Map<Long, Transaction> m_transactionMap;
	
	private int COL_ID = 0;
	private int COL_TITLE = 1;
	private int COL_VALUE = 2;
	private int COL_CATEGORY = 4;
	private int COL_DATE = 5;
	
	private int COL_ACC_ID = 0;
	private int COL_ACC_NAME = 1;
	private int COL_ACC_BALANCE = 2;
	private int COL_ACC_MONTHLY = 3;
	private int COL_ACC_DATE = 5;
	
	private double accountBalance;
	private double accountBalanceRemaining;
	private double accountBalanceRate;
	private double monthlyBudget;
	private double monthlyBudgetRemaining;
	private double monthlyBudgetRate;
	private double moneySpent;
	private double moneySpentMonth;
	
	private double totalMoneySpent;
	private double totalMoneySpentMonth;
	
	private String TABLE_ACCOUNTS = "accounts";
	
	
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
        
        ovListView = (ListView) findViewById(android.R.id.list);
        
        ovAccountBalance = (TextView) findViewById(R.id.abBalance);
        ovAccountBalanceRemaining = (TextView) findViewById(R.id.abBalanceRemaining);
        ovAccountBalanceRate = (TextView) findViewById(R.id.abBalanceRate);
        ovAccountBalanceSpent = (TextView) findViewById(R.id.abBalanceSpent);
        
        ovMonthlyBalance = (TextView) findViewById(R.id.mbBalance);
        ovMonthlyBalanceRemaining = (TextView) findViewById(R.id.mbBalanceRemaining);
        ovMonthlyBalanceRate = (TextView) findViewById(R.id.mbBalanceRate);
        ovMonthlyBalanceSpent = (TextView) findViewById(R.id.mbBalanceSpent);
        
        populateBalanceFields();
        
        populateRecentTransactionList();
        
    }
    
    public void populateBalanceFields() {
    	
    	Cursor accCursor = dbConn.query(TABLE_ACCOUNTS, new String[] { KEY_ACC_NAME, KEY_ACC_BALANCE, KEY_ACC_MONTHLY, KEY_ACC_DATE, KEY_ACC_EXTRA_DATE }, null, null, null, null,null);
   
		if (accCursor.moveToFirst()) {
			accountBalance = accCursor.getDouble(accCursor.getColumnIndex("account_balance"));
		
		
			accountBalance = getAccountBalance(accCursor);
			monthlyBudget = getMonthlyBudget(accCursor);
	    	
	    	abProgress = (ProgressBar) findViewById(R.id.abProgressBar);
	        abProgressStatus = 700;
	        if (accountBalance == 0) 
	        	abProgress.setMax(100);
	        else
	        	abProgress.setMax((int) accountBalance);
	        
	        ovAccountBalance.setText("Total account balance: Û" + accountBalance);
	        ovMonthlyBalance.setText("Monthly budget: Û" + monthlyBudget);
	        
	        abProgress.setProgressDrawable(getResources().getDrawable(R.drawable.ab_progress_layout));
	        abProgress.setIndeterminate(false);
	        abProgress.setProgress(abProgressStatus);
	        
	        mbProgress = (ProgressBar) findViewById(R.id.mbProgressBar);
	        mbProgressStatus = 100;
	        mbProgress.setMax((int) monthlyBudget);
	        
	        mbProgress.setProgressDrawable(getResources().getDrawable(R.drawable.mb_progress_layout));
	        mbProgress.setIndeterminate(false);
	        mbProgress.setProgress(mbProgressStatus);
			
		} else {
			
		}
		
		accCursor.close();
		
    }
    
    public void populateRecentTransactionList() {

        balanceCursor = getBalanceList();
        this.startManagingCursor(this.balanceCursor);
        
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.balance_row, balanceCursor,
                new String[] {"transaction_title", "transaction_value", "transaction_category", "transaction_date"}, new int[] { R.id.TRANS_TITLE, R.id.TRANS_VALUE, R.id.TRANS_CATEGORY, R.id.TRANS_DATE});
        
        adapter.setViewBinder(m_viewBinder);
        
        ovListView.setAdapter(adapter);
        
        //balanceCursor.close();
    	
    }
    
	public Cursor getBalanceList() {
		String tableName = "transactions";
		String limitBy = "4";

        return dbConn.query(true, tableName, new String[] { KEY_ROWID, KEY_TITLE, KEY_VALUE, KEY_PREFIX, KEY_CATEGORY, KEY_DATE}, null, null, null, null, KEY_EXTRA_DATE+" DESC", limitBy);
    }
	
    
    
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
	
	public double getAccountBalance(Cursor cur) {
		
		accountBalance = cur.getDouble(cur.getColumnIndex("account_balance"));
		
		return accountBalance;
		
	}
	
	public double getMonthlyBudget(Cursor cur) {
		
		monthlyBudget = cur.getDouble(cur.getColumnIndex("account_monthly_budget"));
		
		return monthlyBudget;
		
	}
	
	public double getAccountBalanceRemaining(Cursor cur) {
		
		return accountBalanceRemaining;
		
	}
	
	public double getMonthlyBudgetRemaining(Cursor cur) {
		
		
		return monthlyBudgetRemaining;
		
	}
	
	public double getAccountBalanceRate(Cursor cur) {
		
		
		return accountBalanceRate;
		
	}
	
	public double getMonthlyBudgetRate(Cursor cur) {
		
		
		return monthlyBudgetRate;
		
	}
	
	public double getMoneySpent(Cursor cur) {
		
		return moneySpent; 
		
	}
	
	public double getMoneySpentMonth(Cursor cur) {
		
		return moneySpentMonth;
		
	}
	
	public double getTotalAccountMoneySpent(Cursor cur) {
		
		return totalMoneySpent;
		
	}
	
	public double getTotalMonthlyMoneySpent(Cursor cur) {
		
		return totalMoneySpentMonth;
		
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
