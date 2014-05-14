package gaku.app.prinotoshi;

import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PrinSurface extends SurfaceView implements SurfaceHolder.Callback {
	// このサンプルでは実行間隔を 0.016秒間隔（約 60 fps に相当）に設定してみた
	private static final long INTERVAL_PERIOD = 16;
	private ScheduledExecutorService scheduledExecutorService;
	private static final float FONT_SIZE = 24f;
	private Paint paintCircle, paintFps;
	private float x, y, r;
	private ArrayList<Long> intervalTime = new ArrayList<Long>(20);
	private float touchX = 0;
	private float touchY = 0;
	private int itemFlickFlag = 0;
	private int FlickFlag = 0;
	private float SetX = 0;
	private float SetY = 0;
	private int xFlickFlag = 0;
	private int yFlickFlag = 0;
	private float defaultY = 0;
	private int imageSize = 0;
	private int prinFlag = 0;
	private int beforePrin = 100;
	private int n;
	private int gameCount = 0;
	private int gameokFlag = 0;

	private Bitmap[] resource = new Bitmap [101];

	//画像読み込み
	Resources res = this.getContext().getResources();
	Bitmap prin = BitmapFactory.decodeResource(res, R.drawable.purin_2);
	Bitmap prin2 = BitmapFactory.decodeResource(res, R.drawable.purin_0);
	Bitmap cup = BitmapFactory.decodeResource(res, R.drawable.cup_1);
	Bitmap sara = BitmapFactory.decodeResource(res, R.drawable.sara);
	Bitmap desk = BitmapFactory.decodeResource(res, R.drawable.desk);

	public PrinSurface(Context context) {
		super(context);
		init();
	}

	public PrinSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PrinSurface(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/*
	 * コンストラクター引数が1〜3個の場合のすべてで共通となる初期化ルーチン
	 */
	private void init() {
		/*
		 * このクラス（SurfaceViewTest）では、SurfaceView の派生クラスを定義するだけでなく、
		 * SurfaceHolder.Callback インターフェイスのコールバックも実装（implement）しているが、
		 * SurfaceHolder であるこのクラスのインスタンスの呼び出し元のアクティビティ（通常はMainActivity）
		 * に対して、関連するコールバック（surfaceChanged, surfaceCreated, surfaceDestroyed）
		 * の呼び出し先がこのクラスのインスタンス（this）であることを呼出元アクティビティに登録する。
		 */
		SurfaceHolder surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);

		// fps 計測用の設定値の初期化
		for (int i = 0; i < 19; i++) {
			intervalTime.add(System.currentTimeMillis());
		}

		// 描画に関する各種設定
		paintCircle = new Paint();
		paintCircle.setStyle(Style.FILL);
		paintCircle.setColor(Color.WHITE);
		paintCircle.setAntiAlias(false);
		paintFps = new Paint();
		paintFps.setTypeface(Typeface.DEFAULT);
		paintFps.setTextSize(FONT_SIZE);
		paintFps.setColor(Color.BLACK);
		paintFps.setAntiAlias(true);
	}

	// コールバック内容の定義 (1/3)
	@Override
	public void surfaceCreated(final SurfaceHolder surfaceHolder) {
		x = 0;
		y = (float) (getHeight() / 3);
		//標準の上下位置
		defaultY = (float)(getHeight() /1.4);
		DrawSurface(surfaceHolder);
		// 100x100にリサイズ
		imageSize = getWidth()/3;
		//bitmap生成
		// Bitmap生成時のオプション。
	    BitmapFactory.Options options = new Options();
	    // 画像を1/20サイズに縮小（メモリ対策）
        options.inSampleSize = 2;
        // 現在の表示メトリクスの取得
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        // ビットマップのサイズを現在の表示メトリクスに合わせる（メモリ対策）
        options.inDensity = dm.densityDpi;;
	    // inPurgeableでBitmapを再利用するかどうかを明示的に決定
	    options.inPurgeable = true;
		desk= Bitmap.createScaledBitmap(desk, getWidth(), getHeight(), true);
		sara= Bitmap.createScaledBitmap(sara, imageSize, imageSize, false);
		prin= Bitmap.createScaledBitmap(prin, imageSize, imageSize, false);
		cup= Bitmap.createScaledBitmap(cup, imageSize, imageSize, false);

		//リソースセット
		resource[0] = BitmapFactory.decodeResource(res, R.drawable.purin_2,options);
		resource[1] = BitmapFactory.decodeResource(res, R.drawable.coffee_2,options);
		resource[2] = BitmapFactory.decodeResource(res, R.drawable.jerry_2,options);
		resource[3] = BitmapFactory.decodeResource(res, R.drawable.moti_2,options);
		resource[4] = BitmapFactory.decodeResource(res, R.drawable.mushi_2,options);
		resource[5] = BitmapFactory.decodeResource(res, R.drawable.yogurt_2,options);

		resource[6] = BitmapFactory.decodeResource(res, R.drawable.purin_0,options);
		resource[7] = BitmapFactory.decodeResource(res, R.drawable.coffee_0,options);
		resource[8] = BitmapFactory.decodeResource(res, R.drawable.jerry_0,options);
		resource[9] = BitmapFactory.decodeResource(res, R.drawable.moti_0,options);
		resource[10] = BitmapFactory.decodeResource(res, R.drawable.mushi_0,options);
		resource[11] = BitmapFactory.decodeResource(res, R.drawable.yogurt_0,options);

		resource[12] = BitmapFactory.decodeResource(res, R.drawable.purin_1,options);
		resource[13] = BitmapFactory.decodeResource(res, R.drawable.coffee_1,options);
		resource[14] = BitmapFactory.decodeResource(res, R.drawable.jerry_1,options);
		resource[15] = BitmapFactory.decodeResource(res, R.drawable.moti_1,options);
		resource[16] = BitmapFactory.decodeResource(res, R.drawable.mushi_1,options);
		resource[17] = BitmapFactory.decodeResource(res, R.drawable.yogurt_1,options);
	}
	// コールバック内容の定義 (2/3)
	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
	}

	// コールバック内容の定義 (3/3)
	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
		// SingleThreadScheduledExecutor を停止する
		scheduledExecutorService.shutdown();

		// 呼出元アクティビティ側のこのクラスのインスタンスに対するコールバック登録を解除する
		surfaceHolder.removeCallback(this);
	}

	public void DrawSurface(final SurfaceHolder surfaceHolder){
		// SingleThreadScheduledExecutor による単一 Thread のインターバル実行
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {

				// fps（実測値）の計測
				intervalTime.add(System.currentTimeMillis());
				float fps = 20000 / (intervalTime.get(19) - intervalTime.get(0));
				intervalTime.remove(0);

				// ロックした Canvas の取得
				Canvas canvas = surfaceHolder.lockCanvas();
				canvas.drawColor(Color.WHITE);
				//背景描画
				canvas.drawBitmap(desk, 0, 0, paintCircle);

				//横に移動
				if(x <= getWidth()/2 && FlickFlag < 10){
					x = x += getWidth()/25;
					if(x < getWidth()/24){
						Random r = new Random();
						n = r.nextInt(26);
						if(prinFlag != 1 && n!=beforePrin){
							Log.v("makeprin",String.valueOf(n));
							if(prin != null && n!=beforePrin && beforePrin <= 20){
								prin.recycle();
								prin = null;
							}
							if(n <= 20){
								prin = resource[0];
							}
							if(n==21){
								prin = resource[1];
							}
							if(n==22){
								prin = resource[2];
							}
							if(n==23){
								prin = resource[3];
							}
							if(n==24){
								prin = resource[4];
							}
							if(n==25){
								prin = resource[5];
							}
							prin= Bitmap.createScaledBitmap(prin, imageSize, imageSize, false);
							prinFlag = 1;
							beforePrin = n;
						}
					}
					Log.v("prin","描画");
					canvas.drawBitmap(sara, x-imageSize/2, defaultY, paintCircle);
					canvas.drawBitmap(cup, x-imageSize/2, y-imageSize/2, paintCircle);
					canvas.drawBitmap(prin, x-imageSize/2, y-imageSize/2, paintCircle);
				}
				//フリックしたらフラグを立てる
				if(itemFlickFlag == 1){
					FlickFlag ++;
				}

				//下に落として一定以上落ちたらスライド
				if(FlickFlag > 30){
					itemFlickFlag = 0;
					FlickFlag = 0;
					x=0;
					y=(float) (getHeight() / 3);
					xFlickFlag = 0;
					yFlickFlag = 0;
					if(gameokFlag == 1){
						gameCount++;
						gameokFlag = 0;
					}
				}
				//フリックされたら判定
				else if(FlickFlag > 10){
					//お皿はそのまま
					canvas.drawBitmap(sara, x-imageSize/2, defaultY, paintCircle);
					//カップはそのまま
					canvas.drawBitmap(cup, x-imageSize/2, (float) (getHeight() / 3)-imageSize/2, paintCircle);
					//横にフリックされたら折る
					if(xFlickFlag == 1){
							canvas.drawBitmap(prin2, x-imageSize/2, (float) (defaultY-(imageSize/2.3)), paintCircle);
					}
					//立てにフリックされたらそのまま
					else if(yFlickFlag == 1){
						canvas.drawBitmap(prin, x-imageSize/2, (float) (defaultY-(imageSize/2.3)), paintCircle);
					}
					prin2= Bitmap.createScaledBitmap(prin2, imageSize, imageSize, false);
					canvas.drawBitmap(prin2, x-imageSize/2, (float) (defaultY-(imageSize/2.3)), paintCircle);
					//横に移動させる
					x += getWidth()/20;
				}

				else if(itemFlickFlag == 1){
					canvas.drawBitmap(sara, x-imageSize/2, defaultY, paintCircle);
					if(xFlickFlag == 1){
						Log.v("prin",String.valueOf(n));
						if(prin2 != null){
							prin2.recycle();
							prin2 = null;
						}
						if(n <= 20){
							prin2 = resource[6];
							gameokFlag = 1;
						}
						if(n==21){
							prin2 = resource[13];
							SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
							Editor edit = prefs.edit();
							edit.putString("score",String.valueOf(gameCount));
							edit.commit();

							getContext().startActivity(new Intent(getContext(), Result.class));
							System.gc();
						}
						if(n==22){
							prin2 = resource[14];
							SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
							Editor edit = prefs.edit();
							edit.putString("score",String.valueOf(gameCount));
							edit.commit();

							getContext().startActivity(new Intent(getContext(), Result.class));
							System.gc();
						}
						if(n==23){
							prin2 = resource[15];
							SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
							Editor edit = prefs.edit();
							edit.putString("score",String.valueOf(gameCount));
							edit.commit();

							getContext().startActivity(new Intent(getContext(), Result.class));
							System.gc();
						}
						if(n==24){
							prin2 = resource[16];
							SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
							Editor edit = prefs.edit();
							edit.putString("score",String.valueOf(gameCount));
							edit.commit();

							getContext().startActivity(new Intent(getContext(), Result.class));
							System.gc();
						}
						if(n==25){
							prin2 = resource[17];
							SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
							Editor edit = prefs.edit();
							edit.putString("score",String.valueOf(gameCount));
							edit.commit();

							getContext().startActivity(new Intent(getContext(), Result.class));
							System.gc();
						}
						prin2= Bitmap.createScaledBitmap(prin2, imageSize, imageSize, false);
						canvas.drawBitmap(prin2, x-imageSize/2, (float) (defaultY-(imageSize/2.3)), paintCircle);
						canvas.drawBitmap(cup, x-imageSize/2, (getHeight() / 3)-imageSize/2, paintCircle);
						//canvas.drawBitmap(prin, x-imageSize/2, (float) (getHeight() / 4), paintCircle);
					}
					else if(yFlickFlag == 1){
						Log.v("prin",String.valueOf(n));
						if(prin2 != null){
							prin2.recycle();
							prin2 = null;
						}
						if(n <= 20){
							prin2 = resource[12];
							SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
							Editor edit = prefs.edit();
							edit.putString("score",String.valueOf(gameCount));
							edit.commit();

							getContext().startActivity(new Intent(getContext(), Result.class));
							System.gc();
						}
						if(n==21){
							prin2 = resource[7];
							gameokFlag++;
						}
						if(n==22){
							prin2 = resource[8];
							gameokFlag++;
						}
						if(n==23){
							prin2 = resource[9];
							gameokFlag++;
						}
						if(n==24){
							prin2 = resource[10];
							gameokFlag++;
						}
						if(n==25){
							prin2 = resource[11];
							gameokFlag++;
						}
						prin2= Bitmap.createScaledBitmap(prin2, imageSize, imageSize, false);
						canvas.drawBitmap(prin2, x-imageSize/2, (float) (defaultY-(imageSize/2.3)), paintCircle);
						canvas.drawBitmap(cup, x-imageSize/2, (getHeight() / 3)-imageSize/2, paintCircle);
						canvas.drawBitmap(cup, x-imageSize/2, (getHeight() / 3)-imageSize/2, paintCircle);
						canvas.drawBitmap(prin, x-imageSize/2, (float) (defaultY-(imageSize/2.3)), paintCircle);
					}

					SetX = x-imageSize/2;
					SetY = y;
				}
				else{
					//Log.v("prin","描画");
					canvas.drawBitmap(sara, x-imageSize/2, defaultY, paintCircle);
					canvas.drawBitmap(cup, x-imageSize/2, y-imageSize/2, paintCircle);
					canvas.drawBitmap(prin, x-imageSize/2, y-imageSize/2, paintCircle);
					canvas.drawText(String.valueOf(gameCount), 200, FONT_SIZE, paintFps);
				}
				canvas.drawText(String.format("%.1f fps", fps), 0, FONT_SIZE, paintFps);
				// ロックした Canvas の解放
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}, 100, INTERVAL_PERIOD, TimeUnit.MILLISECONDS);
	}

	public int TouchFlag = 0;
	// タッチイベントに対応する処理
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touchX = event.getX();
			touchY = event.getY();

			//x = event.getX();
			//y = event.getY();
			if(event.getX() > getWidth() / 3 && event.getX() < getWidth() / 1.5 && event.getY() > getHeight() / 6 && event.getY() < getHeight() / 2){
				//x = 0;
				//y = (float) (getHeight() / 3);
				Log.v("TOUCH","Flagon");
				TouchFlag = 1;
			}
			break;
		case MotionEvent.ACTION_UP:
			if(TouchFlag == 1){

				Log.v("YPos",String.valueOf(event.getY() - touchY));
				if(event.getY() - touchY > getHeight()/15){
					y += getHeight()/7;
					itemFlickFlag = 1;
					yFlickFlag = 1;
				}
				else if(event.getX() - touchX > getWidth()/14){
					y += getHeight()/7;
					itemFlickFlag = 1;
					xFlickFlag = 1;
				}
			}
			prinFlag = 0;
			TouchFlag = 0;
			break;
		}
		return true;
	}
}