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
	//String redirect_uri = "http://pokipurin.info/log.php?";

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranking);
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String score = pref.getString("score", "0");
		
		
		myWebView = (WebView)findViewById(R.id.webView1);
		myWebView.setWebViewClient(new WebViewClient()/*{
			@Override
			public boolean shouldOverrideUrlLoading(WebView view_, String url_){
				super.shouldOverrideUrlLoading(view_, url_);
				if(url_.startsWith(redirect_uri)){
					String[] params = url_.substring(redirect_uri.length(),url_.length()).split("&");
					String code = (params[0].split("="))[1];
					String state = (params[1].split("="))[1];
					Log.v("URL:",url_);
					Log.v("CODE:",code);
					Log.v("STATE:",state);
					String URL = "http://pokipurin.info/log.php?";
							
					
					myWebView.loadUrl(URL);
					return false;
				}
				return super.shouldOverrideUrlLoading(view_, url_);
			}
		}*/);
		myWebView.getSettings().setDefaultTextEncodingName("Shift-JIS");
		myWebView.getSettings().setJavaScriptEnabled(true);
		myWebView.loadUrl(URL);	
	}
}
