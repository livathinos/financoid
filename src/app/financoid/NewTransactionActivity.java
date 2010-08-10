package app.financoid;
import java.io.*;
import java.util.ArrayList;

import android.app.Activity;
import android.database.SQLException;
import android.database.sqlite.*;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.view.View;

public class NewTransactionActivity extends Activity {
	 
	private ArrayList<String> mCategories;
    private Spinner mCategorySpinner;
    private EditText mCategoryNameEditText;
    private EditText mTransactionAmountEditText;
    private Button mTransactionSaveButton;
    private SQLiteDatabase dbConn;
    
    private final String TRANSACTION_TABLE = "transactions";
    
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
        mCategoryNameEditText = (EditText) findViewById(R.id.transactionNameEditText);
        mTransactionAmountEditText = (EditText) findViewById(R.id.transactionAmountText);
        mTransactionSaveButton = (Button) findViewById(R.id.transactionSaveButton);
        
        // Prepare model for category spinner
        mCategories = new ArrayList<String>();
        
        /*
         * TODO: Implement callback function to close the database connection once
         * 		 the activity is closed.
         */
        
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


    }
    
    
    /*
     * FUNCTION: public void saveTransaction()
     * 
     * DESCRIPTION: Saves the transaction that was entered by the user.
     * 
     * 		INPUTS: (none)
     * 		OUTPUTS: (none)
     * 
     * 
     */
    public void saveTransaction(View button){
    	
    	dbConn.execSQL("INSERT INTO " + TRANSACTION_TABLE + " (transaction_title, transaction_value, transaction_prefix, transaction_category)"
    					+ " VALUES ('Transaction One', '200', '+', 'General Category');");
    }
}
