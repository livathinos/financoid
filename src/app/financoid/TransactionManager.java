package app.financoid;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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
	private String TABLE_CATEGORIES = "categories";
	
    public Object item;
    public Cursor model = null;
    public String trans_cat = null; 
	
	private String KEY_ROWID = "_id";
	private String KEY_TITLE = "transaction_title";
	private String KEY_AMOUNT = "transaction_value";
	private String KEY_CATEGORY = "transaction_category";
	private String KEY_DATE = "transaction_date";
	private String KEY_EXTRA_DATE = "transaction_extra_date";
	
	private String KEY_CAT_TITLE = "category_name";
	
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
		
		if (fetchedTransactionAmount != 0)
			mTransactionAmountEditText.setText("" + fetchedTransactionAmount);
		
		model = getAllCategories();
        SimpleCursorAdapter ModelAdapter = new SimpleCursorAdapter(this,
        		   android.R.layout.simple_spinner_item, model,
        		   new String[] {"category_name"},       
        		   new int[] {android.R.id.text1});
        
        ModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategorySpinner.setAdapter(ModelAdapter);
        trans_cat = model.getString(1);


        mCategorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long i) {
                //updateCategorySelection();
            	item = parent.getItemAtPosition(position);
            	trans_cat = model.getString(1);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // We don't need to worry about nothing being selected, since Spinners don't allow
                // this.
            }
        });
		
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
    public Cursor getAllCategories() {

        return dbConn.query(TABLE_CATEGORIES, new String[] { KEY_ROWID, KEY_CAT_TITLE}, null, null, null, null, null);

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
    	
        updateTransactionEntry();
        finish();
        
        model.close();
        dbConn.close();
    	
    }
    
    /*
     * FUNCTION: public void onDeleteButtonClicked()
     * 
     * DESCRIPTION: Called when the delete button is clicked. Calls upon the createTransactionEntry() 
     * 				function in order to create a new transaction.
     * 
     * 		INPUTS: (none)
     * 		OUTPUTS: (none)
     * 
     * 
     */
    public void onDeleteButtonClicked() {
    	
    	Log.v(TAG, "Delete button clicked");
    	
        deleteTransactionEntry();
        finish();
        
        model.close();
        dbConn.close();
    	
    }
    
    public void updateTransactionEntry() {
    	
    	String trans_name = mTransactionNameEditText.getText().toString();
        String trans_amount = mTransactionAmountEditText.getText().toString();
        
        ContentValues updateTransaction = new ContentValues();
	    updateTransaction.put("transaction_title", trans_name);
	    updateTransaction.put("transaction_value", trans_amount);
	    updateTransaction.put("transaction_category", trans_cat);
	    
	    int trans_id = ((Long) transaction_tag_id).intValue();
	    
	    String [] formatCategoryName = new String [] {""+trans_id};
	    
	    dbConn.update(TABLE_NAME, updateTransaction, "_id=?", formatCategoryName);
        
    }
    
    public void deleteTransactionEntry() {
    	
    	int trans_id = ((Long) transaction_tag_id).intValue();
    	
    	dbConn.execSQL("DELETE FROM " + TABLE_NAME +  " WHERE _id = " + trans_id + ";");
    	
    }
	
}
