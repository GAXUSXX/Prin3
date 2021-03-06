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
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RemoteViews;

public class PrinSurface extends SurfaceView implements SurfaceHolder.Callback ,  Runnable  {
	// このサンプルでは実行間隔を 0.010秒間隔（約 60 fps に相当）に設定してみた
	private static final long INTERVAL_PERIOD = 10;
	private ScheduledExecutorService scheduledExecutorService;
	private static final float FONT_SIZE = 64f;
	private static final float FONT_SIZE2 = 424f;
	private Paint paintCircle, paintFps, paintCount, CountDraw, TimeDraw, ItemCount, endText,paintTime;
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
	private int yFlickokFlag = 0;
	private int startFlag = 100;
	private long startTime = 0;
	private long curTime = 0;
	private int timeCount = 0;
	private int defaultTime = 10;
	private double Time = 2.0;
	private int width = 0;
	private int height = 0;
	private Context mContext;
	private static int mutekiCount = 0;
	private static  String[] SETS;
	private static int item1useFlag = 0;
	private static int item2useFlag = 0;
	private static int item3useFlag = 0;
	private static int DoubleCount = 0;
	private static int PrinCount = 0;
	private int RecoveryFlag = 0;
	private int RecoveryFlag2 = 0;
	private Bitmap scoreimg = null;
	private Bitmap timeimg = null;
	private Bitmap desk = null;
	private Bitmap[] resource = new Bitmap [101];
	private BitmapFactory.Options options;
	private int padding;
	//DEBUG変数
	private int DEBUG = 0;

	//画像読み込み
	Resources res = this.getContext().getResources();
	Bitmap prin = BitmapFactory.decodeResource(res, R.drawable.purin_2);
	Bitmap prin2 = BitmapFactory.decodeResource(res, R.drawable.purin_0);
	Bitmap cup = BitmapFactory.decodeResource(res, R.drawable.cup_1);
	Bitmap sara = BitmapFactory.decodeResource(res, R.drawable.sara);
	Bitmap noneitem = BitmapFactory.decodeResource(res, R.drawable.none);

	// コンストラクタ
	public PrinSurface(Context context, SurfaceView sv) {
		super(context);
		mContext = context;
		SurfaceHolder holder = sv.getHolder();
		holder.addCallback(this);
		sv.setOnTouchListener(new FlickTouchListener());

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
		//SurfaceHolder surfaceHolder = getHolder();
		//surfaceHolder.addCallback(this);

		// fps 計測用の設定値の初期化
		for (int i = 0; i < 19; i++) {
			intervalTime.add(System.currentTimeMillis());
		}

		//画像取得ブレーク
		if(DEBUG == 1){
			Log.v("SetPaint","Break");
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

		// 152dp に相当する px 値を取得
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        padding = (int) (metrics.density * 124);

		int FONT_SIZE2 = padding;

		paintCount = new Paint();
		paintCount.setStyle(Style.FILL);
		paintCount.setColor(Color.BLACK);
		paintCount.setTextSize(FONT_SIZE2);
		paintCount.setAntiAlias(false);

		CountDraw = new Paint();
		CountDraw.setStyle(Style.FILL);
		CountDraw.setColor(Color.BLACK);
		CountDraw.setTextSize(FONT_SIZE2/3);
		CountDraw.setAntiAlias(false);

		ItemCount = new Paint();
		ItemCount.setStyle(Style.FILL);
		ItemCount.setColor(Color.RED);
		ItemCount.setTextSize((float) (FONT_SIZE2/6));
		ItemCount.setAntiAlias(false);

		endText = new Paint();
		endText.setStyle(Style.FILL);
		endText.setColor(Color.RED);
		endText.setTextSize(FONT_SIZE2/2);
		endText.setAntiAlias(false);
		
		paintTime = new Paint();
		paintTime.setStyle(Style.FILL);
		paintTime.setColor(Color.RED);
		paintTime.setTextSize((float) (FONT_SIZE2/6));
		paintTime.setAntiAlias(false);
	}

	// コールバック内容の定義 (1/3)
	@Override
	public void surfaceCreated(final SurfaceHolder surfaceHolder) {
		Display disp =
				((WindowManager)mContext.getSystemService(mContext.WINDOW_SERVICE)).
				getDefaultDisplay();
		width = disp.getWidth();
		height = disp.getHeight();
		// 152dp に相当する px 値を取得
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        padding = (int) (metrics.density * 124);

		x = 0;
		y = padding;
		//標準の上下位置
		defaultY = padding * 2;

		//オプションセットブレーク
		if(DEBUG == 1){
			Log.v("SetOption","Break");
		}
		// 100x100にリサイズ
		imageSize = padding;
		//bitmap生成
		// Bitmap生成時のオプション。
		options = new Options();
		// 画像を1/20サイズに縮小（メモリ対策）
		//options.inSampleSize = (int) 1.5;
		// 現在の表示メトリクスの取得
		DisplayMetrics dm = this.getResources().getDisplayMetrics();
		// ビットマップのサイズを現在の表示メトリクスに合わせる（メモリ対策）
		options.inDensity = dm.densityDpi;;
		// inPurgeableでBitmapを再利用するかどうかを明示的に決定
		options.inPurgeable = true;
		options.inPreferredConfig = Config.RGB_565;

		//画像セットブレーク
		if(DEBUG == 1){
			Log.v("CreateImage","Break");
		}
		desk = BitmapFactory.decodeResource(res, R.drawable.desk,options);
		options.inPreferredConfig = Config.ARGB_4444;
		//desk= Bitmap.createScaledBitmap(desk, width, padding*2, true);
		sara= Bitmap.createScaledBitmap(sara, imageSize*2, (int) (imageSize/1.5), false);
		prin= Bitmap.createScaledBitmap(prin, imageSize, imageSize, false);
		cup= Bitmap.createScaledBitmap(cup, imageSize, imageSize, false);
		scoreimg = BitmapFactory.decodeResource(res, R.drawable.score);
		scoreimg = Bitmap.createScaledBitmap(scoreimg, (int) (imageSize*1.1), (int) (imageSize*0.8), false);

		timeimg = BitmapFactory.decodeResource(res, R.drawable.time);;
		timeimg = Bitmap.createScaledBitmap(timeimg, imageSize, imageSize, false);

		//リソースセット
		//resource[0] = BitmapFactory.decodeResource(res, R.drawable.purin_2,options);
		//resource[1] = BitmapFactory.decodeResource(res, R.drawable.coffee_2,options);
		//resource[2] = BitmapFactory.decodeResource(res, R.drawable.jerry_2,options);
		//resource[3] = BitmapFactory.decodeResource(res, R.drawable.moti_2,options);
		//resource[4] = BitmapFactory.decodeResource(res, R.drawable.mushi_2,options);
		//resource[5] = BitmapFactory.decodeResource(res, R.drawable.yogurt_2,options);

		//resource[6] = BitmapFactory.decodeResource(res, R.drawable.purin_0,options);
		//resource[7] = BitmapFactory.decodeResource(res, R.drawable.coffee_0,options);
		//resource[8] = BitmapFactory.decodeResource(res, R.drawable.jerry_0,options);
		//resource[9] = BitmapFactory.decodeResource(res, R.drawable.moti_0,options);
		//resource[10] = BitmapFactory.decodeResource(res, R.drawable.mushi_0,options);
		//resource[11] = BitmapFactory.decodeResource(res, R.drawable.yogurt_0,options);

		//resource[12] = BitmapFactory.decodeResource(res, R.drawable.purin_1,options);
		//resource[13] = BitmapFactory.decodeResource(res, R.drawable.coffee_1,options);
		//resource[14] = BitmapFactory.decodeResource(res, R.drawable.jerry_1,options);
		//resource[15] = BitmapFactory.decodeResource(res, R.drawable.moti_1,options);
		//resource[16] = BitmapFactory.decodeResource(res, R.drawable.mushi_1,options);
		//resource[17] = BitmapFactory.decodeResource(res, R.drawable.yogurt_1,options);

		/**セットされているアイテム取得**/
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());

		SETS = pref.getString("item", "none,none,none").split(",");
		item1useFlag = 0;
		item2useFlag = 0;
		item3useFlag = 0;

		//Surface描画
		DrawSurface(surfaceHolder);
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
		getContext().stopService(new Intent(getContext(), overrayservice.class));

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

				//初期素材描画ブレーク
				if(DEBUG == 1){
					Log.v("GoImage","Break");
				}
				// ロックした Canvas の取得
				Canvas canvas = surfaceHolder.lockCanvas();
				canvas.drawColor(Color.WHITE);
				//背景描画
				canvas.drawBitmap(desk, 0, padding*3, paintCircle);
				//スコア背景描画
				canvas.drawBitmap(scoreimg, (float) (padding*1.8), padding/13, paintCircle);

				//Time背景描画
				canvas.drawBitmap(timeimg, 0, padding/14, paintCircle);

				//スタートブレーク
				if(DEBUG == 1){
					Log.v("CountBreak","Break");
				}
				if(startFlag < 410){
					startFlag+=3;
					FlickFlag = 100;
					String countt = String.valueOf((int)startFlag/100);
					if((int)startFlag/100 == 1){
						countt="3";
					}
					else if((int)startFlag/100 == 2){
						countt="2";
					}
					else if((int)startFlag/100 == 3){
						countt="1";
					}
					else{
						countt="1";
					}
					canvas.drawText(countt, (float) (padding*1.2), padding * 2, paintCount);
				}
				else if(startFlag == 400){
					startFlag = 1000;
					FlickFlag = 0;
				}

				//アイテムカウントブレーク
				if(DEBUG == 1){
					Log.v("ItemCountBreak","Break");
				}
				if(mutekiCount > 0){
					canvas.drawText(String.valueOf((float)mutekiCount/100), (float) ((float) padding*1.35), (float) (padding * 0.9), ItemCount);
					mutekiCount--;
					if(PrinCount > 0){
						if(PrinCount < mutekiCount){
							PrinCount = 0;
						}
					}

				}

				if(PrinCount > 0){
					canvas.drawText(String.valueOf((float)PrinCount/100), (float) ((float) padding * 1.35), (float) (padding * 0.9), ItemCount);
					if(mutekiCount > 0){
						if(PrinCount > mutekiCount){
							mutekiCount = 0;
						}
					}
					PrinCount--;
				}

				if(x > 200){
					double sumTime = timeCount/10;
					if(SETS[0].equals("add5") || SETS[1].equals("add5") || SETS[2].equals("add5") && SETS[0].equals("add1") || SETS[1].equals("add1") || SETS[2].equals("add1")){
						if(sumTime == 1){
							Time = 3.5;
						}
						if(sumTime == 2){
							Time = 3.4;
						}
						if(sumTime == 3){
							Time = 3.3;
						}
						if(sumTime == 4){
							Time = 3.2;
						}
						if(sumTime == 5){
							Time = 3.1;
						}
						if(sumTime == 6){
							Time = 3.0;
						}

						if(sumTime == 7){
							Time = 2.9;
						}

						if(sumTime == 8){
							Time = 2.8;
						}
						if(sumTime == 9){
							Time = 2.7;
						}
						if(sumTime == 10){
							Time = 2.6;
						}

						if(sumTime == 11){
							Time = 2.5;
						}

						if(sumTime == 12){
							Time = 2.4;
						}

						if(sumTime == 13){
							Time = 2.3;
						}

						if(sumTime == 14){
							Time = 2.2;
						}

						if(sumTime == 15){
							Time = 2.1;
						}

						if(sumTime == 16){
							Time = 2.0;
						}

						if(sumTime == 17){
							Time = 1.9;
						}

						if(sumTime == 18){
							Time = 1.8;
						}

						if(sumTime == 19){
							Time = 1.7;
						}

						if(sumTime == 20){
							Time = 1.6;
						}
						if(sumTime == 21){
							Time = 1.5;
						}
						if(sumTime == 22){
							Time = 1.4;
						}

						if(sumTime == 23){
							Time = 1.3;
						}

						if(sumTime == 24){
							Time = 1.2;
						}
						if(sumTime == 25){
							Time = 1.1;
						}
						if(sumTime == 26){
							Time = 1.0;
						}

						if(sumTime == 27){
							Time = 0.9;
						}

						if(sumTime == 28){
							Time = 0.8;
						}

						if(sumTime == 29){
							Time = 0.7;
						}

						if(sumTime == 30){
							Time = 0.6;
						}
						if(sumTime == 31){
							Time = 0.5;
						}
						if(sumTime == 32){
							Time = 0.4;
						}

						if(sumTime == 33){
							Time = 0.3;
						}

						if(sumTime == 34){
							Time = 0.2;
						}
						if(sumTime == 35){
							Time = 0.1;
						}
						if(sumTime == 36){
							Time = 0.0;
						}
					}
					else if(SETS[0].equals("add5") || SETS[1].equals("add5") || SETS[2].equals("add5")){
						if(sumTime == 1){
							Time = 2.5;
						}
						if(sumTime == 2){
							Time = 2.4;
						}
						if(sumTime == 3){
							Time = 2.3;
						}
						if(sumTime == 4){
							Time = 2.2;
						}
						if(sumTime == 5){
							Time = 2.1;
						}

						if(sumTime == 6){
							Time = 2.0;
						}

						if(sumTime == 7){
							Time = 1.9;
						}

						if(sumTime == 8){
							Time = 1.8;
						}
						if(sumTime == 9){
							Time = 1.7;
						}
						if(sumTime == 10){
							Time = 1.6;
						}

						if(sumTime == 11){
							Time = 1.5;
						}

						if(sumTime == 12){
							Time = 1.4;
						}

						if(sumTime == 13){
							Time = 1.3;
						}

						if(sumTime == 14){
							Time = 1.2;
						}

						if(sumTime == 15){
							Time = 1.1;
						}

						if(sumTime == 16){
							Time = 1.0;
						}

						if(sumTime == 17){
							Time = 0.9;
						}

						if(sumTime == 18){
							Time = 0.8;
						}

						if(sumTime == 19){
							Time = 0.7;
						}

						if(sumTime == 20){
							Time = 0.6;
						}
						if(sumTime == 21){
							Time = 0.5;
						}
						if(sumTime == 22){
							Time = 0.4;
						}

						if(sumTime == 23){
							Time = 0.3;
						}

						if(sumTime == 24){
							Time = 0.2;
						}
						if(sumTime == 25){
							Time = 0.1;
						}
						if(sumTime == 26){
							Time = 0.0;
						}
					}
					else if(SETS[0].equals("add1") || SETS[1].equals("add1") || SETS[2].equals("add1")){
						if(sumTime == 1){
							Time = 3.0;
						}
						if(sumTime == 2){
							Time = 2.9;
						}
						if(sumTime == 3){
							Time = 2.8;
						}
						if(sumTime == 4){
							Time = 2.7;
						}
						if(sumTime == 5){
							Time = 2.6;
						}

						if(sumTime == 6){
							Time = 2.5;
						}

						if(sumTime == 7){
							Time = 2.4;
						}

						if(sumTime == 8){
							Time = 2.3;
						}
						if(sumTime == 9){
							Time = 2.2;
						}
						if(sumTime == 10){
							Time = 2.1;
						}

						if(sumTime == 11){
							Time = 2.0;
						}

						if(sumTime == 12){
							Time = 1.9;
						}

						if(sumTime == 13){
							Time = 1.8;
						}

						if(sumTime == 14){
							Time = 1.7;
						}

						if(sumTime == 15){
							Time = 1.6;
						}

						if(sumTime == 16){
							Time = 1.5;
						}

						if(sumTime == 17){
							Time = 1.4;
						}

						if(sumTime == 18){
							Time = 1.3;
						}

						if(sumTime == 19){
							Time = 1.2;
						}

						if(sumTime == 20){
							Time = 1.1;
						}
						if(sumTime == 21){
							Time = 1.0;
						}
						if(sumTime == 22){
							Time = 0.9;
						}

						if(sumTime == 23){
							Time = 0.8;
						}

						if(sumTime == 24){
							Time = 0.7;
						}
						if(sumTime == 25){
							Time = 0.6;
						}
						if(sumTime == 26){
							Time = 0.5;
						}
						if(sumTime == 27){
							Time = 0.4;
						}

						if(sumTime == 28){
							Time = 0.3;
						}

						if(sumTime == 29){
							Time = 0.2;
						}
						if(sumTime == 30){
							Time = 0.1;
						}
						if(sumTime == 31){
							Time = 0.0;
						}
					}
					else{
						if(sumTime == 1){
							Time = 2.0;
						}
						if(sumTime == 2){
							Time = 1.9;
						}
						if(sumTime == 3){
							Time = 1.8;
						}
						if(sumTime == 4){
							Time = 1.7;
						}
						if(sumTime == 5){
							Time = 1.6;
						}

						if(sumTime == 6){
							Time = 1.5;
						}

						if(sumTime == 7){
							Time = 1.4;
						}

						if(sumTime == 8){
							Time = 1.3;
						}
						if(sumTime == 9){
							Time = 1.2;
						}
						if(sumTime == 10){
							Time = 1.1;
						}

						if(sumTime == 11){
							Time = 1.0;
						}

						if(sumTime == 12){
							Time = 0.9;
						}

						if(sumTime == 13){
							Time = 0.8;
						}

						if(sumTime == 14){
							Time = 0.7;
						}

						if(sumTime == 15){
							Time = 0.6;
						}

						if(sumTime == 16){
							Time = 0.5;
						}

						if(sumTime == 17){
							Time = 0.4;
						}

						if(sumTime == 18){
							Time = 0.3;
						}

						if(sumTime == 19){
							Time = 0.2;
						}

						if(sumTime == 20){
							Time = 0.1;
						}
						if(sumTime == 21){
							Time = 0.0;
						}
					}
					canvas.drawText(String.valueOf(Time), (float) (padding * 0.28), (float) (padding * 0.68), CountDraw);

					timeCount++;
				}

				//時間切れブレーク
				if(DEBUG == 1){
					Log.v("CountOut","Break");
				}
				if(Time == 0.0 && x > 200){
					if(RecoveryFlag == 0 && SETS[0].equals("resurrection") || RecoveryFlag == 0 && SETS[1].equals("resurrection") || RecoveryFlag == 0 && SETS[2].equals("resurrection")){
						Log.v("resurrection","res1");
						startFlag= 0;
						RecoveryFlag = 1;
						timeCount = 1;
						Time = 2.0;
						//getContext().startService(new Intent(getContext(), overrayservice.class));
					}
					else{
						canvas.drawText("失敗", (float) ((float) padding*1), (float) ((float) padding*2.4), endText);
						endGame();
						SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
						Editor edit = prefs.edit();
						edit.putString("score",String.valueOf(gameCount));
						edit.commit();

						getContext().startActivity(new Intent(getContext(), Result.class));
						//System.gc();
					}
				}

				//横に移動ブレーク
				if(DEBUG == 1){
					Log.v("SlideGo","Break");
				}
				//横に移動
				if(x <= width/2 && FlickFlag < 10){
					if(x < width / 6){
						Random r = new Random();
						n = r.nextInt(100);
						if(PrinCount > 0){
							n= 1000;
						}
						if(n ==1000 || RecoveryFlag2 == 1 || RecoveryFlag2 == 2 || prinFlag != 1 && n!=beforePrin){
							Log.v("makeprin",String.valueOf(n));
							RecoveryFlag2 = 2;
							if(prin != null && n!=beforePrin && beforePrin <= 50){
								prin.recycle();
								prin = null;
							}
							int upFlag = 0;
							if(SETS[0].equals("up10") || SETS[1].equals("up10") || SETS[2].equals("up10")){
								upFlag +=1;
							}
							if(SETS[0].equals("up20") || SETS[1].equals("up20") || SETS[2].equals("up20")){
								upFlag +=2;
							}
							if(DoubleCount > 0){
								upFlag = 5;
							}
							if(upFlag ==1){
								Log.v("現在","50%");
								if(n <= 50){
									resource[0] = BitmapFactory.decodeResource(res, R.drawable.purin_2,options);
									prin = resource[0];
									n= 100;
								}
								else{
									n = r.nextInt(3);
									if(n==0){
										resource[1] = BitmapFactory.decodeResource(res, R.drawable.mushi_2,options);
										prin = resource[1];
									}
									if(n==1){
										resource[2] = BitmapFactory.decodeResource(res, R.drawable.jerry_2,options);
										prin = resource[2];
									}
									if(n==2){
										resource[3] = BitmapFactory.decodeResource(res, R.drawable.moti_2,options);
										prin = resource[3];
									}
								}
							}
							else if(upFlag ==2){
								Log.v("現在","60%");
								if(n <= 60){
									resource[0] = BitmapFactory.decodeResource(res, R.drawable.purin_2,options);
									prin = resource[0];
									n= 100;
								}
								else{
									n = r.nextInt(3);
									if(n==0){
										resource[1] = BitmapFactory.decodeResource(res, R.drawable.mushi_2,options);;
										prin = resource[1];
									}
									if(n==1){
										resource[2] = BitmapFactory.decodeResource(res, R.drawable.jerry_2,options);
										prin = resource[2];
									}
									if(n==2){
										resource[3] = BitmapFactory.decodeResource(res, R.drawable.moti_2,options);
										prin = resource[3];
									}
								}
							}
							else if(upFlag ==3){
								Log.v("現在","70%");
								if(n <= 70){
									resource[0] = BitmapFactory.decodeResource(res, R.drawable.purin_2,options);
									prin = resource[0];
									n= 100;
								}
								else{
									n = r.nextInt(3);
									if(n==0){
										resource[1] = BitmapFactory.decodeResource(res, R.drawable.mushi_2,options);
										prin = resource[1];
									}
									if(n==1){
										resource[2] = BitmapFactory.decodeResource(res, R.drawable.jerry_2,options);
										prin = resource[2];
									}
									if(n==2){
										resource[3] = BitmapFactory.decodeResource(res, R.drawable.moti_2,options);
										prin = resource[3];
									}
								}
							}
							else if(upFlag == 5){
								Log.v("現在","80%");
								if(n <= 80){
									resource[0] = BitmapFactory.decodeResource(res, R.drawable.purin_2,options);
									prin = resource[0];
									n= 100;
								}
								else{
									n = r.nextInt(3);
									if(n==0){
										resource[1] = BitmapFactory.decodeResource(res, R.drawable.mushi_2,options);
										prin = resource[1];
									}
									if(n==1){
										resource[2] = BitmapFactory.decodeResource(res, R.drawable.jerry_2,options);
										prin = resource[2];
									}
									if(n==2){
										resource[3] = BitmapFactory.decodeResource(res, R.drawable.moti_2,options);
										prin = resource[3];
									}
								}
							}
							else if(n == 1000){
								resource[0] = BitmapFactory.decodeResource(res, R.drawable.purin_2,options);
								prin = resource[0];
								n= 100;
							}
							else{
								Log.v("現在","40%");
								if(n <= 40){
									resource[0] = BitmapFactory.decodeResource(res, R.drawable.purin_2,options);
									prin = resource[0];
									n = 100;
								}
								else{
									n = r.nextInt(3);
									if(n==0){
										resource[1] = BitmapFactory.decodeResource(res, R.drawable.mushi_2,options);
										prin = resource[1];
									}
									if(n==1){
										resource[2] = BitmapFactory.decodeResource(res, R.drawable.jerry_2,options);
										prin = resource[2];
									}
									if(n==2){
										resource[3] = BitmapFactory.decodeResource(res, R.drawable.moti_2,options);
										prin = resource[3];
									}
								}
							}


							prin= Bitmap.createScaledBitmap(prin, imageSize, imageSize, false);
							prinFlag = 1;
							beforePrin = n;
						}
					}
					if(x<width/2){
						x +=  width/4;
					}
					if(x>width/2){
						x=width/2;
					}
					Log.v("prin","描画");
					canvas.drawBitmap(sara, (float) (x-imageSize*1.07), (float)(defaultY+imageSize*0.9), paintCircle);
					canvas.drawBitmap(cup, (float) (x-imageSize/1.8), (float) (y-imageSize/1.5), paintCircle);
					canvas.drawBitmap(prin, (float) (x-imageSize/1.8), (float) (y-imageSize/1.5), paintCircle);
				}
				//フリックしたらフラグを立てる
				if(itemFlickFlag == 1){
					FlickFlag ++;
				}

				//初期化ブレーク
				if(DEBUG == 1){
					Log.v("EndOut","Break");
				}
				//下に落として一定以上落ちたらスライド
				if(FlickFlag > 15){
					itemFlickFlag = 0;
					FlickFlag = 0;
					x = 0;
					y = (float) (padding * 1.6);
					xFlickFlag = 0;
					yFlickFlag = 0;
					yFlickokFlag = 0;
					SetY = (float) (padding * 1.6);
					if(gameokFlag == 1){
						gameCount++;
						// 個数が100個以下なら10個ごとに0.1秒減らす
						if(gameCount % 10 == 0){
							defaultTime+=10;
						}
					
						gameokFlag = 0;
					}
					timeCount = defaultTime;
					startTime = System.currentTimeMillis();
				}
				//フリックされたら判定
				else if(FlickFlag > 5){
					//フリックで横移動ブレーク
					if(DEBUG == 1){
						Log.v("FlickSlide","Break");
					}
					if(RecoveryFlag2 == 2){
						RecoveryFlag2 = 3;
					}
					//お皿はそのまま
					canvas.drawBitmap(sara, (float) (x-imageSize*1.07), (float)(defaultY+imageSize*0.9), paintCircle);

					//横にフリックされたら折る
					if(xFlickFlag == 1 || mutekiCount > 0){
						canvas.drawBitmap(prin2, (float) (x-imageSize/1.8), SetY, paintCircle);
						//カップはそのまま
						canvas.drawBitmap(cup, (float) (x-imageSize/1.8), (float) (height / 3)-imageSize/2, paintCircle);
					}
					//立てにフリックされたらそのまま
					else if(yFlickFlag == 1){
						canvas.drawBitmap(prin, (float) (x-imageSize/1.8), SetY, paintCircle);
						canvas.drawBitmap(cup, (float) (x-imageSize/1.8),SetY, paintCircle);
					}
					prin2= Bitmap.createScaledBitmap(prin2, imageSize, imageSize, false);
					canvas.drawBitmap(prin2, (float) (x-imageSize/1.8), SetY, paintCircle);
					//横に移動させる

					x += width/4;
				}

				//落下時の処理(8回分落下)
				else if(itemFlickFlag == 1){
					//フリックで横移動ブレーク
					if(DEBUG == 1){
						Log.v("StartFlickSlide","Break");
					}
					SetX = x-imageSize/2;
					if(SetY < defaultY*1.02){
						SetY += defaultY / 5;
					}
					canvas.drawBitmap(sara, (float) (x-imageSize*1.07), (float)(defaultY+imageSize*0.9), paintCircle);
					if(xFlickFlag == 1){
						Log.v("prin",String.valueOf(n));
						if(prin2 != null){
							prin2.recycle();
							prin2 = null;
						}
						if(n == 100){
							resource[6] = BitmapFactory.decodeResource(res, R.drawable.purin_0,options);
							prin2 = resource[6];
							gameokFlag = 1;
						}

						if(n==0){
							if(mutekiCount > 0){
								resource[7] = BitmapFactory.decodeResource(res, R.drawable.mushi_0,options);
								prin2 = resource[7];
							}
							else if(RecoveryFlag == 0 && SETS[0].equals("resurrection") || RecoveryFlag == 0 && SETS[1].equals("resurrection") || RecoveryFlag == 0 && SETS[2].equals("resurrection")){
								Log.v("RecoveryFlag ",String.valueOf(RecoveryFlag));
								Log.v("resurrection","res2");
								startFlag= 0;
								RecoveryFlag = 1;
								RecoveryFlag2 = 1;
								resource[7] = BitmapFactory.decodeResource(res, R.drawable.mushi_0,options);
								prin2 = resource[7];
								n = 2000;
								//getContext().startService(new Intent(getContext(), overrayservice.class));
							}
							else{
								canvas.drawText("失敗", (float) ((float) padding*1), (float) ((float) padding*1.4), endText);
								resource[13] = BitmapFactory.decodeResource(res, R.drawable.mushi_1,options);
								prin2 = resource[13];
								SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
								Editor edit = prefs.edit();
								edit.putString("score",String.valueOf(gameCount));
								edit.commit();

								endGame();
								System.gc();
								getContext().startActivity(new Intent(getContext(), Result.class));
							}
						}
						if(n==1){
							if(mutekiCount > 0){
								resource[8] = BitmapFactory.decodeResource(res, R.drawable.jerry_0,options);
								prin2 = resource[8];
							}
							else if(RecoveryFlag == 0 && SETS[0].equals("resurrection") || RecoveryFlag == 0 && SETS[1].equals("resurrection") || RecoveryFlag == 0 && SETS[2].equals("resurrection")){
								Log.v("RecoveryFlag ",String.valueOf(RecoveryFlag));
								Log.v("resurrection","res3");
								startFlag= 0;
								RecoveryFlag = 1;
								RecoveryFlag2 = 1;
								resource[8] = BitmapFactory.decodeResource(res, R.drawable.jerry_0,options);
								prin2 = resource[8];
								n = 2000;
								//getContext().startService(new Intent(getContext(), overrayservice.class));
							}
							else{
								canvas.drawText("失敗", (float) ((float) padding*1), (float) ((float) padding*1.4), endText);
								resource[14] = BitmapFactory.decodeResource(res, R.drawable.jerry_1,options);
								prin2 = resource[14];
								SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
								Editor edit = prefs.edit();
								edit.putString("score",String.valueOf(gameCount));
								edit.commit();

								endGame();
								System.gc();
								getContext().startActivity(new Intent(getContext(), Result.class));
							}
						}
						if(n==2){
							if(mutekiCount > 0){
								resource[9] = BitmapFactory.decodeResource(res, R.drawable.moti_0,options);
								prin2 = resource[9];
							}
							else if(RecoveryFlag == 0 && SETS[0].equals("resurrection") || RecoveryFlag == 0 && SETS[1].equals("resurrection") || RecoveryFlag == 0 && SETS[2].equals("resurrection")){
								Log.v("RecoveryFlag ",String.valueOf(RecoveryFlag));
								Log.v("resurrection","res4");
								startFlag= 0;
								RecoveryFlag = 1;
								RecoveryFlag2 = 1;
								resource[9] = BitmapFactory.decodeResource(res, R.drawable.moti_0,options);
								prin2 = resource[9];
								n = 2000;
								//getContext().startService(new Intent(getContext(), overrayservice.class));
							}
							else{
								canvas.drawText("失敗", (float) ((float) padding*1), (float) ((float) padding*1.4), endText);
								resource[15] = BitmapFactory.decodeResource(res, R.drawable.moti_1,options);
								prin2 = resource[15];
								SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
								Editor edit = prefs.edit();
								edit.putString("score",String.valueOf(gameCount));
								edit.commit();

								endGame();
								System.gc();
								getContext().startActivity(new Intent(getContext(), Result.class));
							}
						}
						prin2= Bitmap.createScaledBitmap(prin2, imageSize, imageSize, false);
						canvas.drawBitmap(prin2, (float) (x-imageSize/1.8), SetY, paintCircle);
						canvas.drawBitmap(cup, (float) (x-imageSize/1.8), padding-padding/15, paintCircle);
						//canvas.drawBitmap(prin, x-imageSize/2, (float) (getHeight() / 4), paintCircle);
					}
					else if(yFlickFlag == 1){
						//ｔａｔｅ　フリックで横移動ブレーク
						if(DEBUG == 1){
							Log.v("TateFlickSlide","Break");
						}
						Log.v("prin",String.valueOf(n));
						if(prin2 != null){
							prin2.recycle();
							prin2 = null;
						}
						if(n == 100){
							if(mutekiCount > 0){
								prin2 = resource[6];
							}
							else if(RecoveryFlag == 0 && SETS[0].equals("resurrection") || RecoveryFlag == 0 && SETS[1].equals("resurrection") || RecoveryFlag == 0 && SETS[2].equals("resurrection")){
								Log.v("resurrection","res5");
								startFlag= 0;
								RecoveryFlag = 1;
								RecoveryFlag2 = 1;
								prin2 = resource[6];
								n = 2000;
								//getContext().startService(new Intent(getContext(), overrayservice.class));
							}
							else{
								canvas.drawText("失敗", (float) ((float) padding*1), (float) ((float) padding*2.4), endText);
								resource[12] = BitmapFactory.decodeResource(res, R.drawable.purin_1,options);
								prin2 = resource[12];
								SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
								Editor edit = prefs.edit();
								edit.putString("score",String.valueOf(gameCount));
								edit.commit();

								endGame();
								System.gc();
								getContext().startActivity(new Intent(getContext(), Result.class));
							}
						}
						if(n==0){
							resource[7] = BitmapFactory.decodeResource(res, R.drawable.mushi_0,options);
							prin2 = resource[7];
							gameokFlag = 1;
						}
						if(n==1){
							resource[8] = BitmapFactory.decodeResource(res, R.drawable.jerry_0,options);
							prin2 = resource[8];
							gameokFlag = 1;
						}
						if(n==2){
							resource[9] = BitmapFactory.decodeResource(res, R.drawable.moti_0,options);
							prin2 = resource[9];
							gameokFlag = 1;
						}
						prin2= Bitmap.createScaledBitmap(prin2, imageSize, imageSize, false);
						canvas.drawBitmap(prin2, (float) (x-imageSize/1.8), SetY, paintCircle);
						canvas.drawBitmap(cup, (float) (x-imageSize/1.8), SetY, paintCircle);
						canvas.drawBitmap(prin, (float) (x-imageSize/1.8), SetY, paintCircle);
						yFlickokFlag = 1;
					}
				}
				else{
					//Log.v("prin","描画");
					canvas.drawBitmap(sara, (float) (x-imageSize*1.07), (float)(defaultY+imageSize*0.9), paintCircle);
					canvas.drawBitmap(prin, (float) (x-imageSize/1.8), (float) (y-imageSize/1.5), paintCircle);
					canvas.drawBitmap(cup, (float) (x-imageSize/1.8), (float) (y-imageSize/1.5), paintCircle);
				}
				canvas.drawText(String.valueOf(gameCount) + "個", (float) ((float) padding * 1.9), (float) ((float) padding * 0.65), CountDraw);
				canvas.drawText(String.format("%.1f fps", fps), 0, FONT_SIZE, paintFps);
				/**無敵状態表示**/
				if(mutekiCount > 0){
					//canvas.drawText("muteki", 300, FONT_SIZE, paintFps);
				}
				else{
					//canvas.drawText("notmuteki", 300, FONT_SIZE, paintFps);
				}
				/**2倍状態表示**/
				if(DoubleCount > 0){
					//canvas.drawText("double", 700, 300, paintFps);
				}
				else{
					//canvas.drawText("notdouble", 700, 300, paintFps);
				}
				/**プリン確変状態表示**/
				if(PrinCount > 0){
					//canvas.drawText("Prin", 700, 150, paintFps);
				}
				else{
					//canvas.drawText("notPrin", 700, 150, paintFps);
				}
				
				// -0.1秒表示
				if(gameCount % 10 == 0 && gameCount != 0){
					String showText = "-0.1秒";
					int left =  (int)((width - paintTime.measureText(showText))/2.1);
					canvas.drawText(showText, left, height/6, paintTime);
				}

				// ロックした Canvas の解放

				/**item1使用フラグ**/
				if(item1useFlag==1){
					//canvas.drawBitmap(noneitem, width/12, (float) (height/1.13), paintCircle);
					RemoteViews remoteViews = new RemoteViews("gaku.app.prinotoshi", R.layout.main);
					remoteViews.setImageViewResource(R.id.item1, R.drawable.none);
				}
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}, 100, INTERVAL_PERIOD, TimeUnit.MILLISECONDS);
	}

	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ

	}

	private class FlickTouchListener implements View.OnTouchListener {
		public int TouchFlag = 0;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO 自動生成されたメソッド・スタブ
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touchX = event.getX();
				touchY = event.getY();
				Log.v("touchX",String.valueOf(touchX));
				Log.v("touchY",String.valueOf(touchY));

				//x = event.getX();
				//y = event.getY();
				if(event.getX() > width / 3 && event.getX() < width / 1.5 && event.getY() > height / 6 && event.getY() < height / 2){
					//x = 0;
					//y = (float) (getHeight() / 3);
					Log.v("TOUCH","Flagon");
					TouchFlag = 1;
				}
				break;
			case MotionEvent.ACTION_UP:
				if(TouchFlag == 1){

					Log.v("YPos",String.valueOf(event.getY() - touchY));
					if(event.getY() - touchY > height/15){
						y += height/7;
						itemFlickFlag = 1;
						yFlickFlag = 1;
					}
					else if(event.getX() - touchX > width/14){
						y += height/7;
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

	public static void item1Go(View view){
		if(SETS[0].equals("muteki") && item1useFlag != 1){
			mutekiCount = 500;
			Log.v("muteki","ok");
		}
		if(SETS[0].equals("double1") && item1useFlag != 1){
			DoubleCount = 2000;
			Log.v("double","ok");
		}
		if(SETS[0].equals("purin5") && item1useFlag != 1){
			PrinCount = 500;
			Log.v("prin5","ok");
		}
		item1useFlag = 1;
	}
	public static void item2Go(View view){
		if(SETS[1].equals("muteki") && item2useFlag != 1){
			mutekiCount = 500;
			Log.v("muteki","ok");
		}
		if(SETS[1].equals("double1") && item2useFlag != 1){
			DoubleCount = 2000;
			Log.v("double","ok");
		}
		if(SETS[1].equals("purin5") && item2useFlag != 1){
			PrinCount = 500;
			Log.v("prin5","ok");
		}
		item2useFlag = 1;
	}
	public static void item3Go(View view){
		if(SETS[2].equals("muteki") && item3useFlag != 1){
			mutekiCount = 500;
			Log.v("muteki","ok");
		}
		if(SETS[2].equals("double1") && item3useFlag != 1){
			DoubleCount = 2000;
			Log.v("double","ok");
		}
		if(SETS[2].equals("purin5") && item3useFlag != 1){
			PrinCount = 500;
			Log.v("prin5","ok");
		}
		item3useFlag = 1;
	}

	public void endGame(){
		DoubleCount = 0;
		PrinCount = 0;
		mutekiCount = 0;
	}
}