package gaku.app.prinotoshi;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.app.Activity;
import android.content.SharedPreferences;

public class StartActivity extends Activity {

	private SurfaceView mSvMain;
    private PrinSurface mMainDrawArea;

    private ImageView item1,item2,item3;
    private ImageView[] ITEM;

    private SharedPreferences pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     	// タイトルバーを非表示
     	requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        setItemView();

        // SurfaceViewを参照
        mSvMain = (SurfaceView)findViewById(R.id.Surface);


        // 作成したMainSurfaceViewクラスをインスタンス化
        mMainDrawArea = new PrinSurface(this, mSvMain);
    }

    @Override
    protected void onResume() {
    super.onResume();
    // SurfaceView のインスタンスを実体化し、ContentView としてセットする
    //SurfaceViewTest surfaceView = new SurfaceViewTest(this);
    setContentView(R.layout.main);

    setItemView();

    // SurfaceViewを参照
    mSvMain = (SurfaceView)findViewById(R.id.Surface);

    // 作成したMainSurfaceViewクラスをインスタンス化
    mMainDrawArea = new PrinSurface(this, mSvMain);

    }

    public void setItemView(){
    	// ImageViewを参照
        item1 = (ImageView)findViewById(R.id.itemSet1);
        item2 = (ImageView)findViewById(R.id.itemSet2);
        item3 = (ImageView)findViewById(R.id.itemSet3);

        ITEM = new ImageView[]{item1,item2,item3};



        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        String[] SETS = pref.getString("item", "none,none,none").split(",");


        for(int i=0; i<3; i++){
        	Log.v("item",SETS[i]);
        	if(SETS[i].equals("muteki")){
        		ITEM[i].setImageResource(R.drawable.muteki5);
        	}
        	else if(SETS[i].equals("double1")){
        		ITEM[i].setImageResource(R.drawable.double1);
        	}
        	else if(SETS[i].equals("up10")){
        		ITEM[i].setImageResource(R.drawable.up10);
        	}
        	else if(SETS[i].equals("add5")){
        		ITEM[i].setImageResource(R.drawable.add5);
        	}
        	else if(SETS[i].equals("purin5")){
        		ITEM[i].setImageResource(R.drawable.purin5);
        	}
        	else if(SETS[i].equals("add1")){
        		ITEM[i].setImageResource(R.drawable.add1);
        	}
        	else if(SETS[i].equals("up20")){
        		ITEM[i].setImageResource(R.drawable.up20);
        	}
        	else if(SETS[i].equals("resurrection")){
        		ITEM[i].setImageResource(R.drawable.resurrection);
        	}
        }
    }
    public void item1click (View view){
    	PrinSurface.item1Go(view);
    	item1.setImageResource(R.drawable.none);
    }

    public void item2click (View view){
    	PrinSurface.item2Go(view);
    	item2.setImageResource(R.drawable.none);
    }

    public void item3click (View view){
    	PrinSurface.item3Go(view);
    	item3.setImageResource(R.drawable.none);
    }
}