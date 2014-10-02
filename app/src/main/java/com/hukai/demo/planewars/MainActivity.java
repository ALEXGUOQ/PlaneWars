package com.hukai.demo.planewars;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hukai.demo.planewars.data.ConstantData;
import com.hukai.demo.planewars.objs.EnemyPlane;
import com.hukai.demo.planewars.objs.PlaneFactory;
import com.hukai.demo.planewars.objs.SelfPlane;

import java.util.ArrayList;

/**
 * Version 1.0
 * Date: 2014-10-02
 * Author: hukai.me
 */
public class MainActivity extends Activity implements SurfaceHolder.Callback {

    public static int mScreenWidth;
    public static int mScreenHeight;
    public static int mSumScore;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MSG_DRAW = 1000;
    private static final int MSG_FINISH = 1001;

    private DrawHandler mDrawHandler;
    private HandlerThread mHandlerThread;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;
    private Paint mPaint = new Paint();

    private ArrayList<EnemyPlane> mEnemyPlaneList = new ArrayList<EnemyPlane>();
    private SelfPlane mSelfPlane;

    private int mSpeedRate;
    private float mPlayPauseBtnW;
    private float mPlayPauseBtnH;
    private boolean mIsPlaying;
    private boolean mIsTouchSelfPlane;
    private Bitmap mPlayBtnBmp;
    private Bitmap mPauseBtnBmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "[onCreate] + BEGIN");
        setContentView(R.layout.activity_main);
        mHandlerThread = new HandlerThread("DrawThread");
        mHandlerThread.start();
        mDrawHandler = new DrawHandler(mHandlerThread.getLooper());
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        Log.d(TAG, "[onCreate] + END");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "[surfaceCreated] + BEGIN");
        mScreenWidth = mSurfaceView.getWidth();
        mScreenHeight = mSurfaceView.getHeight();
        mSumScore = 0;
        mSpeedRate = ConstantData.BASE_SPEED_RATE;
        mSelfPlane = PlaneFactory.newSelfPlane(this);
        for (int i = 0; i < ConstantData.MAX_ENEMY_PLANE_COUNT; i++) {
            EnemyPlane enemyPlane = PlaneFactory.newEnemyPlane(this);
            mEnemyPlaneList.add(enemyPlane);
        }

        mPlayBtnBmp = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_media_play);
        mPauseBtnBmp = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_media_pause);
        mPlayPauseBtnW = mPlayBtnBmp.getWidth();
        mPlayPauseBtnH = mPlayBtnBmp.getHeight();

        Log.d(TAG, "[surfaceCreated] mScreenSize = " + mScreenWidth + " * " + mScreenHeight
                + ", mPlayPauseBtnSize = " + mPlayPauseBtnW + " * " + mPlayPauseBtnH);

        mSelfPlane.isAlive = true;

        mIsPlaying = true;
        mDrawHandler.sendEmptyMessage(MSG_DRAW);
        Log.d(TAG, "[surfaceCreated] + END");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "[surfaceChanged] + BEGIN");
        Log.d(TAG, "[surfaceChanged] + END");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "[surfaceDestroyed] + BEGIN");
        mHandlerThread.quit();
        Log.d(TAG, "[surfaceDestroyed] + END");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mIsTouchSelfPlane = false;
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            if (x > 20 && x < 20 + mPlayPauseBtnW && y > 20 && y < 20 + mPlayPauseBtnH) {
                if (mIsPlaying) {
                    mIsPlaying = false;
                } else {
                    mIsPlaying = true;
                    mDrawHandler.sendEmptyMessage(MSG_DRAW);
                }
                return true;
            } else if (x > mSelfPlane.x && x < mSelfPlane.x + mSelfPlane.w
                    && y > mSelfPlane.y && y < mSelfPlane.y + mSelfPlane.h) {
                if (mIsPlaying) {
                    mIsTouchSelfPlane = true;
                }
                return true;
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE && event.getPointerCount() == 1) {
            if (mIsTouchSelfPlane) {
                float x = event.getX();
                float y = event.getY();
                if (x > mSelfPlane.centerX + ConstantData.TOUCH_PLANE_RANGE) {
                    if (mSelfPlane.centerX + mSelfPlane.speed <= mScreenWidth) {
                        float moveX = mSelfPlane.centerX + mSelfPlane.speed;
                        mSelfPlane.move(moveX, mSelfPlane.centerY);
                    }
                } else if (x < mSelfPlane.centerX - ConstantData.TOUCH_PLANE_RANGE) {
                    if (mSelfPlane.centerX - mSelfPlane.speed >= 0) {
                        float moveX = mSelfPlane.centerX - mSelfPlane.speed;
                        mSelfPlane.move(moveX, mSelfPlane.centerY);
                    }
                }
                if (y > mSelfPlane.centerY + ConstantData.TOUCH_PLANE_RANGE) {
                    if (mSelfPlane.centerY + mSelfPlane.speed <= mScreenHeight) {
                        float moveY = mSelfPlane.centerY + mSelfPlane.speed;
                        mSelfPlane.move(mSelfPlane.centerX, moveY);
                    }
                } else if (y < mSelfPlane.centerY - ConstantData.TOUCH_PLANE_RANGE) {
                    if (mSelfPlane.centerY - mSelfPlane.speed >= 0) {
                        float moveY = mSelfPlane.centerY - mSelfPlane.speed;
                        mSelfPlane.move(mSelfPlane.centerX, moveY);
                    }
                }
                return true;
            }
        }
        return false;
    }

    private class DrawHandler extends Handler {

        int textColor = getResources().getColor(android.R.color.holo_blue_dark);

        DrawHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case MSG_DRAW:
                    Log.d(TAG, "[handleMessage] MSG_DRAW");
                    initDrawObjs();
                    invokeDrawObjs();
                    if (!mIsPlaying) {
                        this.removeMessages(MSG_DRAW);
                    } else {
                        int delayTime = 80;
                        Message newMsg = this.obtainMessage(MSG_DRAW);
                        this.sendMessageDelayed(newMsg, delayTime);
                    }
                    break;
                case MSG_FINISH:
                    Intent intent = new Intent(getApplication(), FinishActivity.class);
                    intent.putExtra("Score", mSumScore);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        }

        private void initDrawObjs() {
            mSelfPlane.init(mSpeedRate, mSelfPlane.centerX, mSelfPlane.centerY);

            for (EnemyPlane obj : mEnemyPlaneList) {
                if (!obj.isAlive) {
                    obj.init(mSpeedRate, 0, 0);
                    break;
                }
            }


            if (mSumScore >= mSpeedRate * ConstantData.UPGRADE_SPEED_SCORE
                    && mSpeedRate < ConstantData.MAX_SPEED_RATE) {
                mSpeedRate++;
            }
        }

        private void invokeDrawObjs() {
            try {
                mCanvas = mSurfaceHolder.lockCanvas();
                mCanvas.drawColor(Color.WHITE);

                for (EnemyPlane obj : mEnemyPlaneList) {
                    if (obj.isAlive) {
                        obj.draw(mCanvas);
                        if (obj.canCollide() && mSelfPlane.isAlive) {
                            if (obj.isCollide(mSelfPlane)) {
                                mSelfPlane.isAlive = false;
                            }
                        }
                    }
                }

                if (!mSelfPlane.isAlive) {
                    mIsPlaying = false;
                    mDrawHandler.sendEmptyMessage(MSG_FINISH);
                }

                mCanvas.save();
                if (mIsPlaying) {
                    mCanvas.drawBitmap(mPlayBtnBmp, 20, 20, mPaint);
                } else {
                    mCanvas.drawBitmap(mPauseBtnBmp, 20, 20, mPaint);
                }
                mCanvas.restore();

                mSelfPlane.draw(mCanvas);
                mSelfPlane.fire(mCanvas, mEnemyPlaneList);

                mPaint.setTextSize(36);
                mPaint.setColor(textColor);
                mCanvas.drawText("Score:" + mSumScore + ", Speed:" + mSpeedRate, 30 + mPlayPauseBtnW, 70, mPaint);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mCanvas != null) {
                    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                }
            }
        }
    }

}
