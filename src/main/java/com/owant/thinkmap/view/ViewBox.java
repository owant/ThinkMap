package com.owant.thinkmap.view;

/**
 * Created by owant on 06/03/2017.
 * 用于记录当前树形图的大小
 */
public class ViewBox {
    public int top;
    public int left;
    public int right;
    public int bottom;

    public void clear() {
        this.top=0;
        this.left=0;
        this.right=0;
        this.bottom=0;
    }

    @Override
    public String toString() {
        return "ViewBox{" +
                "top=" + top +
                ", left=" + left +
                ", right=" + right +
                ", bottom=" + bottom +
                '}';
    }
}
