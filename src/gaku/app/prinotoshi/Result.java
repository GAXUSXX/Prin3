package gaku.app.prinotoshi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

import android.app.Activity;

import java.util.Arrays;
import java.util.Date;

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
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public class Result extends Activity {
	public final String APP_ID = "1447907075451856";
	public final String CONSUMER_KEY = "KfRlaUS74T6Ea9YQ1qGXUdVmX";
	public final String CONSUMER_SECRET = "6Paic5Rsq6JV07DasFK9hyAiDCtRonIA71p7l8tnuFSkLqCqhL";
	
	
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
        saveScore();
	}

	public void saveScore(){
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String nowTime = sdf.format(date);
		
		int score = Integer.valueOf(pref.getString("score", "0"));
		
		// �ō��X�R�A�̎擾
		int totalScore = pref.getInt("totalScore",0);
		if(totalScore < score){
			totalScore = score;
		}
		
		// �{���X�R�A�̎擾
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
		
		// �f�[�^�ۑ�
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt("totalScore", totalScore);
		editor.putInt("todayScore", todayScore);
		editor.putString("today", today);
		editor.commit();
	}
	
	public void Line(View view){
		// Lineに投稿する
		
		String score = pref.getString("score", "0");
		
		String URL = "line://msg/text/"
					+ "[ぽっきんプリン]" + score + "個をポキったよ！"
					+ " http://dummy.php" + " #pokipuri";
		
		
		Uri uri = Uri.parse(URL);
		Intent intent = new Intent(Intent.ACTION_VIEW,uri);
		startActivity(intent);
	}

	public void Twitter(View view){
		String score = pref.getString("score", "0");
		
		String URL = "https://twitter.com/intent/tweet?text="
					+ "[ぽっきんプリン]" + score + "個をポキったよ！"
					+ "&hashtags=" + "pokipuri" 
					+ "&url=" +"http://dummy.php";
		Uri uri = Uri.parse(URL);
		Intent intent = new Intent(Intent.ACTION_VIEW,uri);
		startActivity(intent);
	}

	public void Facebook(View view){
		
		Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName( "gaku.app.prinotoshi","gaku.app.prinotoshi.Facebook");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
		/*
		if (FacebookDialog.canPresentShareDialog(getApplicationContext(),
                FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            try {
                String name = "POKIPURI";
                String url = "http://dummy.com/";
                String description = "Test";

                FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this).setDescription(description)
                        .setName(name).setLink(url).build();
                uiHelper.trackPendingDialogCall(shareDialog.present());
            } catch (FacebookException e) {
                Toast.makeText(this, "Facebook", Toast.LENGTH_SHORT).show();
            }
        }*/
	}

	public void Retry(View view){
    	//���g���C
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName( "gaku.app.prinotoshi","gaku.app.prinotoshi.StartActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

	public void toMenu(View view){
    	//���j���[
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName( "gaku.app.prinotoshi","gaku.app.prinotoshi.MainActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
	  @Override
	  public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode==KeyEvent.KEYCODE_BACK){
	        Intent intent = new Intent(Intent.ACTION_MAIN);
	        intent.setClassName( "gaku.app.prinotoshi","gaku.app.prinotoshi.MainActivity");
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        startActivity(intent);
	        finish();

	    	return false;
	    }
	    return false;
	  }
	  
	  public void Toast(String msg){
		  Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
	  }	
	  
}
