package gaku.app.prinotoshi;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.app.Activity;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // SurfaceView のインスタンスを実体化し、ContentView としてセットする
        //SurfaceViewTest surfaceView = new SurfaceViewTest(this);
        PrinSurface surfaceView = new PrinSurface(this);
        setContentView(surfaceView);

        Log.v("create","create");
    }
    @Override
    protected void onResume() {
    super.onResume();
    // SurfaceView のインスタンスを実体化し、ContentView としてセットする
    //SurfaceViewTest surfaceView = new SurfaceViewTest(this);
    PrinSurface surfaceView = new PrinSurface(this);
    setContentView(surfaceView);

    }
}