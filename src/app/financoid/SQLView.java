package app.financoid;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.widget.TextView;

public class SQLView extends ExportView {

	private String TABLE_NAME = "transactions";
	private String CAT_TABLE_NAME = "categories";
	
	private static HashMap<String, String> s_transactionsProjectionMap;
	private static HashMap<String, String> s_categoriesProjectionMap;
	
	private static final String KEY_ID = "_id";
    private static final String KEY_TITLE = "transaction_title";
    private static final String KEY_VALUE = "transaction_value";
    private static final String KEY_CATEGORY = "transaction_category";
    private static final String KEY_PREFIX = "transaction_category";
    private static final String KEY_DATE = "transaction_date";
    
    private static final String CAT_ID = "_id";
    private static final String CAT_TITLE = "category_name";
    private static final String CAT_DESCRIPTION = "category_description";
    
    private static final String DB_NAME = "financoiddb";
    private static final String DB_VERSION = "1.0";
	protected String getTag() {
		return "SQLExport";
	}
	
	static {
		s_transactionsProjectionMap = new HashMap<String, String>();
		s_transactionsProjectionMap.put(KEY_ID, KEY_ID);
		s_transactionsProjectionMap.put(KEY_TITLE, KEY_TITLE);
		s_transactionsProjectionMap.put(KEY_CATEGORY, KEY_CATEGORY);
		s_transactionsProjectionMap.put(KEY_PREFIX, KEY_PREFIX);
		s_transactionsProjectionMap.put(KEY_DATE, KEY_DATE);

		s_categoriesProjectionMap = new HashMap<String, String>();
		s_categoriesProjectionMap.put(CAT_ID, CAT_ID);
		s_categoriesProjectionMap.put(CAT_TITLE, CAT_TITLE);
		s_categoriesProjectionMap.put(CAT_DESCRIPTION, CAT_DESCRIPTION);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, "sql");
		m_title = (TextView) findViewById(R.id.title);
		m_title.setText(R.string.sql_file);

		super.m_exporter = new Runnable() {
			public void run() {
				HashMap<String, String> transactionsProjection = getTransactionsProjection();
				HashMap<String, String> categoriesProjection = getCategoriesProjection();

				Set<String> keySet = transactionsProjection.keySet();
				String[] proj = keySet.toArray(new String[keySet.size()]);
				SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/app.financoid/databases/financoiddb", null, SQLiteDatabase.OPEN_READONLY);
				Cursor c = db.query(TABLE_NAME, new String[] {KEY_ID, KEY_TITLE, KEY_VALUE, KEY_PREFIX, KEY_CATEGORY, KEY_DATE}, null, null, null, null, null);

				StringBuilder sb = new StringBuilder();
				sb.append("-- Exported database: ").append(DB_NAME).append("\n");
				sb.append("-- Exported version: ").append(DB_VERSION).append("\n");
				sb.append("-- Begin table: ").append(TABLE_NAME).append("\n");
				c.moveToFirst();
				while (c.isAfterLast() == false) {
					sb.append("INSERT INTO ").append(TABLE_NAME).append(" ");
					keySetToSQL(keySet, sb);
					keySetToValues(keySet, sb, c);
					c.moveToNext();
				}
				sb.append("-- End table: ").append(CAT_TABLE_NAME).append("\n");

				sb.append("-- Begin table: ").append(CAT_TABLE_NAME).append("\n");
				keySet = categoriesProjection.keySet();
				proj = keySet.toArray(new String[keySet.size()]);
				c = db.query(CAT_TABLE_NAME, new String[] {CAT_ID, CAT_TITLE, CAT_DESCRIPTION}, null, null, null, null, null);
				c.moveToFirst();
				while (c.isAfterLast() == false) {
					sb.append("INSERT INTO ").append(CAT_TABLE_NAME);
					keySetToSQL(keySet, sb);
					keySetToValues(keySet, sb, c);
					c.moveToNext();
				}
				sb.append("-- End table: ").append(CAT_TABLE_NAME).append("\n");

				c.close();
				db.close();

				// write to a file
				try {
					File output = new File(Environment.getExternalStorageDirectory() + "/" + getFilename());
					FileWriter out = new FileWriter(output);

					out.write(sb.toString());
					out.flush();
					out.close();
				} catch (final IOException e) {
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
			}

			private void keySetToSQL(Set<String> columns, StringBuilder sb) {
				sb.append(" (");
				for (String key : columns) {
					sb.append("'").append(key).append("', ");
				}
				sb.deleteCharAt(sb.length() - 1);
				sb.deleteCharAt(sb.length() - 1);
				sb.append(") ");
			}

			private void keySetToValues(Set<String> columns, StringBuilder sb, Cursor c) {
				sb.append(" VALUES (");
				int i = 1;
				for (String key : columns) {
					String val = c.getString(c.getColumnIndex(key));
					if (val == null) {
						val = "";
					}
					val = val.replaceAll("'", "\\'");
					sb.append("'").append(val).append("'");
					if (i != c.getColumnCount()) {
						sb.append(", ");
					}
					i++;
				}
				sb.append(");\n");
			}
		};
	}
	
	public static HashMap<String, String> getTransactionsProjection() {
		return s_transactionsProjectionMap;
	}

	public static HashMap<String, String> getCategoriesProjection() {
		return s_categoriesProjectionMap;
	}
	
	
}
