package com.owant.drawtreeview.model;

import java.util.LinkedList;

/**
 * Created by owant on 16/12/2016.
 */

public class TreeNode<T> {
    /**
     * the parent node,if root node parent node=null;
     */
    public TreeNode<T> parentNode;

    /**
     * the data value
     */
    public T value;

    /**
     * have the child nodes
     */
    public LinkedList<TreeNode<T>> childNodes;

    /**
     * focus tag for the tree add nodes
     */
    public boolean focus;

    /**
     * index of the tree floor
     */
    public int floor;

    public TreeNode(T value) {
        this.value = value;
        this.childNodes = new LinkedList<TreeNode<T>>();

//        this.focus = false;
//        this.parentNode = null;
    }

    public TreeNode<T> getParentNode() {
        return parentNode;
    }

    public void setParentNode(TreeNode<T> parentNode) {
        this.parentNode = parentNode;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public LinkedList<TreeNode<T>> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(LinkedList<TreeNode<T>> childNodes) {
        this.childNodes = childNodes;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }
}
