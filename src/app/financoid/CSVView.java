package app.financoid;

import java.io.FileWriter;
import java.io.IOException;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.widget.TextView;
import au.com.bytecode.opencsv.CSVWriter;

/*import com.evancharlton.mileage.TransactionsProvider;
import com.evancharlton.mileage.Mileage;
import com.evancharlton.mileage.R;
import com.evancharlton.mileage.calculators.CalculationEngine;
import com.evancharlton.mileage.models.Transaction;*/

public class CSVView extends ExportView {

	private String TABLE_NAME = "transactions";
	
	private static final String KEY_ID = "_id";
    private static final String KEY_TITLE = "transaction_title";
    private static final String KEY_VALUE = "transaction_value";
    private static final String KEY_CATEGORY = "transaction_category";
    private static final String KEY_DATE = "transaction_date";
	
	protected String getTag() {
		return "CSVExport";
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, "csv");
		m_title = (TextView) findViewById(R.id.title);
		m_title.setText(R.string.csv_file);

		super.m_exporter = new Runnable() {
			public void run() {
				try {
					CSVWriter csv = new CSVWriter(new FileWriter(Environment.getExternalStorageDirectory() + "/" + getFilename()));

					// write out the column headers
					String[] columns = new String[] {KEY_ID, KEY_TITLE, KEY_VALUE, KEY_CATEGORY, KEY_DATE};
					csv.writeNext(columns);

					// load all the fill-ups
					SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/app.financoid/databases/financoiddb", null, SQLiteDatabase.OPEN_READONLY);
					Cursor c = db.query(TABLE_NAME, new String[] {KEY_TITLE, KEY_VALUE, KEY_CATEGORY, KEY_DATE}, null, null, null, null, null);
					c.moveToFirst();
					while (!c.isAfterLast()) {
						//Transaction f = new Transaction((CalculationEngine) null, c);
						String[] row = new String[] {c.getString(0), c.getString(1), c.getString(2), c.getString(3)};
						// write each fill-up
						csv.writeNext(row);

						c.moveToNext();
					}
					c.close();
					db.close();
					csv.close();
					m_handler.post(new Runnable() {
						public void run() {
							Bundle data = new Bundle();
							data.putString(MESSAGE, getString(R.string.export_finished_msg) + "\n" + getFilename());
							data.putString(TITLE, getString(R.string.success));
							data.putBoolean(SUCCESS, true);

							Message msg = new Message();
							msg.setData(data);
							m_handler.handleMessage(msg);
						}
					});
				} catch (final IOException e) {
					e.printStackTrace();
					m_handler.post(new Runnable() {
						public void run() {
							Bundle data = new Bundle();
							data.putString(MESSAGE, e.getMessage());
							data.putString(TITLE, getString(R.string.error));
							data.putBoolean(SUCCESS, false);

							Message msg = new Message();
							msg.setData(data);
							m_handler.handleMessage(msg);
						}
					});
					return;
				}
			}
		};
	}
	
	public String[] toCSV(final String[] columns, final String[] row) {
		final int size = columns.length;
		String[] data = new String[size];
		String col;
		for (int i = 0; i < size; i++) {
			col = columns[i];

			if (col.equals(KEY_ID)){
				data[i] = row[i];
			} else if (col.equals(KEY_TITLE)) {
				data[i] = row[i];
			} else if (col.equals(KEY_VALUE)) {
				data[i] = row[i];
			} else if (col.equals(KEY_CATEGORY)) {
				data[i] = row[i];
			} else if (col.equals(KEY_DATE)) {
				data[i] = row[i];
			}
		}
		return data;
	}
}
