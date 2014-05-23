package gaku.app.prinotoshi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Ranking extends Activity {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranking);
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		
		String NAME = pref.getString("name", "NO NAME");
		String TOTAL_SCORE = Integer.toString(pref.getInt("totalScore", 0));
		String TODAY_SCORE = Integer.toString(pref.getInt("todayScore", 0));
		
		String URL = "http://zakuran.cranky.jp/pokipurin/entry.php?" +
				     "name=" + NAME + "&" +
				     "total=" + TOTAL_SCORE + "&" +
				     "today=" + TODAY_SCORE ;
		WebView myWebView = (WebView)findViewById(R.id.webView1);
		myWebView.setWebViewClient(new WebViewClient());
		myWebView.getSettings().setDefaultTextEncodingName("Shift-JIS");
		myWebView.getSettings().setJavaScriptEnabled(true);
		myWebView.loadUrl(URL);	
	}

}
