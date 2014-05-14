package gaku.app.prinotoshi;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.facebook.FacebookException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.android.Facebook;
import android.app.Activity;
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

	public void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String score = prefs.getString("score", "0");
		TextView scoretext = (TextView) findViewById(R.id.scoreText);
		scoretext.setText(score);

		// 繧ｭ繝ｼ繝上ャ繧ｷ繝･繧貞�ｺ蜉帙☆繧九さ繝ｼ繝峨ｒ霑ｽ蜉�縺吶ｋ
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
	}

	public void Line(View view){
		// 繝ｩ繧､繝ｳ縺ｧ騾√ｋ
		Uri uri = Uri.parse("line://msg/text/test縺ｧ縺�");
		Intent intent = new Intent(Intent.ACTION_VIEW,uri);
		startActivity(intent);
	}

	public void Twitter(View view){
		Uri uri = Uri.parse("https://twitter.com/intent/tweet?text=縺ｦ縺吶→縺ｧ縺�");
		Intent intent = new Intent(Intent.ACTION_VIEW,uri);
		startActivity(intent);
	}

	public void Facebook(View view){
		if (FacebookDialog.canPresentShareDialog(getApplicationContext(),
                FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            try {
                String name = "POKIPURI";
                String url = "http://dummy.com/";
                String description = "縺薙ｌ縺ｯPOKIPURI縺ｮ繝�繧ｹ繝医〒縺吶��";

                // Fragment 縺ｧ逋ｺ陦後☆繧九→縺阪�ｯ setFragment() 繧貞他縺ｶ
                FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this).setDescription(description)
                        .setName(name).setLink(url).build();
                uiHelper.trackPendingDialogCall(shareDialog.present());
            } catch (FacebookException e) {
                Toast.makeText(this, "Facebook 縺ｧ繧ｨ繝ｩ繝ｼ縺檎匱逕溘＠縺ｾ縺励◆縲�", Toast.LENGTH_SHORT).show();
            }
        }
	}

	public void Retry(View view){
    	//繝ｪ繝医Λ繧､
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName( "gaku.app.prinotoshi","gaku.app.prinotoshi.StartActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

	public void toMenu(View view){
    	//繝｡繝九Η繝ｼ
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName( "gaku.app.prinotoshi","gaku.app.prinotoshi.MainActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}
