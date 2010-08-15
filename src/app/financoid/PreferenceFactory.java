package app.financoid;

import java.text.DecimalFormat;

import android.content.Context;
import android.content.SharedPreferences;

//import app.financoid.CalculationEngine;

public class PreferenceFactory {
	private static PreferenceFactory s_instance = null;
	private Context m_activity;
	private SharedPreferences m_settings;
	//private CalculationEngine m_calcEngine;

	public static final String DATE = "date_format_pref";
	public static final String NUMBER = "number_pref";
	public static final String CURRENCY = "currency_pref";
	public static final String VALUE = "value_pref";
	public static final String EXPORT = "export_pref";

	private static final String PREFS_NAME = "app.financoid.financoid_preferences";
	private static final int BOOLEAN = 0;
	private static final int FLOAT = 1;
	private static final int INT = 2;
	private static final int LONG = 3;
	private static final int STRING = 4;

	public static final int EUROS = 0;
	public static final int DOLLARS = 1;
	public static final int POUNDS = 2;

	public static final int EXPORT_TO_SD = 0;
	public static final int EXPORT_TO_MAIL = 1;

	private PreferenceFactory(Context context) {
		m_activity = context;
		m_settings = m_activity.getSharedPreferences(PREFS_NAME, 0);
	}

	public static PreferenceFactory getInstance(Context context) {
		if (s_instance == null) {
			s_instance = new PreferenceFactory(context);
		}
		return s_instance;
	}

	public boolean write(String key, boolean value) {
		return write(key, value, BOOLEAN);
	}

	public boolean write(String key, float value) {
		return write(key, value, FLOAT);
	}

	public boolean write(String key, int value) {
		return write(key, value, INT);
	}

	public boolean write(String key, long value) {
		return write(key, value, LONG);
	}

	public boolean write(String key, String value) {
		return write(key, value, STRING);
	}

	private boolean write(String key, Object value, int type) {
		SharedPreferences.Editor editor = m_settings.edit();
		switch (type) {
			case BOOLEAN:
				editor.putBoolean(key, (Boolean) value);
				break;
			case FLOAT:
				editor.putFloat(key, (Float) value);
				break;
			case INT:
				editor.putInt(key, (Integer) value);
				break;
			case LONG:
				editor.putLong(key, (Long) value);
				break;
			case STRING:
				editor.putString(key, (String) value);
				break;
			default:
				throw new IllegalArgumentException("Unrecognized type: " + value.toString());
		}
		return editor.commit();
	}

	public boolean getBoolean(String key, boolean defValue) {
		return m_settings.getBoolean(key, defValue);
	}

	public float getFloat(String key, float defValue) {
		return m_settings.getFloat(key, defValue);
	}

	public int getInt(String key, int defValue) {
		try {
			return m_settings.getInt(key, defValue);
		} catch (ClassCastException ce) {
			return Integer.parseInt(m_settings.getString(key, String.valueOf(defValue)));
		}
	}

	public long getLong(String key, long defValue) {
		return m_settings.getLong(key, defValue);
	}

	public String getString(String key, String defValue) {
		return m_settings.getString(key, defValue);
	}

	public String getString(int array, String key) {
		try {
			int index = getInt(key, 0);
			String data = m_activity.getResources().getStringArray(array)[index];
			return data;
		} catch (IndexOutOfBoundsException e) {
			return "";
		}
	}

	public String getCurrency() {
		return getString(CURRENCY, "�");
	}

	/*public String getVolume() {
		return getString(R.array.units_volume, VOLUME);
	}

	public CalculationEngine getCalculator() {
		int volume = getInt(VOLUME, 0);
		int distance = getInt(DISTANCE, 0);
		int economy = getInt(ECONOMY, 0);
		m_calcEngine = getCalculator(volume, distance, economy);
		return m_calcEngine;
	}

	public CalculationEngine getCalculator(int volume, int distance, int economy) {
		if (m_calcEngine == null) {
			m_calcEngine = new CalculationEngine();
		}
		m_calcEngine.setInputVolume(volume);
		m_calcEngine.setInputDistance(distance);
		m_calcEngine.setEconomy(economy);
		switch (economy) {
			case MI_PER_GALLON:
				m_calcEngine.setOutputVolume(GALLONS);
				m_calcEngine.setOutputDistance(MILES);
				break;
			case KM_PER_GALLON:
				m_calcEngine.setOutputVolume(GALLONS);
				m_calcEngine.setOutputDistance(KILOMETERS);
				break;
			case MI_PER_LITRE:
				m_calcEngine.setOutputVolume(LITRES);
				m_calcEngine.setOutputDistance(MILES);
				break;
			case KM_PER_LITRE:
				m_calcEngine.setOutputVolume(LITRES);
				m_calcEngine.setOutputDistance(KILOMETERS);
				break;
			case MI_PER_IMP_GALLON:
				m_calcEngine.setOutputVolume(IMP_GALLONS);
				m_calcEngine.setOutputDistance(MILES);
				break;
			case KM_PER_IMP_GALLON:
				m_calcEngine.setOutputVolume(IMP_GALLONS);
				m_calcEngine.setOutputDistance(KILOMETERS);
				break;
			case LITRES_PER_CKM:
				m_calcEngine.setOutputVolume(LITRES);
				m_calcEngine.setOutputDistance(KILOMETERS_100);
				break;
			case GALLONS_PER_CKM:
				m_calcEngine.setOutputVolume(GALLONS);
				m_calcEngine.setOutputDistance(KILOMETERS_100);
				break;
			case IMP_GALLONS_PER_CKM:
				m_calcEngine.setOutputVolume(IMP_GALLONS);
				m_calcEngine.setOutputDistance(KILOMETERS_100);
				break;
		}
		return m_calcEngine;
	}*/

	public String format(double number) {
		// TODO: support this in the future
		DecimalFormat formatter = new DecimalFormat("#,##0.00");
		return formatter.format(number);
	}

	public String shortFormat(double number) {
		DecimalFormat formatter = new DecimalFormat("#,##0.##");
		return formatter.format(number);
	}
}
