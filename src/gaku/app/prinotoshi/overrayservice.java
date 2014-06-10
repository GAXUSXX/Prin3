package gaku.app.prinotoshi;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
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
				(int) (disp.getWidth()/4.8),
				(int) (disp.getWidth()/4.8),
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
    			SetHeight = (int) (disp.getHeight()/1.39);
    			Log.v("model",Build.MODEL);
    			if(Build.MODEL.contains("SC-04E")){
    				SetHeight = (int) (disp.getHeight()/1.36);
    			}
    			if(i == 0){
    				// 38dp に相当する px 値を取得
    		        DisplayMetrics metrics = getResources().getDisplayMetrics();
    		        int padding = (int) (metrics.density * 36);
    				SetWidth = (int) padding;
    			}
    			if(i == 1){
    				// 152dp に相当する px 値を取得
    		        DisplayMetrics metrics = getResources().getDisplayMetrics();
    		        int padding = (int) (metrics.density * 144);
    				SetWidth = (int) padding;
    			}

    			if(i == 2){
    				// 114dp に相当する px 値を取得
    		        DisplayMetrics metrics = getResources().getDisplayMetrics();
    		        int padding = (int) (metrics.density * 252);
    				SetWidth = (int) padding;
    			}
    		}
    	}
		params.y=SetHeight;
		params.x=SetWidth;

		wm.addView(chatHead, params);
	}

	private int count = 0;
    private final Context context = this;
    private final Handler handler = new Handler();
    private final Runnable showMessageTask = new Runnable() {
        @Override
        public void run() {
        	if (chatHead != null) wm.removeView(chatHead);
        }
    };

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v("service","Destroy");
		handler.postDelayed(showMessageTask, 380);
	}
}
