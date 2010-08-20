package app.financoid;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class TransactionManager extends Activity {
	
	private SQLiteDatabase dbConn;
	private Cursor fetchTransactionCur;
	private Spinner mCategorySpinner;
    private EditText mTransactionNameEditText;
    private EditText mTransactionAmountEditText;
    private Button mTransactionSaveButton;
    private Button mTransactionDeleteButton;
    private Calendar m_date = GregorianCalendar.getInstance();
    
    private Object transaction_tag_id;
	
	public static final String TAG = "Transaction Manager";
	
	private String TABLE_NAME = "transactions";
	
	private String KEY_ROWID = "_id";
	private String KEY_TITLE = "transaction_title";
	private String KEY_AMOUNT = "transaction_value";
	private String KEY_CATEGORY = "transaction_category";
	private String KEY_DATE = "transaction_date";
	private String KEY_EXTRA_DATE = "transaction_extra_date";
	
	private int fetchedTransactionId;
	private String fetchedTransactionName;
	private double fetchedTransactionAmount;
	private String fetchedTransactionCategory;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_management);
        
        // Obtain handles to UI objects
        mCategorySpinner = (Spinner) findViewById(R.id.transactionCategorySpinner);
        mTransactionNameEditText = (EditText) findViewById(R.id.transactionNameEditText);
        mTransactionAmountEditText = (EditText) findViewById(R.id.transactionAmountText);
        mTransactionSaveButton = (Button) findViewById(R.id.transactionSaveButton);
        mTransactionDeleteButton = (Button) findViewById(R.id.transactionDeleteButton);
        
        //Get the data that was sent from the OverviewActivity's ListView, containing the
        // clicked list item.
        Intent intent = getIntent();
        transaction_tag_id = intent.getExtras().get("TransactionId");
		
	}
	
	@Override
	public void onResume() {
		
		dbConn = FinancoidOpenDb.connectToDb(this, dbConn);
		
        fetchTransactionCur = fetchTransactionInfo();
        
        displayTransactionInfo(fetchTransactionCur);
        
        mTransactionSaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSaveButtonClicked();
            }
        });
        
        mTransactionDeleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onDeleteButtonClicked();
            }
        });
        
        super.onResume();
        
	}
	
	public Cursor fetchTransactionInfo() {
		
		return dbConn.query(TABLE_NAME, new String [] {KEY_ROWID, KEY_TITLE, KEY_AMOUNT, KEY_CATEGORY, KEY_DATE, KEY_EXTRA_DATE} , KEY_ROWID + "=" + transaction_tag_id, null, null, null, null );
	}
	
	public void displayTransactionInfo(Cursor cur) {
		
		if (cur.moveToFirst()) {
			
			fetchedTransactionName = cur.getString(cur.getColumnIndex(KEY_TITLE));
			fetchedTransactionAmount = cur.getDouble(cur.getColumnIndex(KEY_AMOUNT));
			fetchedTransactionCategory = cur.getString(cur.getColumnIndex(KEY_CATEGORY));
			fetchedTransactionId = cur.getInt(cur.getColumnIndex(KEY_ROWID));
			
		}
		
		cur.close();
		
		if (fetchedTransactionName != null)
			mTransactionNameEditText.setText(fetchedTransactionName);
		
		//if (fetchedTransactionAmount != null)
			//mTransactionAmountEditText.setText(fetchedTransactionAmount);
		
		//if (fetchedTransactionCategory != null)
			//mCategorySpinner.setText();
		
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
    	
        //createTransactionEntry();
        finish();
        dbConn.close();
    	
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
    public void onDeleteButtonClicked() {
    	
    	Log.v(TAG, "Delete button clicked");
    	
        //createTransactionEntry();
        finish();
        dbConn.close();
    	
    }
	
}
