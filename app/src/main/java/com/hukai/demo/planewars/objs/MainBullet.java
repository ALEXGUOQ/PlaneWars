package com.hukai.demo.planewars.objs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.hukai.demo.planewars.ActionMove;
import com.hukai.demo.planewars.R;
import com.hukai.demo.planewars.Constant;

/**
 * Version 1.0
 * Date: 2014-10-02
 * Author: hukai.me
 */
public class MainBullet extends BaseObj implements ActionMove {

    public int mPower;

    private Bitmap mBulletBmp;

    public MainBullet(Context context) {
        mPower = Constant.BASE_BULLET_POWER;
        mBulletBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet);
        w = mBulletBmp.getWidth();
        h = mBulletBmp.getHeight();
    }

    @Override
    public void init(int speedRate, float centerX, float centerY) {
        isAlive = true;
        speed = Constant.BASE_BULLET_SPEED * speedRate;
        x = centerX - w / 2;
        y = centerY - h;
    }

    @Override
    public void draw(Canvas canvas) {
        if (isAlive) {
            canvas.save();
            canvas.drawBitmap(mBulletBmp, x, y, paint);
            canvas.restore();
            move(0, speed);
        }
    }

    @Override
    public void move(float moveX, float moveY) {
        if (y >= 0) {
            y = y - moveY;
        } else {
            isAlive = false;
        }
    }

    public boolean checkCollide(BaseObj obj) {
//        if (y >= obj.y && x >= obj.x && x + w <= obj.x + obj.w) {
//            isAlive = false;
//            return true;
//        }
        if (x + w <= obj.x) {
            return false;
        } else if (obj.x + obj.w <= x) {
            return false;
        } else if (y + h <= obj.y) {
            return false;
        } else if (obj.y + obj.h <= y) {
            if (y - speed < obj.y) {
                Log.d("MainBullet", "isAlive = false");
                isAlive = false;
                return true;
            }
        }
        isAlive = false;
        return true;
    }
}
