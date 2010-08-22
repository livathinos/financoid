package app.financoid;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class StatisticsActivity extends Activity {
	
	private WebView wv;
	final String mimeType = "text/html"; 
	final String encoding = "utf-8"; 
	
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
        setContentView(R.layout.statistics_overview);
        
        wv = (WebView) findViewById(R.id.wv1);
    }
    
    public void onResume() {
    	Log.d("Time before webview load:", " " + SystemClock.uptimeMillis());
    	LineData bd = new LineData(wv);
        //LineData bd = new LineData(wv);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.addJavascriptInterface(bd, "graphdata");
        wv.setWebChromeClient(new MyWebChromeClient());
        wv.loadUrl("file:///android_asset/plot/statistics.html");
        Log.d("Time after webview load:", " " + SystemClock.uptimeMillis());
        super.onResume();
        
    }
    
    final class MyWebChromeClient extends WebChromeClient {
        
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.d("graphdebug", message);
            result.confirm();
            return true;
        }
    }
    
}
