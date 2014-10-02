package com.hukai.demo.planewars.objs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.hukai.demo.planewars.ActionMove;
import com.hukai.demo.planewars.ActionShoot;
import com.hukai.demo.planewars.MainActivity;
import com.hukai.demo.planewars.R;
import com.hukai.demo.planewars.data.ConstantData;

import java.util.List;

/**
 * Version 1.0
 * Date: 2014-10-02
 * Author: hukai.me
 */
public class SelfPlane extends BaseObj implements ActionShoot, ActionMove {

    private static final String TAG = SelfPlane.class.getSimpleName();
    private SelfBullet selfBullet;
    private Bitmap mPlaneBmp;
    private Bitmap mPlaneDieBmp;

    public SelfPlane(Context context) {
        speed = ConstantData.BASE_PLANE_SPEED;
        mPlaneBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.plane);
        mPlaneDieBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.plane);
        w = mPlaneBmp.getWidth();
        h = mPlaneBmp.getHeight();
        x = (MainActivity.mScreenWidth - w) / 2;
        y = MainActivity.mScreenHeight - h;
        centerX = x + w / 2;
        centerY = y + h / 2;
        selfBullet = BulletFactory.newSelfBullet(context);
    }

    @Override
    public void init(int speedRate, float centerX, float centerY) {
        speed = ConstantData.BASE_PLANE_SPEED  * (speedRate + 1);
    }

    @Override
    public void draw(Canvas canvas) {
        if (isAlive) {
            canvas.save();
            canvas.drawBitmap(mPlaneBmp, x, y, paint);
            canvas.restore();
        } else {
            canvas.save();
            canvas.drawBitmap(mPlaneDieBmp, x, y, paint);
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
        if (!selfBullet.isAlive) {
            return;
        }
        for (EnemyPlane enemyPlane : enemyPlanes) {
            if (enemyPlane.isCanCollide() && selfBullet.checkCollide(enemyPlane)) {
                enemyPlane.beAttacked(selfBullet.mPower);
                if (enemyPlane.isExplosion) {
                    MainActivity.mSumScore += enemyPlane.initBlood;
                }
                break;
            }
        }
        selfBullet.draw(canvas);
    }

    public void initBullet() {
        if (!selfBullet.isAlive) {
            selfBullet.init(ConstantData.BASE_SPEED_RATE, centerX, centerY);
        }
    }
}
