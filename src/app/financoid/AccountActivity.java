package app.financoid;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

/*
 * CONSTRUCTOR: public class AccountActivity [extends] Activity
 * 
 * DESCRIPTION: Class consisting of all the necessary tools for activity view
 * 				of the Account creation and display of the application.
 * 
 * 		INPUTS: (none)
 * 		OUTPUTS: (none)
 * 
 */
public class AccountActivity extends Activity {
	
	private EditText mAccountNameEditText;
	private EditText mAccountBalanceEditText;
	private EditText mMonthlyBudgetEditText;
	private Button mAccountSaveButton;
	private SQLiteDatabase dbConn;
	
	private final static String TAG = "Account management";
	private final static String TABLE_NAME = "accounts";
	private String ACC_NAME;
	private String ACC_BALANCE;
	private String ACC_MONTHLY_BUDGET;
	
	private final static String KEY_ROWID = "_id";
	private final static String KEY_TITLE = "account_name";
	private final static String KEY_BALANCE = "account_balance";
	private final static String KEY_MONTHLY_BUDGET = "account_monthly_budget";
	private final static String KEY_DATE = "account_date";
	private final static String KEY_EXTRA_DATE = "account_extra_date";
	
	private Calendar m_date = GregorianCalendar.getInstance();
	
	
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
        setContentView(R.layout.account_management);
        
        dbConn = connectToDb();
        
        // Obtain handles to UI objects
        mAccountNameEditText = (EditText) findViewById(R.id.accountNameEditText);
        mAccountBalanceEditText = (EditText) findViewById(R.id.accountBalanceEditText);
        mMonthlyBudgetEditText = (EditText) findViewById(R.id.accountMonthlyBudgetEditText);
        mAccountSaveButton = (Button) findViewById(R.id.accountSaveButton);
  
        mAccountSaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSaveButtonClicked();
            }
        });
    }

	/*
	 * FUNCTIN: public Cursor getAllCategories()
	 * 
	 * DESCRIPTION: Returns all categories inside the database tables 'categories'
	 * 
	 * 		INPUTS: (none)
	 * 		OUTPUTS: (none)
	 * 
	 * 
	 */
	public Cursor getAccounts() {
	
	    return dbConn.query(TABLE_NAME, new String[] { KEY_ROWID, KEY_TITLE, KEY_BALANCE, KEY_MONTHLY_BUDGET}, null, null, null, null, null);
	
	}
	
	/*
	 * FUNCTION: public void updateCategorySelection()
	 * 
	 * DESCRIPTION: [PLACEHOLDER]
	 * 
	 */
	public void updateAccountSelection() {
		
	}
	
	/*
	 * FUNCTION: public void onSaveButtonClicked()
	 * 
	 * DESCRIPTION: Called when the save button is clicked. Calls upon the createTransactionEntry() 
	 * 				function in order to create a new transaction.
	 * 
	 * 		INPUTS: (none)
	 * 		OUTPUTS: (none)
	 * 
	 * 
	 */
	public void onSaveButtonClicked() {
		
		Log.v(TAG, "Save button clicked");
		
	    createAccountEntry();
	    finish();
	    dbConn.close();
		
	}
	
	/*
	 * FUNCTION: public void createTransactionEntry()
	 * 
	 * DESCRIPTION: Creates a new transaction inserting a new row of data into the 'transactions'
	 * 				table of the datatabase.
	 * 
	 * 		INPUTS: (none)
	 * 		OUTPUTS: (none)
	 * 
	 * 
	 */
	public void createAccountEntry() {
		
		ACC_NAME = mAccountNameEditText.getText().toString();
	    ACC_BALANCE = mAccountBalanceEditText.getText().toString();
	    ACC_MONTHLY_BUDGET = mMonthlyBudgetEditText.getText().toString();
	    
	    dbConn.execSQL("INSERT INTO " + TABLE_NAME + " (account_name, account_balance, account_monthly_budget, account_date, account_extra_date)"
				+ " VALUES ('" + ACC_NAME + "', '" + ACC_BALANCE + "', '" + ACC_MONTHLY_BUDGET + "', '" + m_date.getTimeInMillis() + "', datetime());");
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
    
    

}
