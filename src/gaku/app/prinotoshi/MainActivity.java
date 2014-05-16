package gaku.app.prinotoshi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Process;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class MainActivity extends Activity {

	public ImageView ITEM;			// アイコン
	public TextView NAME;			// アイテム名
	public TextView DESC;			// アイテム効果
	public TextView UNLOCK;			// 解放条件
	public String selected = "";	// 選択中のアイテム

	// 装備アイテム
	public ImageView SET1;
	public ImageView SET2;
	public ImageView SET3;
	public ImageView[] ITEMS; //セット中のアイテム
	public String[] SETS={"none","none","none"};

	// アイテム
	public ImageView item1;
	public ImageView item2;
	public ImageView item3;
	public ImageView item4;
	public ImageView item5;
	public ImageView item6;
	public ImageView item7;
	public ImageView item8;

	// カウントビュー
	public TextView COUNT1;
	public TextView COUNT2;
	public TextView COUNT3;
	public TextView COUNT4;
	public TextView COUNT5;
	public TextView COUNT6;
	public TextView COUNT7;
	public TextView COUNT8;

	// 回復時間
	public TextView TIME;
	public Timer timer;
	public TimerTask timerTask;
	
	// アイテム所持数
	public int count1;
	public int count2;
	public int count3;
	public int count4;
	public int count5;
	public int count6;
	public int count7;
	public int count8;

	// スタミナ
	public int star;

	// プリファレンス
	public SharedPreferences pref;
	public SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("create","create");
    }

    @Override
    public void onResume(){
    	super.onResume();

        getObject();	// オブジェクトの生成
        setScore();		// スコア
        readStar();		// スタミナ取得
        setStar();		// スタミナ
        setTime();		// 時間
        setLock();		// アイテムLock
        readItem();		// アイテム所持数の取得
        setItem();		//　アイテム個数表示
    }
    @Override
    public void onRestart(){
    	super.onRestart();
    	
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setClassName( "gaku.app.prinotoshi","gaku.app.prinotoshi.MainActivity");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		//finish();
    }

    @Override
    public void onPause(){
    	super.onPause();
    	saveData("finish");
    }

    @Override
    public void onStop(){
    	super.onStop();
    	saveData("finish");
    }

    @Override
    public void onDestroy(){
    	super.onDestroy();
    	saveData("finish");
    }

    /*******************************
     * データ取得・表示処理			   *
     *******************************/

    // オブジェクトの取得・生成
    public void getObject(){
        ITEM = (ImageView)findViewById(R.id.item0);
        NAME = (TextView)findViewById(R.id.name);
        DESC = (TextView)findViewById(R.id.desc);
        UNLOCK = (TextView)findViewById(R.id.unlock);
        SET1 = (ImageView)findViewById(R.id.set1);
        SET2 = (ImageView)findViewById(R.id.set2);
        SET3 = (ImageView)findViewById(R.id.set3);
        item1 = (ImageView)findViewById(R.id.item1);
        item2 = (ImageView)findViewById(R.id.item2);
        item3 = (ImageView)findViewById(R.id.item3);
        item4 = (ImageView)findViewById(R.id.item4);
        item5 = (ImageView)findViewById(R.id.item5);
        item6 = (ImageView)findViewById(R.id.item6);
        item7 = (ImageView)findViewById(R.id.item7);
        item8 = (ImageView)findViewById(R.id.item8);
        COUNT1 = (TextView)findViewById(R.id.count1);
        COUNT2 = (TextView)findViewById(R.id.count2);
        COUNT3 = (TextView)findViewById(R.id.count3);
        COUNT4 = (TextView)findViewById(R.id.count4);
        COUNT5 = (TextView)findViewById(R.id.count5);
        COUNT6 = (TextView)findViewById(R.id.count6);
        COUNT7 = (TextView)findViewById(R.id.count7);
        COUNT8 = (TextView)findViewById(R.id.count8);
        TIME = (TextView)findViewById(R.id.time);
        ITEMS=new ImageView[]{SET1,SET2,SET3};
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    }
    
    // トータルハイスコアと、当日ハイスコアの表示
    public void setScore(){

    	// さいこうスコアの取得・表示
    	TextView totalScore = (TextView)findViewById(R.id.totalScore);
    	int total_score = pref.getInt("totalScore", 0);
    	totalScore.setText(Integer.toString(total_score) + "個");

    	Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String nowTime = sdf.format(date);
    	
    	// ほんじつスコアの取得・表示
    	TextView todayScore = (TextView)findViewById(R.id.todayScore);
    	
		// 本日スコアの取得
		int today_Score = pref.getInt("todayScore", 0);
		String today = pref.getString("today", nowTime);
		if(!today.equals(nowTime)){
	    	todayScore.setText("0");
			today_Score = 0;
		}else{
	    	todayScore.setText(Integer.toString(today_Score) + "個");
		}
    	
    }

    // スタミナ取得
    public void readStar(){
    	star = pref.getInt("star", 6);
    }

    // スタミナ画像セット
    public void setStar(){
    	ImageView starView = (ImageView)findViewById(R.id.star);
    	switch(star){
    	case 0:
    		starView.setImageResource(R.drawable.star0);
    		break;
    	case 1:
    		starView.setImageResource(R.drawable.star1);
    		break;
    	case 2:
    		starView.setImageResource(R.drawable.star2);
    		break;
    	case 3:
    		starView.setImageResource(R.drawable.star3);
    		break;
    	case 4:
    		starView.setImageResource(R.drawable.star4);
    		break;
    	case 5:
    		starView.setImageResource(R.drawable.star5);
    		break;
    	case 6:
    		starView.setImageResource(R.drawable.star6);
    		break;
    	}
    }
    
    public void setTime(){
		if(star != 6){
			TIME.setAlpha(1);
			timer = new Timer();
			timerTask = new timerTask(MainActivity.this);
			timer.scheduleAtFixedRate(timerTask, 0, 1000);
		}
    }

    //　アイテムロック
    public void setLock(){
    	int total_score = pref.getInt("totalScore", 0);

    	// さいこうスコアの値に応じて、アイテムにロックをかける
    	if(50 > total_score){
    		item1.setImageResource(R.drawable.lock);
    		COUNT1.setAlpha(0);
    	}
    	if(100 > total_score){
    		item2.setImageResource(R.drawable.lock);
    		COUNT2.setAlpha(0);
    	}
    	if(200 > total_score){
    		item3.setImageResource(R.drawable.lock);
    		COUNT3.setAlpha(0);
    	}
    	if(250 > total_score){
    		item4.setImageResource(R.drawable.lock);
    		COUNT4.setAlpha(0);
    	}
    	if(300 > total_score){
    		item5.setImageResource(R.drawable.lock);
    		COUNT5.setAlpha(0);
    	}
    	if(500 > total_score){
    		item6.setImageResource(R.drawable.lock);
    		COUNT6.setAlpha(0);
    	}
    	if(750 > total_score){
    		item7.setImageResource(R.drawable.lock);
    		COUNT7.setAlpha(0);
    	}
    	if(1000 > total_score){
    		item8.setImageResource(R.drawable.lock);
    		COUNT8.setAlpha(0);
    	}

    }

    // アイテム所持数の取得
    public void readItem(){

    	count1 = pref.getInt("count1", 0);
    	count2 = pref.getInt("count2", 0);
    	count3 = pref.getInt("count3", 0);
    	count4 = pref.getInt("count4", 0);
    	count5 = pref.getInt("count5", 0);
    	count6 = pref.getInt("count6", 0);
    	count7 = pref.getInt("count7", 0);
    	count8 = pref.getInt("count8", 0);

    }

    // アイテム所持数の表示
    public void setItem(){

    	COUNT1.setText(Integer.toString(count1));
    	COUNT2.setText(Integer.toString(count2));
    	COUNT3.setText(Integer.toString(count3));
    	COUNT4.setText(Integer.toString(count4));
    	COUNT5.setText(Integer.toString(count5));
    	COUNT6.setText(Integer.toString(count6));
    	COUNT7.setText(Integer.toString(count7));
    	COUNT8.setText(Integer.toString(count8));

    }


    /********************************
     * 各機能						*
     ********************************/

    // スタミナ全回復
    public void recovery(View v){

    	star = 6;
		setStar();
		editor = pref.edit();
		editor.putInt("totalScore",1000);
		editor.commit();
    }

    // アイテムの購入
    public void itemBuy(View v){
    	// さいこうスコアの取得
    	int total_score = pref.getInt("totalScore", 0);

    	if(selected.equals("muteki")){

    		// ロック解除されていなければメソッド終了
    		if(50 > total_score){
    			return;
    		}
    		count1 = buy(count1);
    		setItem();
    		saveData("buy");

    	}else if(selected.equals("double1")){

    		// ロック解除されていなければメソッド終了
    		if(100 > total_score){
    			return;
    		}
    		count2 = buy(count2);
    		setItem();
    		saveData("buy");

    	}else if(selected.equals("up10")){

    		// ロック解除されていなければメソッド終了
    		if(200 > total_score){
    			return;
    		}
    		count3 = buy(count3);
    		setItem();
    		saveData("buy");

    	}else if(selected.equals("add5")){

    		// ロック解除されていなければメソッド終了
    		if(250 > total_score){
    			return;
    		}
    		count4 = buy(count4);
    		setItem();
    		saveData("buy");

    	}else if(selected.equals("purin5")){

    		// ロック解除されていなければメソッド終了
    		if(300 > total_score){
    			return;
    		}
    		count5 = buy(count5);
    		setItem();
    		saveData("buy");

    	}else if(selected.equals("add1")){

    		// ロック解除されていなければメソッド終了
    		if(500 > total_score){
    			return;
    		}
    		count6 = buy(count6);
    		setItem();
    		saveData("buy");

    	}else if(selected.equals("up20")){

    		// ロック解除されていなければメソッド終了
    		if(750 > total_score){
    			return;
    		}
    		count7 = buy(count7);
    		setItem();
    		saveData("buy");

    	}else if(selected.equals("resurrection")){

    		// ロック解除されていなければメソッド終了
    		if(1000 > total_score){
    			return;
    		}
    		count8 = buy(count8);
    		setItem();
    		saveData("buy");

    	}
    }

    // 購入処理
    public int buy(int selectedItem){
    	if(5 <= star){
    		star -= 5;
    		setStar();
    		selectedItem += 1;
    		return selectedItem;
    	}
		return 0;
    }

    // アイテムの選択処理
    public void select(View view){
    	switch(view.getId()){
    	case R.id.item1:
    		setDesc(R.drawable.muteki5,R.string.item1,"muteki",R.string.desc1,R.string.unlock1);
    		break;
    	case R.id.item2:
    		setDesc(R.drawable.double1,R.string.item2,"double1",R.string.desc2,R.string.unlock2);
    		break;
    	case R.id.item3:
    		setDesc(R.drawable.up10,R.string.item3,"up10",R.string.desc3,R.string.unlock3);
    		break;
    	case R.id.item4:
    		setDesc(R.drawable.add5,R.string.item4,"add5",R.string.desc4,R.string.unlock4);
    		break;
    	case R.id.item5:
    		setDesc(R.drawable.purin5,R.string.item5,"purin5",R.string.desc5,R.string.unlock5);
    		break;
    	case R.id.item6:
    		setDesc(R.drawable.add1,R.string.item6,"add1",R.string.desc6,R.string.unlock6);
    		break;
    	case R.id.item7:
    		setDesc(R.drawable.up20,R.string.item7,"up20",R.string.desc7,R.string.unlock7);
    		break;
    	case R.id.item8:
    		setDesc(R.drawable.resurrection,R.string.item8,"resurrection",R.string.desc8,R.string.unlock8);
    		break;
    	}
    }

    // アイテム(名前・説明・解放条件・画像条件)の取得表示
    public void setDesc(int res,int name,String saveName,int desc,int unlock){

    	// 選択されたアイテム情報を各ビューに表示
		ITEM.setImageResource(res);
		NAME.setText(name);
		DESC.setText(desc);
		UNLOCK.setText(unlock);

		// 選択済みのアイテムをもういちどタップしたらアイテム欄にアイテムをセット
		if(selected.equals(saveName)){
			setItem(saveName,res);
		}

		// 選択済みアイテムにアイテム名を保存
		selected = saveName;
    }

    // アイテム使用欄にアイテムをセットする
    public void setItem(String name,int res){

    	for(int i=0; i<3; i++){

    		// もしアイテムがすでにセットされていたらアイテムを外してメソッド終了
    		if(SETS[i].equals(name)){
    			cancelItem(name);
    			SETS[i]="none";
    			ITEMS[i].setImageResource(R.drawable.none);
    			return;
    		}
    	}

		for(int i=0; i<3; i++){

			// アイテムがセットされていないので空白の欄があればアイテムをセット
		 	if(SETS[i].equals("none")){
		 		useItem(name,i,res);

		 		break;
		 	}
		}
    }

    // アイテム消費
    public void useItem(String name,int i, int res){
		if(name.equals("muteki")){
			if(0 < count1){
				count1 -= 1;
    			setItem();
		 		SETS[i] = name;
		 		ITEMS[i].setImageResource(res);
			}
		}
		if(name.equals("double1")){
			if(0 < count2){
				count2 -= 1;
    			setItem();
    			SETS[i] = name;
		 		ITEMS[i].setImageResource(res);
			}
		}
		if(name.equals("up10")){
			if(0 < count3){
				count3 -= 1;
    			setItem();
    			SETS[i] = name;
		 		ITEMS[i].setImageResource(res);
			}
		}
		if(name.equals("add5")){
			if(0 < count4){
				count4 -= 1;
    			setItem();
    			SETS[i] = name;
		 		ITEMS[i].setImageResource(res);
			}
		}
		if(name.equals("purin5")){
			if(0 < count5){
				count5 -= 1;
    			setItem();
    			SETS[i] = name;
		 		ITEMS[i].setImageResource(res);
			}
		}
		if(name.equals("add1")){
			if(0 < count6){
				count6 -= 1;
    			setItem();
    			SETS[i] = name;
		 		ITEMS[i].setImageResource(res);
			}
		}
		if(name.equals("up20")){
			if(0 < count7){
				count7 -= 1;
    			setItem();
    			SETS[i] = name;
		 		ITEMS[i].setImageResource(res);
			}
		}
		if(name.equals("resurrection")){
			if(0 < count8){
				count8 -= 1;
    			setItem();
    			SETS[i] = name;
		 		ITEMS[i].setImageResource(res);
			}
		}
    }

    // アイテムキャンセル
    public void cancelItem(String name){
		if(name.equals("muteki")){
			count1 += 1;
   			setItem();
		}
		if(name.equals("double1")){
			count2 += 1;
   			setItem();
		}
		if(name.equals("up10")){
			count3 += 1;
   			setItem();
		}
		if(name.equals("add5")){
			count4 += 1;
    		setItem();
		}
		if(name.equals("purin5")){
			count5 += 1;
   			setItem();
		}
		if(name.equals("add1")){
			count6 += 1;
   			setItem();
		}
		if(name.equals("up20")){
			count7 += 1;
   			setItem();
		}
		if(name.equals("resurrection")){
			count8 += 1;
   			setItem();
		}
    }

    // スタミナ消費(ゲーム開始時)
    public void consumeStar(View v){

    	if(star!=0){
    		star -= 1;
    		Start(v);
    	}
    }

    // ゲームスタート
    public void Start(View view){

    	saveData("start");

		view.setBackgroundDrawable(null);
		if(view instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup)view;
			int size = vg.getChildCount();
			for(int i = 0; i < size; i++) {
				Start(vg.getChildAt(i));
			}
		}

		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setClassName( "gaku.app.prinotoshi","gaku.app.prinotoshi.StartActivity");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

    public void saveData(String state){
    	if(state.equals("start")){
    		
    		Long nowTime = System.currentTimeMillis();
    		
    		editor = pref.edit();

    		editor.putInt("count1", count1);
    		editor.putInt("count2", count2);
    		editor.putInt("count3", count3);
    		editor.putInt("count4", count4);
    		editor.putInt("count5", count5);
    		editor.putInt("count6", count6);
    		editor.putInt("count7", count7);
    		editor.putInt("count8", count8);

    		editor.putInt("star", star);
    		if(star==5){
    			editor.putLong("play", nowTime);
    		}

    		editor.commit();
    	}else if(state.equals("buy")){
    		editor = pref.edit();

    		editor.putInt("star", star);
    		
    		editor.putInt("count1", count1);
    		editor.putInt("count2", count2);
    		editor.putInt("count3", count3);
    		editor.putInt("count4", count4);
    		editor.putInt("count5", count5);
    		editor.putInt("count6", count6);
    		editor.putInt("count7", count7);
    		editor.putInt("count8", count8);

    		editor.commit();
    	}else if(state.equals("finish")){
    		editor = pref.edit();

    		editor.putInt("star", star);

    		editor.commit();
    	}else if(state.equals("recovery")){
    		Long nowTime = System.currentTimeMillis();
    		
    		editor = pref.edit();
    		
    		editor.putInt("star", star);
    		editor.putLong("play", nowTime);
    		
    		editor.commit();
    	}
    }

    // メニュー生成
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "Update");

		return true;
	}

	// メニューリスナー
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/* webView邵ｺ�ｽｫlayout.xml邵ｺ�ｽｮwebview郢ｧ蛛ｵ縺晉ｹ晢ｿｽ郢晢ｿｽ */
		// WebView myWebView = (WebView)findViewById(R.id.webview);
		super.onOptionsItemSelected(item);
		int itemId = item.getItemId();
		switch (itemId) {
		case 0:

			StrictMode
			.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
			.permitAll().build());

			String url0 = "http://pppnexus.ddo.jp/prinotoshi.apk";
			download(url0);

			break;
		}
		Toast.makeText(this, "Selected Item: " + item.getTitle(),
				Toast.LENGTH_SHORT).show();
		return true;
	}

	// アップデート機能
	public void download(String apkurl) {

		try {
			// URL
			URL url = new URL(apkurl);

			// HTTP
			HttpURLConnection c = (HttpURLConnection) url.openConnection();
			c.setRequestMethod("GET");
			c.connect();
			// SD
			String PATH = Environment.getExternalStorageDirectory()
					+ "/download/";
			File file = new File(PATH);
			file.mkdirs();

			File outputFile = new File(file, "app.apk");
			FileOutputStream fos = new FileOutputStream(outputFile);

			InputStream is = c.getInputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			fos.close();
			is.close();

			// Intent
			Intent intent = new Intent(Intent.ACTION_VIEW);
			// MIME type
			intent.setDataAndType(
					Uri.fromFile(new File(Environment
							.getExternalStorageDirectory()
							+ "/download/"
							+ "app.apk")),
					"application/vnd.android.package-archive");
			// Intent
			startActivity(intent);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	  @Override
	  public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode==KeyEvent.KEYCODE_BACK){
	    	moveTaskToBack(true);
	    	return true;
	    }
	    return false;
	  }
	  
	  public class timerTask extends TimerTask{
		
		  private Handler handler;
		  private Context context;
		  private Toast toast;
		  
		  public timerTask(Context context){
			  
			  handler = new Handler();
			  this.context = context;

		  }
		  
		  @Override
		  public void run() {
			  
			  handler.post(new Runnable(){
				  @Override
				  public void run(){
					  
					  Long nowTime = System.currentTimeMillis();
					  Long savedTime = pref.getLong("play", 0);
					  
					  Long diff = 600 - ((nowTime - savedTime)/1000);
					  
					  Long second = diff % 60;
					  Long minute = (diff-second) / 60;
					  String s = Long.toString(second);
					  String m = Long.toString(minute);
					  if(s.length() == 1){
						  s = "0" + s;
					  }
					  String sa = m + ":" + s;
					  TIME.setText("+1補充まであと" + sa + "残っています");
					  
					  if(diff <= 0){
						  star += 1;
						  setStar();
						  saveData("recovery");
						  if(star == 6){
							  timer.cancel();
							  TIME.setAlpha(0);
						  }
					  }
				  }
			  });
		  }
		  
	  }
}