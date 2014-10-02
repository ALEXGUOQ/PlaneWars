package com.hukai.demo.planewars.objs;

import android.content.Context;

/**
 * Version 1.0
 * Date: 2014-10-02
 * Author: hukai.me
 */
public class BulletFactory {
    public static SelfBullet newSelfBullet(Context context) {
        return new SelfBullet(context);
    }
}
