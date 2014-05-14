package gaku.app.prinotoshi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

public class MainActivity extends Activity {

	public ImageView ITEM;
	public TextView NAME;
	public TextView DESC;
	public TextView UNLOCK;
	public ImageView SET1;
	public ImageView SET2;
	public ImageView SET3;
	public ImageView item1;
	public ImageView item2;
	public ImageView item3;
	public ImageView item4;
	public ImageView item5;
	public ImageView item6;
	public ImageView item7;
	public ImageView item8;
	public SharedPreferences pref;
	public SharedPreferences.Editor editor;

	public String selected = "";

	public String[] SETS={"none","none","none"};
	public ImageView[] ITEMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("create","create");
        
        getObject();	// オブジェクトの生成
        setStar();		// スタミナ
        setScore();		// スコア
        setLock();		// アイテムLock
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	setStar();
    }
    
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
        ITEMS=new ImageView[]{SET1,SET2,SET3};
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    }
    
    public void recovery(View v){
    	int star = 6;
    	editor = pref.edit();
		editor.putInt("star", star);
		editor.commit();
		setStar();
    }
    
    public void setStar(){
    	int star = pref.getInt("star", 6);
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
    
    public void consumeStar(View v){
    	
    	int star = pref.getInt("star", 6);
    	
    	if(star!=0){
    		star -= 1;
    		editor = pref.edit();
    		editor.putInt("star", star);
    		editor.commit();
    		Start(v);
    	}
    }
    
    public void setScore(){
    	TextView totalScore = (TextView)findViewById(R.id.totalScore);
    	int total_score = pref.getInt("totalScore", 0);
    	totalScore.setText(Integer.toString(total_score));
    	TextView todayScore = (TextView)findViewById(R.id.todayScore);
    	int today_score = pref.getInt("todayScore", 0);
    	todayScore.setText(Integer.toString(today_score));
    }
    
    public void setLock(){
    	int total_score = pref.getInt("totalScore", 0);

    	if(50 > total_score){
    		item1.setImageResource(R.drawable.lock);
    	}
    	if(100 > total_score){
    		item2.setImageResource(R.drawable.lock);
    	}
    	if(200 > total_score){
    		item3.setImageResource(R.drawable.lock);
    	}
    	if(250 > total_score){
    		item4.setImageResource(R.drawable.lock);
    	}
    	if(300 > total_score){
    		item5.setImageResource(R.drawable.lock);
    	}
    	if(500 > total_score){
    		item6.setImageResource(R.drawable.lock);
    	}
    	if(750 > total_score){
    		item7.setImageResource(R.drawable.lock);
    	}
    	if(1000 > total_score){
    		item8.setImageResource(R.drawable.lock);
    	}
    	
    }

    public void result(View view){
    	Intent intent = new Intent(this,Result.class);
    	startActivity(intent);
    }

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
    		setDesc(R.drawable.up10,R.string.item7,"up10",R.string.desc7,R.string.unlock7);
    		break;
    	case R.id.item8:
    		setDesc(R.drawable.resurrection,R.string.item8,"resurrection",R.string.desc8,R.string.unlock8);
    		break;
    	}
    }

    public void setDesc(int res,int name,String saveName,int desc,int unlock){
		ITEM.setImageResource(res);
		NAME.setText(name);
		DESC.setText(desc);
		UNLOCK.setText(unlock);


		if(selected.equals(saveName)){
			setItem(saveName,res);
		}
		selected = saveName;
    }

    public void setItem(String name,int res){
    	for(int i=0; i<3; i++){
    		if(SETS[i].equals(name)){
    			SETS[i]="none";
    			ITEMS[i].setImageResource(R.drawable.none);
    			return;
    		}
    	}
		for(int i=0; i<3; i++){
		 	if(SETS[i].equals("none")){
		 		SETS[i] = name;
		 		ITEMS[i].setImageResource(res);
		 		break;
		 	}
		}
    }


	public void Start(View view){

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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "Update");

		return true;
	}

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

}