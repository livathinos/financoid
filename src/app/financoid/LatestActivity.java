package app.financoid;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class LatestActivity extends Activity {
	
	public static final String KEY_TITLE="transaction_title";
	public static final String KEY_VALUE="transaction_value";
	public static final String KEY_PREFIX="transaction_prefix";
	public static final String KEY_CATEGORY = "transaction_category";
	public static final String KEY_DATE = "transaction_date";
	public static final String KEY_EXTRA_DATE = "transaction_extra_date";
	public static final String KEY_ROWID="_id";
	
	private SQLiteDatabase dbConn;
	private Cursor balanceCursor;
	
	private PreferenceFactory f_prefs;
	private ListView ovListView;
	private DateFormat f_dateFormatter = null;
	
	private int COL_TITLE = 1;
	private int COL_VALUE = 2;
	private int COL_CATEGORY = 4;
	private int COL_DATE = 6;
	
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
        setContentView(R.layout.latest_transactions);
        
        dbConn = FinancoidOpenDb.connectToDb(this, dbConn);
        
        f_prefs = PreferenceFactory.getInstance(this);
        setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
        
        ovListView = (ListView) findViewById(android.R.id.list);

        balanceCursor = getLatestList();
        this.startManagingCursor(this.balanceCursor);
        
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.balance_row, balanceCursor,
                new String[] {"transaction_title", "transaction_value", "transaction_category", "transaction_date"}, new int[] { R.id.TRANS_TITLE, R.id.TRANS_VALUE, R.id.TRANS_CATEGORY, R.id.TRANS_DATE});
        
        adapter.setViewBinder(m_viewBinder);
        
        ovListView.setAdapter(adapter);
        
        ovListView.setItemsCanFocus(true);
        ovListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				/*
				AlertDialog alertDialog = new AlertDialog.Builder(OverviewActivity.this).create();
				alertDialog.setTitle("Debug");	     
	            
	            alertDialog.setMessage("Transaction ID: " + adapter.getItemId(arg2));
	            alertDialog.show();
	            */
	            Intent intent = new Intent(LatestActivity.this, TransactionManager.class);
	            
	            /*
	             * Put the extra information we need (the row id of the item selected)
	             * into the extra field of the intent.
	             */
	            intent.putExtra("TransactionId", adapter.getItemId(arg2));
	            
	            startActivity(intent);
				
			}

	      });

        /*
         * TODO: Implement callback function to close the database connection once
         * 		 the activity is closed.
         */
        
    }
    
    /*
     * FUNCTION: public void displayMostFrequent()
     * 
     * DESCRIPTION: Displays most frequent transactions
     * 
     * 		INPUTS: (none)
     * 		OUTPUTS: (none)
     * 
     */
    public void displayMostFrequent() {
    	
    }
    
	public Cursor getLatestList() {
		String tableName = "transactions";
		String dateQuery = "(strftime('%d') == strftime('%d',transaction_extra_date))";
		
		return dbConn.query(tableName, new String[] { KEY_ROWID, KEY_TITLE, KEY_VALUE, KEY_PREFIX, KEY_CATEGORY, KEY_EXTRA_DATE, KEY_DATE},
        		dateQuery, 
        		null, null, null, KEY_EXTRA_DATE + " DESC");
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
					f_dateFormatter = android.text.format.DateFormat.getMediumDateFormat(LatestActivity.this);
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
