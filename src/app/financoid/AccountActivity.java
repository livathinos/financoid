package app.financoid;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
	private Cursor fetchAccountCur;

	private int fetchedAccountId;
	private String fetchedAccountName;
	private double fetchedAccountBalance;
	private double fetchedMonthlyBudget;
	
	private final static String TAG = "Account management";
	private final static String TABLE_NAME = "accounts";
	private String ACC_NAME;
	private String ACC_BALANCE;
	private String ACC_MONTHLY_BUDGET;
	
	private final static String KEY_ROWID = "_id";
	private final static String KEY_TITLE = "account_name";
	private final static String KEY_BALANCE = "account_balance";
	private final static String KEY_MONTHLY_BUDGET = "account_monthly_budget";
	
	private final static int account_tag_id = 1;
	
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
    
    public void onResume() {
    	
    	dbConn = FinancoidOpenDb.connectToDb(this, dbConn);
    	fetchAccountCur = fetchAccountInfo();
        
        displayAccountInfo(fetchAccountCur);
    	
    	super.onResume();
    	
    }
    
    public Cursor fetchAccountInfo() {
		
    	return dbConn.query(TABLE_NAME, new String [] {KEY_ROWID, KEY_TITLE, KEY_BALANCE, KEY_MONTHLY_BUDGET} , KEY_ROWID + "=" + account_tag_id, null, null, null, null );
    	
    }
    
    public void displayAccountInfo(Cursor cur) {
    	
    	if (cur.moveToFirst()) {
			
			fetchedAccountName = cur.getString(cur.getColumnIndex(KEY_TITLE));
			fetchedAccountBalance = cur.getDouble(cur.getColumnIndex(KEY_BALANCE));
			fetchedMonthlyBudget = cur.getDouble(cur.getColumnIndex(KEY_MONTHLY_BUDGET));
			fetchedAccountId = cur.getInt(cur.getColumnIndex(KEY_ROWID));
			
		}
		
		cur.close();
		
		if (fetchedAccountName != null)
			mAccountNameEditText.setText(fetchedAccountName);
		
		if (fetchedAccountBalance != 0)
			mAccountBalanceEditText.setText("" + fetchedAccountBalance);
		
		if (fetchedMonthlyBudget != 0)
			mMonthlyBudgetEditText.setText("" + fetchedMonthlyBudget);
    	
    }

	/*
	 * FUNCTION: public Cursor getAllCategories()
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
	public void accountUpdate() {
		
		ACC_NAME = mAccountNameEditText.getText().toString();
	    ACC_BALANCE = mAccountBalanceEditText.getText().toString();
	    ACC_MONTHLY_BUDGET = mMonthlyBudgetEditText.getText().toString();
	    
		ContentValues updateAccount = new ContentValues();
	    updateAccount.put("account_name", ACC_NAME);
	    updateAccount.put("account_balance", ACC_BALANCE);
	    updateAccount.put("account_monthly_budget", ACC_MONTHLY_BUDGET);
	    
	    dbConn.update("accounts", updateAccount, "_id=?", new String[] {"1"});
	       
	}
	
	public boolean accountExists() {
		
		return true;
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
		
	    if (!accountExists())
	    	createAccountEntry();
	    else
	    	accountUpdate();
	    
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

}
