package com.hukai.demo.planewars.objs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.hukai.demo.planewars.ActionFire;
import com.hukai.demo.planewars.ActionMove;
import com.hukai.demo.planewars.MainActivity;
import com.hukai.demo.planewars.R;
import com.hukai.demo.planewars.data.ConstantData;

import java.util.List;

/**
 * Version 1.0
 * Date: 2014-10-02
 * Author: hukai.me
 */
public class SelfPlane extends BaseObj implements ActionFire, ActionMove {

    private static final String TAG = SelfPlane.class.getSimpleName();
    private SelfBullet mSelfBullet;
    private Bitmap mPlaneBmp;

    public SelfPlane(Context context) {
        speed = ConstantData.BASE_PLANE_SPEED;
        mPlaneBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.plane);
        w = mPlaneBmp.getWidth();
        h = mPlaneBmp.getHeight();
        x = (MainActivity.mScreenWidth - w) / 2;
        y = MainActivity.mScreenHeight - h;
        centerX = x + w / 2;
        centerY = y + h / 2;
        mSelfBullet = BulletFactory.newSelfBullet(context);
    }

    @Override
    public void init(int speedRate, float centerX, float centerY) {
        speed = ConstantData.BASE_PLANE_SPEED  * (speedRate + 1);
        if (!mSelfBullet.isAlive) {
            mSelfBullet.init(ConstantData.BASE_SPEED_RATE, centerX, centerY);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (isAlive) {
            canvas.save();
            canvas.drawBitmap(mPlaneBmp, x, y, paint);
            canvas.restore();
        }
    }

    @Override
    public void move(float moveX, float moveY) {
        Log.d(TAG, "[move] moveX = " + moveX + ", moveY = " + moveY);
        setCenterX(moveX);
        setCenterY(moveY);
    }

    @Override
    public void fire(Canvas canvas, List<EnemyPlane> enemyPlanes) {
        if (!mSelfBullet.isAlive) {
            return;
        }
        for (EnemyPlane enemyPlane : enemyPlanes) {
            if (enemyPlane.canCollide() && mSelfBullet.checkCollide(enemyPlane)) {
                enemyPlane.beAttacked(mSelfBullet.mPower);
                if (enemyPlane.isExplosion) {
                    MainActivity.mSumScore += enemyPlane.initBlood;
                }
                break;
            }
        }
        mSelfBullet.draw(canvas);
    }
}
