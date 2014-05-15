package gaku.app.prinotoshi;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

import com.facebook.FacebookException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.android.Facebook;

import android.app.Activity;

import java.util.Date;
import java.util.List;

import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class Result extends Activity {
	public final String CONSUMER_KEY = "KfRlaUS74T6Ea9YQ1qGXUdVmX";
	public final String CONSUMER_SECRET = "6Paic5Rsq6JV07DasFK9hyAiDCtRonIA71p7l8tnuFSkLqCqhL";
	private Facebook facebook = null;
	private UiLifecycleHelper uiHelper;
	private SharedPreferences pref;

	public void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String score = pref.getString("score", "0");
		TextView scoretext = (TextView) findViewById(R.id.scoreText);
		scoretext.setText(score);

	    try {
	        PackageInfo info = getPackageManager().getPackageInfo(
	                "gaku.app.prinotoshi",
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature :info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            }
	    } catch (NameNotFoundException e) {

	    } catch (NoSuchAlgorithmException e) {

	    }
	    
	    // FacebookSDKの起動
		uiHelper = new UiLifecycleHelper(this, new Session.StatusCallback() {

            @Override
            public void call(Session session, SessionState state, Exception exception) {
                Log.i("Activity", "SessionState : " + state);
            }
        });
        uiHelper.onCreate(savedInstanceState);
        
		uiHelper = new UiLifecycleHelper(this, new Session.StatusCallback() {

            @Override
            public void call(Session session, SessionState state, Exception exception) {
                Log.i("Activity", "SessionState : " + state);
            }
        });
        uiHelper.onCreate(savedInstanceState);
        saveScore();
	}

	public void saveScore(){

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String nowTime = sdf.format(date);
		
		int score = Integer.valueOf(pref.getString("score", "0"));
		
		// 最高スコアの取得
		int totalScore = pref.getInt("totalScore",0);
		if(totalScore < score){
			totalScore = score;
		}
		
		// 本日スコアの取得
		int todayScore = pref.getInt("todayScore", 0);
		String today = pref.getString("today", nowTime);
		if(!today.equals(nowTime)){
			today = nowTime;
			todayScore = score;
		}else{
			if(todayScore < score){
				todayScore = score;
			}
		}
		
		// データ保存
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt("totalScore", totalScore);
		editor.putInt("todayScore", todayScore);
		editor.putString("today", today);
		editor.commit();
	}
	
	public void Line(View view){
		// ライン
		Uri uri = Uri.parse("line://msg/text/test");
		Intent intent = new Intent(Intent.ACTION_VIEW,uri);
		startActivity(intent);
	}

	public void Twitter(View view){
		Uri uri = Uri.parse("https://twitter.com/intent/tweet?text=testt");
		Intent intent = new Intent(Intent.ACTION_VIEW,uri);
		startActivity(intent);
	}

	public void Facebook(View view){
		if (FacebookDialog.canPresentShareDialog(getApplicationContext(),
                FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            try {
                String name = "POKIPURI";
                String url = "http://dummy.com/";
                String description = "Test";

                // Fragment シェア画面の開く
                FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this).setDescription(description)
                        .setName(name).setLink(url).build();
                uiHelper.trackPendingDialogCall(shareDialog.present());
            } catch (FacebookException e) {
                Toast.makeText(this, "Facebook", Toast.LENGTH_SHORT).show();
            }
        }
	}

	public void Retry(View view){
    	//リトライ
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName( "gaku.app.prinotoshi","gaku.app.prinotoshi.StartActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Result.this.finish();
    }

	public void toMenu(View view){
    	//メニュー
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName( "gaku.app.prinotoshi","gaku.app.prinotoshi.MainActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Result.this.finish();
    }
	  @Override
	  public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode==KeyEvent.KEYCODE_BACK){
	    	
	        Intent intent = new Intent(Intent.ACTION_MAIN);
	        intent.setClassName( "gaku.app.prinotoshi","gaku.app.prinotoshi.MainActivity");
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        startActivity(intent);
	        Result.this.finish();
	        
	    	return false;
	    }
	    return false;
	  }
	
}
