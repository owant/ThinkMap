package com.owant.thinkmap.control;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.nineoldandroids.view.ViewHelper;
import com.owant.thinkmap.line.EaseCubicInterpolator;

/**
 * Created by owant on 09/02/2017.
 */

public class MenuMEventHandler {
    /**
     * 作用于的View
     */
    private View mView;

    private int lastX = 0;
    private int lastY = 0;

    private int mode = 0;
    private int mWith;
    private int mDx;
    boolean state = false;
    int flag = 0;

    public MenuMEventHandler(View view, int with, int dx) {
        this.mView = view;
        this.mWith = with;
        this.mDx = dx;
    }

    public boolean move(MotionEvent event) {
        int currentX = (int) event.getRawX();//获得手指当前的坐标,相对于屏幕
        int currentY = (int) event.getRawY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                state = false;
                mode = 1;
                break;
            case MotionEvent.ACTION_UP:
                mode = 0;
                float tranX = ViewHelper.getTranslationX(mView);
                //进行自动展开和关闭
                if (flag == -1) {//打开
                    if (Math.abs(tranX) < mDx / 4) {
                        ViewHelper.setTranslationX(mView, 0);
                    } else {
                        //起点tranX，终点为-mDx
                        Animator transY = ObjectAnimator.ofFloat(mView, "translationX", tranX, -mDx);
                        transY.setDuration(200);
                        transY.setInterpolator(new EaseCubicInterpolator(0f,.58f,1f,.01f));
                        transY.start();
                    }

                } else if (flag == 1) {//关闭
                    //tranX增大
                    if (Math.abs(tranX) < mDx * (3 / 4)) {
                        ViewHelper.setTranslationX(mView, -mDx);
                    } else {
                        //起点tranX，终点为0
                        Animator transY = ObjectAnimator.ofFloat(mView, "translationX", tranX, 0);
                        transY.setDuration(200);
                        transY.setInterpolator(new EaseCubicInterpolator(0f,.58f,1f,.01f));
                        transY.start();
                    }
                }
                flag = 0;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //将模式进行为负数这样，多指下，抬起不会触发移动
                mode = -2;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //多指按下时记录两者的距离
                mode += 1;
                break;
            case MotionEvent.ACTION_MOVE:
                state = true;
                if (mode >= 2) {
                    //多指的操作
                    flag = 0;
                } else if (mode == 1) {
                    int deltaX = currentX - lastX;
                    int deltaY = currentY - lastY;

                    if (deltaX > 0) {
                        flag = 1;//关闭
                    } else {
                        flag = -1;//打开
                    }

                    if (Math.abs(deltaX) > Math.abs(deltaY)) {
                        //TODO 计算
                        //   ------------
                        //   -      |   -
                        //   ------------
                        //
                        float translationX = ViewHelper.getTranslationX(mView) + deltaX;
                        if (translationX > (-mDx) && translationX <= 0) {
                            ViewHelper.setTranslationX(mView, translationX);
                        }
                    }
                }
                break;
        }
        lastX = currentX;
        lastY = currentY;
        return state;
    }

}
