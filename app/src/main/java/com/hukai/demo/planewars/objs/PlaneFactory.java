package com.hukai.demo.planewars.objs;

import android.content.Context;

/**
 * Version 1.0
 * Date: 2014-10-02
 * Author: hukai.me
 */
public class PlaneFactory {
    public static EnemyPlane newEnemyPlane(Context context) {
        return new EnemyPlane(context);
    }

    public static SelfPlane newSelfPlane(Context context) {
        return new SelfPlane(context);
    }
}
