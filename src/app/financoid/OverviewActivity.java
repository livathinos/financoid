package app.financoid;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
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
	private TextView ovMonthlyBalanceOverdrawn;
	private TextView ovAccountBalanceOverdrawn;
	
	private DateFormat f_dateFormatter = null;
	private Calendar m_date = GregorianCalendar.getInstance();
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
	private double monthlyBalance;
	private double monthlyBalanceRemaining;
	private double monthlyBalanceRate;
	private double moneySpent;
	private double moneyMonthlySpent;
	
	private double tmpDouble;
	
	public int numTransactions = 0;
	public int numTransactionsThisMonth = 0;
	
	private String TABLE_ACCOUNTS = "accounts";
	private String TABLE_TRANSACTIONS = "transactions";
	
	
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
        //ovAccountBalanceRate = (TextView) findViewById(R.id.abBalanceRate);
        ovAccountBalanceSpent = (TextView) findViewById(R.id.abBalanceSpent);
        
        ovMonthlyBalance = (TextView) findViewById(R.id.mbBalance);
        ovMonthlyBalanceRemaining = (TextView) findViewById(R.id.mbBalanceRemaining);
        ovMonthlyBalanceRate = (TextView) findViewById(R.id.mbBalanceRate);
        ovMonthlyBalanceSpent = (TextView) findViewById(R.id.mbBalanceSpent);
        
        populateBalanceFields();
        
        populateRecentTransactionList();
        
    }
    
    public void populateBalanceFields() {
    	String dateQuery = "(strftime('%m') == strftime('%m',transaction_extra_date))";
    	
    	Cursor accCursor = dbConn.query(TABLE_ACCOUNTS, new String[] { KEY_ACC_NAME, KEY_ACC_BALANCE, KEY_ACC_MONTHLY, KEY_ACC_DATE, KEY_ACC_EXTRA_DATE }, null, null, null, null,null);
    	Cursor transCursor = dbConn.query(TABLE_TRANSACTIONS, new String [] {KEY_TITLE, KEY_VALUE, KEY_DATE, KEY_EXTRA_DATE}, null, null, null, null, null);
    	Cursor transMonthCursor = dbConn.query(TABLE_TRANSACTIONS, new String [] {KEY_TITLE, KEY_VALUE, KEY_DATE, KEY_EXTRA_DATE}, dateQuery, null, null, null, null);
    	
		/*
		 * Start using the transaction Cursor to be able to calculate values such as total money spent,
		 * and money spent this month.
		 * 
		 */
		if(transCursor.moveToFirst()) {
			
			moneySpent = getMoneySpent(transCursor);
			ovAccountBalanceSpent.setText("Total expenses: Û" + doubleDecimalFormat(moneySpent));
			
		} else {
			
			ovAccountBalanceSpent.setText("");
			
		}
		
		if (transMonthCursor.moveToFirst()) {
			
			moneyMonthlySpent = getMoneyMonthlySpent(transMonthCursor);
			ovMonthlyBalanceSpent.setText("Monthly expenses: Û" + doubleDecimalFormat(moneyMonthlySpent));
			
		} else {
			
			ovMonthlyBalanceSpent.setText("");
			
		}
		
		transCursor.close();
		transMonthCursor.close();
		
		if (accCursor.moveToFirst()) {
			accountBalance = accCursor.getDouble(accCursor.getColumnIndex("account_balance"));
		
		
			accountBalance = getAccountBalance(accCursor);
			monthlyBalance = getMonthlyBalance(accCursor);
	    	
	        /*
	         * TODO: The text below should be using the preferencefactory to get the type of
	         * 		 currency. At the time being it's hardcoded into the String which should be changed.
	         */
	        ovAccountBalance.setText("Account budget: Û" + accountBalance);
	        ovMonthlyBalance.setText("Monthly budget: Û" + monthlyBalance);
	        /*ovAccountBalance.setText("Account budget: Û" + accountBalance, TextView.BufferType.SPANNABLE);
	        ovMonthlyBalance.setText("Monthly budget: Û" + monthlyBalance, TextView.BufferType.SPANNABLE);
	        
	        Spannable str_ab = (Spannable) ovAccountBalance.getText();
	        Spannable str_mb = (Spannable) ovMonthlyBalance.getText();
	        
	        str_ab.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, str_ab.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	        str_mb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 17, str_mb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	        */
	    	abProgress = (ProgressBar) findViewById(R.id.abProgressBar);
	        
	    	/*
	    	 * Cast moneySpent to a temporary variable to avoid trailing zeros
	    	 * when doing -, +, / functions on its values later on.
	    	 */
	    	
	    	abProgressStatus = (int) moneySpent;
	        
	        if (accountBalance == 0) 
	        	abProgress.setMax(100);
	        else
	        	abProgress.setMax((int) accountBalance);
	        
	        abProgress.setProgressDrawable(getResources().getDrawable(R.drawable.ab_progress_layout));
	        abProgress.setIndeterminate(false);
	        abProgress.setProgress(abProgressStatus);
	        
	        mbProgress = (ProgressBar) findViewById(R.id.mbProgressBar);
	        
	        mbProgressStatus = (int) moneyMonthlySpent;
	        
	        mbProgress.setMax((int) monthlyBalance);
	        
	        mbProgress.setProgressDrawable(getResources().getDrawable(R.drawable.mb_progress_layout));
	        mbProgress.setIndeterminate(false);
	        mbProgress.setProgress(mbProgressStatus);
			
		} else {
			
		}
		
		accCursor.close();
		
		/*
		 * Set the amount remaining for the account balance and the monthly budget
		 * Check whether the balance remaining values are positive or negative and
		 * set their colors accordingly to represent a healthy and an overdrawn
		 * account/monthly budget.
		 * 
		 */
		accountBalanceRemaining = getAccountBalanceRemaining();
		monthlyBalanceRemaining = getMonthlyBalanceRemaining();
		
		ovAccountBalanceRemaining.setText("Û" + doubleDecimalFormat(accountBalanceRemaining), TextView.BufferType.SPANNABLE);
		ovMonthlyBalanceRemaining.setText("Û" + doubleDecimalFormat(monthlyBalanceRemaining), TextView.BufferType.SPANNABLE);
		
		Spannable str_ab = (Spannable) ovAccountBalanceRemaining.getText();
        Spannable str_mb = (Spannable) ovMonthlyBalanceRemaining.getText();
        
        str_ab.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, str_ab.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str_mb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, str_mb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        
		if(accountBalanceRemaining < 0) {
		
			str_ab.setSpan(new ForegroundColorSpan(0xFF9F1D1D), 0, str_ab.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
		} else {
			
			str_ab.setSpan(new ForegroundColorSpan(0xFF357F0F), 0, str_ab.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
		}
		
		if(monthlyBalanceRemaining < 0) {
			
			str_mb.setSpan(new ForegroundColorSpan(0xFF9F1D1D), 0, str_mb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
		} else {
			
			str_mb.setSpan(new ForegroundColorSpan(0xFF357F0F), 0, str_mb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
		}
        
		monthlyBalanceRate = getMonthlyBalanceRate();
		
		ovMonthlyBalanceRate.setText("Spend rate/day: Û" + doubleDecimalFormat(monthlyBalanceRate));
		
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
		String limitBy = "4";

        return dbConn.query(true, TABLE_TRANSACTIONS, new String[] { KEY_ROWID, KEY_TITLE, KEY_VALUE, KEY_PREFIX, KEY_CATEGORY, KEY_DATE}, null, null, null, null, KEY_EXTRA_DATE+" DESC", limitBy);
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
	
	public double getMonthlyBalance(Cursor cur) {
		
		monthlyBalance = cur.getDouble(cur.getColumnIndex("account_monthly_budget"));
		
		return monthlyBalance;
		
	}
	
	public double getAccountBalanceRemaining() {
		
		accountBalanceRemaining = accountBalance - moneySpent;
		return accountBalanceRemaining;
		
	}
	
	public double getMonthlyBalanceRemaining() {
		
		monthlyBalanceRemaining = monthlyBalance - moneyMonthlySpent;
		return monthlyBalanceRemaining;
		
	}
	
	public double getAccountBalanceRate(Cursor cur) {
		
		
		return accountBalanceRate;
		
	}
	
	public double getMonthlyBalanceRate() {
		
		double numMonthDays = m_date.DAY_OF_MONTH;
		monthlyBalanceRate = moneyMonthlySpent/numMonthDays;
		
		return monthlyBalanceRate;
		
	}
	
	public double getMoneySpent(Cursor cur) {
		
		/*
		 * Get the transaction value of the first ever transaction. If there
		 * are more, keep adding to the sum to get the total paid.
		 * 
		 */
		moneySpent = cur.getDouble(cur.getColumnIndex("transaction_value"));
		numTransactions++;
		
		while(cur.moveToNext()) {
			
			numTransactions++;
			moneySpent += cur.getDouble(cur.getColumnIndex("transaction_value"));
			
		}
		
		return moneySpent; 
		
	}
	
	public double getMoneyMonthlySpent(Cursor cur) {
		
		/*
		 * Get the transaction value of the first transaction this month. If there
		 * are more, keep adding to the sum to get the total paid this month.
		 * 
		 */
		
		moneyMonthlySpent = cur.getDouble(cur.getColumnIndex("transaction_value"));
		numTransactionsThisMonth++;
		
		while(cur.moveToNext()) {
			
			numTransactionsThisMonth++;
			moneyMonthlySpent += cur.getDouble(cur.getColumnIndex("transaction_value"));
			
		}
		
		return moneyMonthlySpent;
		
	}
	
	public String doubleDecimalFormat(double num) {
		
		NumberFormat numberFormat = DecimalFormat.getInstance();
		numberFormat.setMaximumFractionDigits(2);
		String formattedText = numberFormat.format(num);
		
		return formattedText;
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
