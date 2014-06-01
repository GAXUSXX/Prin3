package gaku.app.prinotoshi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Facebook extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranking);
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String score = pref.getString("score", "0");
		String URL = "http://zakuran.cranky.jp/pokipurin/login.php?score=" + score;
		
		WebView myWebView = (WebView)findViewById(R.id.webView1);
		myWebView.setWebViewClient(new WebViewClient());
		myWebView.getSettings().setDefaultTextEncodingName("Shift-JIS");
		myWebView.getSettings().setJavaScriptEnabled(true);
		myWebView.loadUrl(URL);	
	}

	
}
