package gaku.app.prinotoshi;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;

public class overrayservice extends Service{
	private WindowManager wm;
	private ImageView chatHead;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override public void onCreate() {
		// WindowManagerを取得する
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        // ディスプレイのインスタンス生成
     	Display disp = wm.getDefaultDisplay();
     	disp.getWidth();
		chatHead = new ImageView(this);
		chatHead.setImageResource(R.drawable.resurrectionnone);
		WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				disp.getWidth()/5,
				disp.getWidth()/5,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.TOP | Gravity.LEFT;
		SharedPreferences pref2 = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String[] SETS = pref2.getString("item", "none,none,none").split(",");
        int SetWidth = 0;
        int SetHeight = 0;
    	for(int i=0;i<3;i++){
    		if(SETS[i].equals("resurrection")){
    			SetHeight = (int) (disp.getHeight()/1.38);
    			SetWidth = (int) ((disp.getWidth()/13.9) * (i+1)) * 3;
    			if(i == 1){
        			SetWidth -= disp.getWidth()/39;
        		}
    			if(i==2){
    				SetWidth += disp.getWidth()/11;
    			}
    		}
    	}
		params.y=SetHeight;
		params.x=SetWidth;

		wm.addView(chatHead, params);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v("service","Destroy");
		int i = 0;
		while(i<1000000000){
			i++;
		}
		if (chatHead != null) wm.removeView(chatHead);
	}
}
