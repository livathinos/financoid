package app.financoid;
import java.io.*;
import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.*;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.util.Log;
import android.view.View;

public class NewTransactionActivity extends Activity {
	 
	private ArrayList<String> mCategories;
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
        
        // Prepare model for category spinner
        mCategories = new ArrayList<String>();
        
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
    
    
    public Cursor getAllCategories() {
    	String tableName = "categories";

        return dbConn.query(tableName, new String[] { KEY_ROWID, KEY_TITLE}, null, null, null, null, null);

    }
    
    /*
     * FUNCTION: public void updateCategorySelection()
     * 
     */
    public void updateCategorySelection() {
    	
    }
    
    /*
     * FUNCTION: public void onSaveButtonClicked()
     * 
     */
    public void onSaveButtonClicked() {
    	
    	Log.v(TAG, "Save button clicked");
        createTransactionEntry();
        finish();
    	
    }
    
    /*
     * FUNCTION: public void createTransactionEntry()
     * 
     */
    public void createTransactionEntry() {
    	
    	String trans_name = mTransactionNameEditText.getText().toString();
        String trans_amount = mTransactionAmountEditText.getText().toString();
        //String trans_cat = item.toString();
        
        dbConn.execSQL("INSERT INTO " + TRANSACTION_TABLE + " (transaction_title, transaction_value, transaction_prefix, transaction_category)"
				+ " VALUES ('" + trans_name + "', '" + trans_amount + "', '+', '" + trans_cat + "');");
        /*dbConn.execSQL("INSERT INTO categories (category_name, category_description) VALUES ('Clothes', 'Money you spent on clothes and other wearable apparel');");
        dbConn.execSQL("INSERT INTO categories (category_name, category_description) VALUES ('Entertainment', 'Money you spent on drinks/movies/theater or other places of entertainment');");
        dbConn.execSQL("INSERT INTO categories (category_name, category_description) VALUES ('Books', 'Money you spent on books');");
        dbConn.execSQL("INSERT INTO categories (category_name, category_description) VALUES ('Everyday purchases', 'Money you spent on everyday small things like cigarettes, news papers etc.');");*/
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
    	
    }
}
