package gaku.app.prinotoshi;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.Window;
import android.app.Activity;

public class StartActivity extends Activity {

	private SurfaceView mSvMain;
    private PrinSurface mMainDrawArea;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

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

    // SurfaceViewを参照
    mSvMain = (SurfaceView)findViewById(R.id.Surface);

    // 作成したMainSurfaceViewクラスをインスタンス化
    mMainDrawArea = new PrinSurface(this, mSvMain);

    }
}