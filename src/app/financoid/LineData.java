package app.financoid;

import java.util.Calendar;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.webkit.WebView;

public class LineData {

	private WebView mAppView;

	public LineData(WebView appView) {
		this.mAppView = appView;
	}

	// public String get() {
	// Random rand = new Random();
	// String ret = "[";
	// for(int i = 0; i < 7; i++) {
	// ret += "[" + i + "," + rand.nextInt(12) + "]";
	// if(i != 6) {
	// ret += ",";
	// }
	// }
	// ret += "]";
	// Log.d("graphdebug", ret);
	//			
	// return ret;
	// }

	public void loadGraph() {
		JSONArray arr = new JSONArray();

		JSONObject result = new JSONObject();
		JSONObject resultMore = new JSONObject();
		
		try {
			Log.d("Time before data input:", " " + SystemClock.uptimeMillis());
			result.put("data", getRawDataJSON());
			result.put("lines", getLineOptionsJSON());
			result.put("points", getFalseJSON());
			result.put("legend", getLegendJSON());
			arr.put(result);
			
			resultMore.put("data", getRawDataJSON());
			resultMore.put("lines", getLineOptionsJSON());
			resultMore.put("points", getFalseJSON());
 			resultMore.put("legend", getLegendJSON());
			arr.put(resultMore);

		} catch (Exception ex) {
			//
		}
		Log.d("Time after data input:", " " + SystemClock.uptimeMillis());
		String ret = "var data = " + arr.toString() + ";";
		Log.d("graphdebug", ret);
		mAppView.loadUrl("javascript:GotGraph(" + arr.toString() + ")");

	}
	
	private JSONArray getRawDataJSON() {
		Random rand = new Random();
		JSONArray arr = new JSONArray();
		int priorval = rand.nextInt(50);
		for (int i = 0; i < 50; i++) {
			JSONArray elem = new JSONArray();
			elem.put(i);
			if(rand.nextBoolean()) {
				priorval += rand.nextInt(20);
			} else {
				priorval -= rand.nextInt(20);
			}
			elem.put(priorval);
			arr.put(elem);
		}
		return arr;
	}

	private JSONObject getLineOptionsJSON() {
		JSONObject ret = new JSONObject();
		try {
			ret.put("show", true);
		} catch (Exception ex) {

		}
		return ret;
	}
	
	private JSONObject getFalseJSON() {
		JSONObject ret = new JSONObject();
		try {
			ret.put("show", true);
		} catch (Exception ex) {

		}
		return ret;
	}
	
	private JSONObject getLegendJSON() {
		JSONObject ret = new JSONObject();
		try {
			ret.put("show", true);
		} catch (Exception ex) {
			
		}
		return ret;
	}

	public String getGraphTitle() {
		return "";
	}

	//THIS ALSO WORKS!!
	public String getGraphTitle(String arg, int intarg) {
		return "my line baby: " + arg.length() * intarg;
	}

}
