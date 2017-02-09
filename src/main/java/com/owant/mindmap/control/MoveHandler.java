package com.owant.mindmap.control;

import android.view.MotionEvent;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by owant on 09/02/2017.
 */

public class MoveHandler {
    /**
     * 作用于的View
     */
    private View mView;

    private int lastX = 0;
    private int lastY = 0;

    public MoveHandler(View view) {
        this.mView = view;
    }

    public boolean move(MotionEvent event) {
        int action = event.getAction();
        int currentX = (int) event.getRawX();//获得手指当前的坐标,相对于屏幕
        int currentY = (int) event.getRawY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = currentX - lastX;
                int deltaY = currentY - lastY;

                int translationX = (int) ViewHelper.getTranslationX(mView) + deltaX;
                int translationY = (int) ViewHelper.getTranslationY(mView) + deltaY;

                ViewHelper.setTranslationX(mView, translationX);
                ViewHelper.setTranslationY(mView, translationY);
                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }

        lastX = currentX;
        lastY = currentY;
        return true;
    }

}
