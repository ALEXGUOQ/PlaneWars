package com.hukai.demo.planewars.objs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.hukai.demo.planewars.ActionMove;
import com.hukai.demo.planewars.MainActivity;
import com.hukai.demo.planewars.R;
import com.hukai.demo.planewars.Constant;

import java.util.Random;

/**
 * Version 1.0
 * Date: 2014-10-02
 * Author: hukai.me
 */
public class EnemyPlane extends BaseObj implements ActionMove {
    public int initBlood;
    public boolean isExplosion;

    private int blood;
    private boolean isVisible;
    private Bitmap mEnemyBmp;

    public EnemyPlane(Context context) {
        mEnemyBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        w = mEnemyBmp.getWidth();
        h = mEnemyBmp.getHeight();
        blood = Constant.BASE_PLANE_BLOOD;
        initBlood = blood;
    }

    @Override
    public void init(int speedRate, float centerX, float centerY) {
        isAlive = true;
        setCenterX(centerX);
        setCenterY(centerY);
        Random random = new Random();
        speed = (Constant.BASE_PLANE_SPEED + random.nextInt(3)) * speedRate;
        x = random.nextInt((int) (MainActivity.mScreenWidth - w));
        y = random.nextInt(speed) - h;
    }

    @Override
    public void draw(Canvas canvas) {
        if (isAlive) {
            if (!isExplosion) {
                if (isVisible) {
                    canvas.save();
                    canvas.drawBitmap(mEnemyBmp, x, y, paint);
                    canvas.restore();
                }
                move(0, speed);
            } else {
                canvas.save();
                canvas.drawBitmap(mEnemyBmp, x, y, paint);
                canvas.restore();
                isExplosion = false;
                isAlive = false;
            }
        }
    }

    @Override
    public void move(float moveX, float moveY) {
        if (y < MainActivity.mScreenHeight) {
            y = y + moveY;
        } else {
            isAlive = false;
        }
        if (y + h > 0 && y < MainActivity.mScreenHeight) {
            isVisible = true;
        } else {
            isVisible = false;
        }
    }

    public void beAttacked(int power) {
        blood = blood - power;
        if (blood <= 0) {
            isExplosion = true;
        }
    }

    public boolean isCollide(BaseObj obj) {
        if (x + w <= obj.x) {
            return false;
        } else if (obj.x + obj.w <= x) {
            return false;
        } else if (y + h <= obj.y) {
            return false;
        } else if (obj.y + obj.h <= y) {
            return false;
        }
        return true;
    }

    public boolean canCollide() {
        return isAlive && !isExplosion && isVisible;
    }
}
