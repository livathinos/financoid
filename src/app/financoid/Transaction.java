package app.financoid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.net.Uri;

/**
 * Provide a data model to encapsulate the logic for a Transaction.
 * 
 */
public class Transaction {
	
	private long m_id;
	
	@SuppressWarnings("unused")
	private String m_title;
	
	@SuppressWarnings("unused")
	private String m_category;
	private double m_value;
	
	@SuppressWarnings("unused")
	private String m_date;
	
	@SuppressWarnings("unused")
	private double m_amount;
	
	public static final String TITLE = "transaction_name"; // price per unit volume
	public static final String VALUE = "transaction_value";
	public static final String CATEGORY = "transaction_category"; // odometer, not economy
	public static final String DATE = "transaction_date"; // timestamp in milliseconds
	public static final String _ID = "_id";
	public static final String TABLE_NAME = "transactions";
	
	public static final String AUTHORITY = "app.financoid";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/transactions");
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.app.financoid.transaction";
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.app.financoid.transaction";
	public static final String DEFAULT_SORT_ORDER = VALUE + " DESC";
	
	public static final Map<String, String> PLAINTEXT = new HashMap<String, String>();
	public static final List<String> PROJECTION = new ArrayList<String>();

	static {
		PLAINTEXT.put(DATE, "Date");
		PLAINTEXT.put(VALUE, "Value of transaction");
		PLAINTEXT.put(CATEGORY, "Category of transaction");
		PLAINTEXT.put(_ID, "Id of transaction");
		PLAINTEXT.put(TITLE, "title");

		PROJECTION.add(_ID);
		PROJECTION.add(TITLE);
		PROJECTION.add(CATEGORY);
		PROJECTION.add(VALUE);
		PROJECTION.add(DATE);
	}
	
	public static String[] getProjection() {
		return PROJECTION.toArray(new String[PROJECTION.size()]);
	}
	
	public Transaction(Cursor c) {
		load(c);
	}
	
	private void load(Cursor c) {
		int index = c.getColumnIndex(_ID);

		if (index >= 0) {
			m_id = c.getLong(index);
		}

		index = c.getColumnIndex(TITLE);
		if (index >= 0) {
			m_title = c.getString(index);
		}

		index = c.getColumnIndex(VALUE);
		if (index >= 0) {
			m_value = c.getDouble(index);
		}

		index = c.getColumnIndex(DATE);
		if (index >= 0) {
			m_date = c.getString(index);
		}
	}
	public long getId() {
		return m_id;
	}
	
	public double getAmount() {
		return m_value;
	}
	
}
