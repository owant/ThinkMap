package com.owant.thinkmap.view;

/**
 * Created by owant on 07/03/2017.
 */

public interface TreeLayoutManager {
    /**
     * 进行树形结构的位置计算
     */
    void onTreeLayout(TreeView treeView);

    /**
     * 位置分布好后的回调,用于确认ViewGroup的大小
     */
    ViewBox onTreeLayoutCallBack();

    /**
     * 修正位置
     *
     * @param treeView
     * @param next
     */
    void correctLayout(TreeView treeView, NodeView next);
}
