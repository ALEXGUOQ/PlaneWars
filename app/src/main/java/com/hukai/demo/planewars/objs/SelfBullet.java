package com.hukai.demo.planewars.objs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.hukai.demo.planewars.ActionMove;
import com.hukai.demo.planewars.R;
import com.hukai.demo.planewars.data.ConstantData;

/**
 * Version 1.0
 * Date: 2014-10-02
 * Author: hukai.me
 */
public class SelfBullet extends BaseObj implements ActionMove{

    public int mPower;

    private Bitmap mBulletBmp;

    public SelfBullet(Context context) {
        mPower = ConstantData.BASE_BULLET_POWER;
        mBulletBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet);
        w = mBulletBmp.getWidth();
        h = mBulletBmp.getHeight();
    }

    @Override
    public void init(int speedRate, float centerX, float centerY) {
        isAlive = true;
        speed = ConstantData.BASE_BULLET_SPEED * speedRate;
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
            y -= moveY;
        } else {
            isAlive = false;
        }
    }

    public boolean checkCollide(BaseObj obj) {
        if (x + w <= obj.x) {
            return false;
        } else if (obj.x + obj.w <= x) {
            return false;
        } else if (y + h <= obj.y) {
            return false;
        } else if (obj.y + obj.h <= y) {
            if (y - speed < obj.y) {
                isAlive = false;
                return true;
            }
        }
        isAlive = false;
        return true;
    }
}
