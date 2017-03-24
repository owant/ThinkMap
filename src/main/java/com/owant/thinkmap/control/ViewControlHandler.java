package com.owant.thinkmap.control;

import android.view.MotionEvent;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by owant on 09/02/2017.
 */

public class ViewControlHandler {
    /**
     * 作用于的View
     */
    private View mView;

    private int lastX = 0;
    private int lastY = 0;

    private int mode = 0;
    private float oldDist;
    private int midDx = 30;

    public ViewControlHandler(View view) {
        this.mView = view;
    }

    public boolean move(MotionEvent event) {

        int currentX = (int) event.getRawX();//获得手指当前的坐标,相对于屏幕
        int currentY = (int) event.getRawY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = 1;
                break;
            case MotionEvent.ACTION_UP:
                mode = 0;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //将模式进行为负数这样，多指下，抬起不会触发移动
                mode = -2;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //多指按下时记录两者的距离
                oldDist = spacing(event);
                mode += 1;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode >= 2) {
                    //移动后的距离
                    float newDist = spacing(event);
                    float scaleX = ViewHelper.getScaleX(mView);
                    ViewHelper.setPivotX(mView, 0);
                    ViewHelper.setPivotY(mView, mView.getHeight() / 2.0f);

                    //新和旧的比为缩放的倍数
                    float v = scaleX * (newDist / oldDist);
                    if (newDist > oldDist + midDx) {//增大
                        if (v <= 1.2) {
                            ViewHelper.setScaleX(mView, v);
                            ViewHelper.setScaleY(mView, v);
                            oldDist = newDist;
                        }
                    }
                    if (newDist < oldDist - midDx) {//减小
                        if (v > 0.3) {
                            ViewHelper.setScaleX(mView, v);
                            ViewHelper.setScaleY(mView, v);
                            oldDist = newDist;
                        }
                    }
                } else if (mode == 1) {
                    int deltaX = currentX - lastX;
                    int deltaY = currentY - lastY;

                    int translationX = (int) ViewHelper.getTranslationX(mView) + deltaX;
                    int translationY = (int) ViewHelper.getTranslationY(mView) + deltaY;

                    ViewHelper.setTranslationX(mView, translationX);
                    ViewHelper.setTranslationY(mView, translationY);
                }
                break;
        }
        lastX = currentX;
        lastY = currentY;
        return true;

    }


    /**
     * 两点之间的距离
     *
     * @param event
     * @return
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

}
