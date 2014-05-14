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
        ITEM = (ImageView)findViewById(R.id.item0);
        NAME = (TextView)findViewById(R.id.name);
        DESC = (TextView)findViewById(R.id.desc);
        UNLOCK = (TextView)findViewById(R.id.unlock);
        SET1 = (ImageView)findViewById(R.id.set1);
        SET2 = (ImageView)findViewById(R.id.set2);
        SET3 = (ImageView)findViewById(R.id.set3);
        ITEMS=new ImageView[]{SET1,SET2,SET3};
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        
        setStar();
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	setStar();
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
    	case R.id.item9:
    		setDesc(R.drawable.up20,R.string.item9,"up20",R.string.desc9,R.string.unlock9);
    		setDesc(R.drawable.up20,R.string.item7,"up20",R.string.desc7,R.string.unlock7);
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
		
		//繧ｲ繝ｼ繝�繧ｹ繧ｿ繝ｼ繝�
		//郢ｧ�ｽｲ郢晢ｽｼ郢晢ｿｽ郢ｧ�ｽｹ郢ｧ�ｽｿ郢晢ｽｼ郢晢ｿｽ
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setClassName( "gaku.app.prinotoshi","gaku.app.prinotoshi.StartActivity");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 繝｡繝九Η繝ｼ縺ｮ隕∫ｴ�繧定ｿｽ蜉�
		// 郢晢ｽ｡郢昜ｹ斟礼ｹ晢ｽｼ邵ｺ�ｽｮ髫補悪�ｽｴ�ｿｽ郢ｧ螳夲ｽｿ�ｽｽ陷会ｿｽ
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "Update");

		return true;
	}

	// 繧ｪ繝励す繝ｧ繝ｳ繝｡繝九Η繝ｼ驕ｸ謚槭＆繧後◆蝣ｴ蜷医��驕ｸ謚樣��逶ｮ縺ｫ蜷医ｏ縺帙※
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/* webView邵ｺ�ｽｫlayout.xml邵ｺ�ｽｮwebview郢ｧ蛛ｵ縺晉ｹ晢ｿｽ郢晢ｿｽ */
		// WebView myWebView = (WebView)findViewById(R.id.webview);
		super.onOptionsItemSelected(item);
		int itemId = item.getItemId();
		switch (itemId) {
		case 0:
			// 繝代ヵ繧ｩ繝ｼ繝槭Φ繧ｹ菴惹ｸ九ｒ讀懷�ｺ縺吶ｋ讖溯�ｽ繧堤┌蜉ｹ縺ｫ縺励※縺翫￥
			// 郢昜ｻ｣繝ｵ郢ｧ�ｽｩ郢晢ｽｼ郢晄ｧｭﾎｦ郢ｧ�ｽｹ闖ｴ諠ｹ�ｽｸ荵晢ｽ定ｮ�諛ｷ�ｿｽ�ｽｺ邵ｺ蜷ｶ�ｽ玖ｮ匁ｺｯ�ｿｽ�ｽｽ郢ｧ蝣､笏瑚怏�ｽｹ邵ｺ�ｽｫ邵ｺ蜉ｱ窶ｻ邵ｺ鄙ｫ�ｿ･
			StrictMode
			.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
			.permitAll().build());

			String url0 = "http://pppnexus.ddo.jp/prinotoshi.apk";
			// 繝�繧ｦ繝ｳ繝ｭ繝ｼ繝峨�ｻ繧､繝ｳ繧ｹ繝医�ｼ繝ｫ髢句ｧ�
			// 郢晢ｿｽ郢ｧ�ｽｦ郢晢ｽｳ郢晢ｽｭ郢晢ｽｼ郢晏ｳｨ�ｿｽ�ｽｻ郢ｧ�ｽ､郢晢ｽｳ郢ｧ�ｽｹ郢晏現�ｿｽ�ｽｼ郢晢ｽｫ鬮｢蜿･�ｽｧ�ｿｽ
			download(url0);

			break;
		}
		Toast.makeText(this, "Selected Item: " + item.getTitle(),
				Toast.LENGTH_SHORT).show();
		return true;
	}

	/**
<<<<<<< HEAD
	 * 繝�繧ｦ繝ｳ繝ｭ繝ｼ繝峨�ｻ繧､繝ｳ繧ｹ繝医�ｼ繝ｫ繝｡繧ｽ繝�繝�
=======
	 * 郢晢ｿｽ郢ｧ�ｽｦ郢晢ｽｳ郢晢ｽｭ郢晢ｽｼ郢晏ｳｨ�ｿｽ�ｽｻ郢ｧ�ｽ､郢晢ｽｳ郢ｧ�ｽｹ郢晏現�ｿｽ�ｽｼ郢晢ｽｫ郢晢ｽ｡郢ｧ�ｽｽ郢晢ｿｽ郢晢ｿｽ
>>>>>>> a14d7fbb98efacb3ee483ce18e9a4529fd4df01a
	 */
	public void download(String apkurl) {

		try {
			// URL險ｭ螳�
			URL url = new URL(apkurl);

			// HTTP隰暦ｽ･驍ｯ螟仙ｹ戊沂�ｿｽ
			HttpURLConnection c = (HttpURLConnection) url.openConnection();
			c.setRequestMethod("GET");
			c.connect();
			// SD郢ｧ�ｽｫ郢晢ｽｼ郢晏ｳｨ�ｿｽ�ｽｮ髫ｪ�ｽｭ陞ｳ�ｿｽ
			String PATH = Environment.getExternalStorageDirectory()
					+ "/download/";
			File file = new File(PATH);
			file.mkdirs();


			// 繝�繧ｦ繝ｳ繝ｭ繝ｼ繝蛾幕蟋�
			// 郢晢ｿｽ郢晢ｽｳ郢晄亢ﾎ帷ｹ晢ｽｪ郢晁ｼ斐＜郢ｧ�ｽ､郢晢ｽｫ邵ｺ�ｽｮ髫ｪ�ｽｭ陞ｳ�ｿｽ
			File outputFile = new File(file, "app.apk");
			FileOutputStream fos = new FileOutputStream(outputFile);

			// 郢晢ｿｽ郢ｧ�ｽｦ郢晢ｽｳ郢晢ｽｭ郢晢ｽｼ郢晁崟蟷�
			InputStream is = c.getInputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			fos.close();
			is.close();

			// Intent騾墓ｻ難ｿｽ�ｿｽ
			Intent intent = new Intent(Intent.ACTION_VIEW);
			// MIME type髫ｪ�ｽｭ陞ｳ�ｿｽ
			intent.setDataAndType(
					Uri.fromFile(new File(Environment
							.getExternalStorageDirectory()
							+ "/download/"
							+ "app.apk")),
					"application/vnd.android.package-archive");
			// Intent騾具ｽｺ髯ｦ�ｿｽ
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