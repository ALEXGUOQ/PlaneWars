package com.hukai.demo.planewars.objs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Version 1.0
 * Date: 2014-10-02
 * Author: hukai.me
 */
public abstract class BaseObj {
    public float x;
    public float y;
    public float w;
    public float h;
    public float centerX;
    public float centerY;
    public int speed;
    public boolean isAlive;
    public Paint paint = new Paint();

    public BaseObj() {
    }

    public BaseObj(Context context) {
    }

    public abstract void init(int speedRate, float centerX, float centerY);

    public abstract void draw(Canvas canvas);

    public void setCenterX(float centerX) {
        this.centerX = centerX;
        this.x = centerX - w / 2;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
        this.y = centerY - h / 2;
    }
}
