package com.hukai.demo.planewars;

import android.graphics.Canvas;

import com.hukai.demo.planewars.objs.EnemyPlane;

import java.util.List;

/**
 * Version 1.0
 * Date: 2014-10-02
 * Author: hukai.me
 */
public interface ActionFire {

    public void fire(Canvas canvas, List<EnemyPlane> enemyPlanes);

}
