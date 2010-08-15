package app.financoid;
import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.*;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.util.Log;
import android.view.View;

public class NewTransactionActivity extends Activity {
	 
    private Spinner mCategorySpinner;
    private EditText mTransactionNameEditText;
    private EditText mTransactionAmountEditText;
    private Button mTransactionSaveButton;
    private SQLiteDatabase dbConn;
    
    public Object item;
    public Cursor model = null;
    public String trans_cat = null; 
    
    public static final String TAG = "NewTransaction";
    private final String TRANSACTION_TABLE = "transactions";
    
    public static final String KEY_TITLE = "category_name";
    public static final String KEY_ROWID = "_id";
    
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
        setContentView(R.layout.new_transaction);
        dbConn = connectToDb();
        
        // Obtain handles to UI objects
        mCategorySpinner = (Spinner) findViewById(R.id.transactionCategorySpinner);
        mTransactionNameEditText = (EditText) findViewById(R.id.transactionNameEditText);
        mTransactionAmountEditText = (EditText) findViewById(R.id.transactionAmountText);
        mTransactionSaveButton = (Button) findViewById(R.id.transactionSaveButton);
        
        model = getAllCategories();
        SimpleCursorAdapter ModelAdapter = new SimpleCursorAdapter(this,
        		   android.R.layout.simple_spinner_item, model,
        		   new String[] {"category_name"},       
        		   new int[] {android.R.id.text1});
        		ModelAdapter.setDropDownViewResource(
        		        android.R.layout.simple_spinner_dropdown_item);
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
        mTransactionSaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSaveButtonClicked();
            }
        });
        
        
        
        /*
         * TODO: Implement callback function to close the database connection once
         * 		 the activity is closed.
         */
        
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
    public Cursor getAllCategories() {
    	String tableName = "categories";

        return dbConn.query(tableName, new String[] { KEY_ROWID, KEY_TITLE}, null, null, null, null, null);

    }
    
    /*
     * FUNCTION: public void updateCategorySelection()
     * 
     * DESCRIPTION: [PLACEHOLDER]
     * 
     */
    public void updateCategorySelection() {
    	
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
    	
        createTransactionEntry();
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
    public void createTransactionEntry() {
    	
    	String trans_name = mTransactionNameEditText.getText().toString();
        String trans_amount = mTransactionAmountEditText.getText().toString();
        
        dbConn.execSQL("INSERT INTO " + TRANSACTION_TABLE + " (transaction_title, transaction_value, transaction_prefix, transaction_category, transaction_date, transaction_extra_date)"
				+ " VALUES ('" + trans_name + "', '" + trans_amount + "', '+', '" + trans_cat + "', '" + m_date.getTimeInMillis() + "', datetime());");
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
    
}//end of class NewTransactionActivity
