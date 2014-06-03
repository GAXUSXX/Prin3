package gaku.app.prinotoshi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Facebook extends Activity {
	
	public WebView myWebView;
	String URL = "http://pokipurin.info/login.php";
	String scheme = "app://";

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranking);
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String score = pref.getString("score", "0");
		String URL = "http://pokipurin.info/login.php?score=" + score;
		
		
		myWebView = (WebView)findViewById(R.id.webView1);
		myWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view_, String url_){
				super.shouldOverrideUrlLoading(view_, url_);
				if(url_.startsWith(scheme)){
					String method = url_.substring(scheme.length(),url_.length());
					
					if(method.equals("end")){
						finish();
					}
					return false;
				}
				return super.shouldOverrideUrlLoading(view_, url_);
			}
		});
		myWebView.getSettings().setDefaultTextEncodingName("Shift-JIS");
		myWebView.getSettings().setJavaScriptEnabled(true);
		myWebView.loadUrl(URL);	
	}
}
