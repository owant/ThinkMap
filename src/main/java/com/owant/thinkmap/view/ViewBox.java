package com.owant.thinkmap.view;

/**
 * Created by owant on 06/03/2017.
 * 用于记录当前树形图的大小
 */
public class ViewBox {
    public int left;
    public int top;
    public int right;
    public int bottom;

    @Override
    public String toString() {
        return "ViewBox{" +
                "left=" + left +
                ", top=" + top +
                ", right=" + right +
                ", bottom=" + bottom +
                '}';
    }
}
