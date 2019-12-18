package com.owant.thinkmap.view;

import android.util.Log;
import android.view.View;

import com.owant.thinkmap.model.ForTreeItem;
import com.owant.thinkmap.model.NodeModel;
import com.owant.thinkmap.model.TreeModel;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by zm on 2019/12/11.
 */
public class BottomTreeLayoutManager implements TreeLayoutManager {

    final int msg_standard_layout = 1;
    final int msg_correct_layout = 2;
    final int msg_box_call_back = 3;

    private ViewBox mViewBox;
    private int mDy;
    private int mDx;
    private int mHeight;

    public BottomTreeLayoutManager(int dx, int dy, int height) {
        mViewBox = new ViewBox();

        this.mDx = dx;
        this.mDy = dy;
        this.mHeight = height;
    }

    @Override
    public void onTreeLayout(final TreeView treeView) {

        final TreeModel<String> mTreeModel = treeView.getTreeModel();
        if (mTreeModel != null) {

            View rootView = treeView.findNodeViewFromNodeModel(mTreeModel.getRootNode());
            if (rootView != null) {

                //根节点位置
                rootTreeViewLayout((NodeView) rootView);
            }

            mTreeModel.addForTreeItem(new ForTreeItem<NodeModel<String>>() {
                @Override
                public void next(int msg, NodeModel<String> next) {
                    doNext(msg, next, treeView);
                }
            });

            //基本布局
            mTreeModel.ergodicTreeInWith(msg_standard_layout);

            //纠正
            mTreeModel.ergodicTreeInWith(msg_correct_layout);

            mViewBox.clear();
            mTreeModel.ergodicTreeInDeep(msg_box_call_back);
        }
    }


    @Override
    public ViewBox onTreeLayoutCallBack() {
        if (mViewBox != null) {
            return mViewBox;
        } else {
            return null;
        }
    }

    private void doNext(int msg, NodeModel<String> next, TreeView treeView) {
        View view = treeView.findNodeViewFromNodeModel(next);

        if (msg == msg_standard_layout) {
            //标准分布
            standardLayout(treeView, (NodeView) view);
        } else if (msg == msg_correct_layout) {
            //纠正
            correctLayout(treeView, (NodeView) view);
        } else if (msg == msg_box_call_back) {

            //View的大小变化
            int left = view.getLeft();
            int top = view.getTop();
            int bottom = view.getBottom();
            int right = view.getRight();

            //     *******
            //     *     *
            //     *     *
            //     *******

            if (left < mViewBox.left) {
                mViewBox.left = left;
            }
            if (top < mViewBox.top) {
                mViewBox.top = top;
            }
            if (bottom > mViewBox.bottom) {
                mViewBox.bottom = bottom;
            }
            if (right > mViewBox.right) {
                mViewBox.right = right;
            }
        }
    }

    /**
     * 布局纠正
     *
     * @param treeView
     * @param next
     */
    public void correctLayout(TreeView treeView, NodeView next) {

        TreeModel mTree = treeView.getTreeModel();
        int count = next.getTreeNode().getChildNodes().size();

        if (next.getParent() != null && count >= 2) {
            NodeModel<String> ln = next.getTreeNode().getChildNodes().get(0);
            NodeModel<String> rn = next.getTreeNode().getChildNodes().get(count - 1);
            Log.i("see fc", next.getTreeNode().getValue() + ":" + ln.getValue() + "," + rn.getValue());


            int ltDr = next.getLeft() - treeView.findNodeViewFromNodeModel(ln).getRight() + mDx;
            int rtDr = treeView.findNodeViewFromNodeModel(rn).getLeft() - next.getRight() + mDx;

            //上移动
            ArrayList<NodeModel<String>> allLowNodes = mTree.getAllLowNodes(ln);
            ArrayList<NodeModel<String>> allPreNodes = mTree.getAllPreNodes(rn);

            for (NodeModel<String> low : allLowNodes) {
                NodeView view = (NodeView) treeView.findNodeViewFromNodeModel(low);
                moveNodeLayout(treeView, view, rtDr);
            }

            for (NodeModel<String> pre : allPreNodes) {
                NodeView view = (NodeView) treeView.findNodeViewFromNodeModel(pre);
                moveNodeLayout(treeView, view, -ltDr);
            }
        }
    }

    /**
     * 标准分布
     *
     * @param treeView
     * @param rootView
     */
    private void standardLayout(TreeView treeView, NodeView rootView) {
        NodeModel<String> treeNode = rootView.getTreeNode();
        if (treeNode != null) {
            //所有的子节点
            LinkedList<NodeModel<String>> childNodes = treeNode.getChildNodes();
            int size = childNodes.size();
            int mid = size / 2;
            int r = size % 2;

            //基线
            //        a
            //    b   |   c
            //
            //
            int left = rootView.getLeft() + rootView.getMeasuredWidth() / 2;
            int top = rootView.getBottom() + mDy;

            int right = 0;
            int bottom = 0;

            if (size == 0) {
                return;
            } else if (size == 1) {
                NodeView midChildNodeView = (NodeView) treeView.findNodeViewFromNodeModel(childNodes.get(0));

                left = left - midChildNodeView.getMeasuredWidth() / 2;
                right = left + midChildNodeView.getMeasuredWidth();
                bottom = top + midChildNodeView.getMeasuredHeight();
                midChildNodeView.layout(left, top, right, bottom);
            } else {
                int leftLeft = left;
                int leftTop = top;
                int leftRight = 0;
                int leftBottom = 0;

                int rightLeft = left;
                int rightTop = top;
                int rightRight = 0;
                int rightBottom = 0;

                if (r == 0) {//偶数
                    for (int i = mid - 1; i >= 0; i--) {
                        NodeView leftView = (NodeView) treeView.findNodeViewFromNodeModel(childNodes.get(i));
                        NodeView rightView = (NodeView) treeView.findNodeViewFromNodeModel(childNodes.get(size - i - 1));


                        if (i == mid - 1) {
                            leftLeft = leftLeft - mDx / 2 - leftView.getMeasuredWidth();
                            leftRight = leftLeft + leftView.getMeasuredWidth();
                            leftBottom = leftTop + leftView.getMeasuredHeight();

                            rightLeft = rightLeft + mDx / 2;
                            rightRight = rightLeft + rightView.getMeasuredWidth();
                            rightBottom = rightTop + rightView.getMeasuredHeight();
                        } else {
                            leftLeft = leftLeft - mDx - leftView.getMeasuredWidth();
                            leftRight = leftLeft + leftView.getMeasuredWidth();
                            leftBottom = leftTop + leftView.getMeasuredHeight();

                            rightLeft = rightLeft + mDx;
                            rightRight = rightLeft + rightView.getMeasuredWidth();
                            rightBottom = rightTop + rightView.getMeasuredHeight();
                        }

                        leftView.layout(leftLeft, rightTop, leftRight, leftBottom);
                        rightView.layout(rightLeft, rightTop, rightRight, rightBottom);

                        rightLeft = rightView.getRight();
                    }

                } else {
                    NodeView midView = (NodeView) treeView.findNodeViewFromNodeModel(childNodes.get(mid));
                    midView.layout(left - midView.getMeasuredWidth()/2, top, left - midView.getMeasuredWidth()/2 + midView.getMeasuredWidth(),
                            top + midView.getMeasuredHeight());

                    leftLeft = midView.getLeft();
                    rightLeft = midView.getRight();

                    for (int i = mid - 1; i >= 0; i--) {
                        NodeView leftView = (NodeView) treeView.findNodeViewFromNodeModel(childNodes.get(i));
                        NodeView rightView = (NodeView) treeView.findNodeViewFromNodeModel(childNodes.get(size - i - 1));

                        leftLeft = leftLeft - mDx - leftView.getMeasuredWidth();
                        leftRight = leftLeft + leftView.getMeasuredWidth();
                        leftBottom = leftTop + leftView.getMeasuredHeight();

                        rightLeft = rightLeft + mDx;
                        rightRight = rightLeft + rightView.getMeasuredWidth();
                        rightBottom = rightTop + rightView.getMeasuredHeight();

                        leftView.layout(leftLeft, rightTop, leftRight, leftBottom);
                        rightView.layout(rightLeft, rightTop, rightRight, rightBottom);
                        rightLeft = rightView.getRight();
                    }
                }
            }
        }
    }

    /**
     * 移动
     *
     * @param rootView
     * @param dx
     */
    private void moveNodeLayout(TreeView superTreeView, NodeView rootView, int dx) {

        Deque<NodeModel<String>> queue = new ArrayDeque<>();
        NodeModel<String> rootNode = rootView.getTreeNode();
        queue.add(rootNode);
        while (!queue.isEmpty()) {
            rootNode = queue.poll();
            rootView = (NodeView) superTreeView.findNodeViewFromNodeModel(rootNode);
            int l = rootView.getLeft() + dx;
            int t = rootView.getTop();
            rootView.layout(l, t, l + rootView.getMeasuredWidth(), t + rootView.getMeasuredHeight());

            LinkedList<NodeModel<String>> childNodes = rootNode.getChildNodes();
            for (NodeModel<String> item : childNodes) {
                queue.add(item);
            }
        }
    }


    /**
     * root节点的定位
     *
     * @param rootView
     */
    private void rootTreeViewLayout(NodeView rootView) {
        int lr = mDy;
        int tr = mDx;
        int rr = lr + rootView.getMeasuredWidth();
        int br = tr + rootView.getMeasuredHeight();
        rootView.layout(lr, tr, rr, br);
    }
}
