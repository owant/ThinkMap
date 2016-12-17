package com.owant.drawtreeview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;

import com.owant.drawtreeview.model.NotFindNodeException;
import com.owant.drawtreeview.model.Tree;
import com.owant.drawtreeview.model.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by owant on 17/12/2016.
 */

public class TreeView extends RelativeLayout {

    /**
     * the default x,y mDx
     */
    private int mDx;
    private int mDy;
    private int mWith;
    private int mHeight;

    private Context mContext;

    public Tree<String> tree;
    private ArrayList<NodeView> nodeViews;

    public TreeView(Context context) {
        this(context, null, 0);
    }

    public TreeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TreeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;
        nodeViews = new ArrayList<NodeView>();

        mDx = dp2px(mContext, 20);
        mDy = dp2px(mContext, 20);

        addNoteView();
    }

    private void addNoteView() {
        if (tree != null) {
            TreeNode<String> rootNode = tree.getRootNode();
            Deque<TreeNode<String>> queue = new ArrayDeque<>();
            queue.add(rootNode);
            while (!queue.isEmpty()) {
                rootNode = (TreeNode<String>) queue.poll();

                NodeView nodeView = new NodeView(mContext);
                nodeView.setTreeNode(rootNode);

                nodeViews.add(nodeView);
                this.addView(nodeView);

                LinkedList<TreeNode<String>> childNodes = rootNode.getChildNodes();
                if (childNodes.size() > 0) {
                    for (TreeNode<String> item : childNodes) {
                        queue.add(item);
                    }
                }
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mHeight = getMeasuredHeight();
        mWith = getMeasuredWidth();

        if (tree != null) {
            TreeNode<String> rootNode = tree.getRootNode();
            setNoteViewLayout(rootNode);

            //矫正
            for (int i = nodeViews.size() - 1; i >= 0; i--) {
                NodeView v = nodeViews.get(i);
                if (v.getTreeNode().getParentNode() != null && v.getTreeNode().getChildNodes().size() > 2) {
                    int size = v.getTreeNode().getChildNodes().size();
                    notifyFatherLayout(v.getTreeNode(),
                            findTreeNodeView(v.getTreeNode().getChildNodes().get(0)),
                            findTreeNodeView(v.getTreeNode().getChildNodes().get(size - 1)));
                }
            }
        }
    }

    private void setNoteViewLayout(TreeNode<String> rootNode) {

        NodeView fatherView = findTreeNodeView(rootNode);

        int fatherWidth = fatherView.getMeasuredWidth();
        int fatherHeight = fatherView.getMeasuredHeight();

        TreeNode<String> treeNode = fatherView.getTreeNode();

        if (treeNode.getParentNode() == null) {
            int top = mHeight / 2 - fatherHeight / 2;
            fatherView.layout(getPaddingLeft() + mDy, top,
                    getPaddingLeft() + mDy + fatherWidth, top + fatherHeight);
        }

        //获得子叶子
        LinkedList<TreeNode<String>> childNodes = treeNode.getChildNodes();

        int size = treeNode.getChildNodes().size();
        //获取中间item
        int mid = size / 2;
        //奇数偶数
        int r = size % 2;

        if (size == 0) {
            return;
        } else if (size == 1) {
            TreeNode<String> midTreeNode = childNodes.get(0);
            NodeView midView = findTreeNodeView(midTreeNode);

            int starY = fatherView.getTop() + fatherView.getMeasuredHeight() / 2;
            starY = starY - midView.getMeasuredHeight() / 2;

            int startX = fatherView.getLeft() + fatherWidth + mDx;
            midView.layout(startX, starY, startX + midView.getMeasuredWidth(), starY + midView.getMeasuredHeight());

            setNoteViewLayout(midTreeNode);

        } else {

            //基线
            int startY = fatherView.getTop() + fatherView.getMeasuredHeight() / 2;
            int startX = fatherView.getLeft() + fatherWidth + mDx;

            int dxTopStartY = startY;
            int dxBottomStartY = startY;


            if (r != 0) {//奇数
                //layout中间的
                TreeNode<String> midTreeNode = childNodes.get(mid);
                NodeView midView = findTreeNodeView(midTreeNode);
                Log.i("view", midView + "");

                startY = startY - midView.getMeasuredHeight() / 2;

                midView.layout(startX, startY, startX + midView.getMeasuredWidth(), startY + midView.getMeasuredHeight());

                dxTopStartY = midView.getTop();
                dxBottomStartY = midView.getTop() + midView.getMeasuredHeight();
                setNoteViewLayout(midTreeNode);
            }

            for (int i = mid - 1; i >= 0; i--) {

                int bottom = size - i - 1;
                TreeNode<String> topNode = childNodes.get(i);
                TreeNode<String> bottomNode = childNodes.get(bottom);

                NodeView topView = findTreeNodeView(topNode);
                NodeView bottomView = findTreeNodeView(bottomNode);

                Log.i("view", topView + "");
                Log.i("view", bottomView + "");

                //上
                dxTopStartY = dxTopStartY - mDy - topView.getMeasuredHeight();
                topView.layout(startX, dxTopStartY, startX + topView.getMeasuredWidth(), dxTopStartY + topView.getMeasuredHeight());
                setNoteViewLayout(topNode);

                //下
                dxBottomStartY = dxBottomStartY + mDy;
                bottomView.layout(startX, dxBottomStartY, startX + bottomView.getMeasuredWidth(), dxBottomStartY + bottomView.getMeasuredHeight());

                dxBottomStartY = dxBottomStartY + bottomView.getMeasuredHeight();

                setNoteViewLayout(bottomNode);
            }

        }
    }

    private void notifyFatherLayout(TreeNode<String> fatherNode, NodeView shouldTopView, NodeView shouldBottomView) {

        NodeView preRootView = null;
        NodeView botRootView = null;

        try {
            preRootView = findTreeNodeView(tree.getPreNode(fatherNode));
            if (preRootView != null) {
                Log.i("pre", preRootView.getText().toString());
            }
        } catch (NotFindNodeException e) {
            e.printStackTrace();
        }

        try {
            botRootView = findTreeNodeView(tree.getLowNode(fatherNode));
            if (preRootView != null) {
                Log.i("bot", botRootView.getText().toString());
            }
        } catch (NotFindNodeException e) {
            e.printStackTrace();
        }

        if (preRootView != null) {

            NodeView rootView = findTreeNodeView(fatherNode);

//            LinkedList<TreeNode<String>> cs = preRootView.getTreeNode().getChildNodes();
//            if (cs.size() > 2) {
//                preRootView = findTreeNodeView(cs.get(cs.size() - 1));
//            }

            //先处理上层
            int haveHeightTop = rootView.getTop() - preRootView.getTop() - preRootView.getMeasuredHeight();
            int shouldHeightTop = rootView.getTop() - (shouldTopView.getTop() - mDy);
            int should_dy = shouldHeightTop - haveHeightTop;

            moveDown(rootView, should_dy);
            LinkedList<TreeNode<String>> childNodes = fatherNode.getParentNode().getChildNodes();
            boolean shouldMove = false;

            for (int i = 0; i < childNodes.size(); i++) {
                if (shouldMove) {
                    moveDown(findTreeNodeView(childNodes.get(i)), should_dy);
                }
                if (childNodes.get(i) == fatherNode) {
                    shouldMove = true;
                } else if (childNodes.get(i).getFloor() != fatherNode.getFloor()) {
                    shouldMove = false;
                }
            }
        }

        if (botRootView != null) {

            NodeView rootView = findTreeNodeView(fatherNode);
            //下层
            int haveHeightTop = botRootView.getTop() - (rootView.getTop() + rootView.getMeasuredHeight() + mDy);
            int shouldHeightTop = shouldBottomView.getTop() - rootView.getTop();

            int should_dy = shouldHeightTop - haveHeightTop;

            moveDown(botRootView, should_dy);
            TreeNode<String> should = null;
            try {
                should = tree.getLowNode(botRootView.getTreeNode());
                while (should != null) {
                    moveDown(findTreeNodeView(should), should_dy);
                    should = tree.getLowNode(should);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void moveDown(NodeView rootView, int should_dy) {

        int l = rootView.getLeft();
        int t = rootView.getTop() + should_dy;
        rootView.layout(l, t, l + rootView.getMeasuredWidth(), t + rootView.getMeasuredHeight());

        //子节点也要移动
        LinkedList<TreeNode<String>> childNodes = rootView.getTreeNode().getChildNodes();

        for (TreeNode<String> child : childNodes) {
            moveDown(findTreeNodeView(child), should_dy);
        }

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (tree != null) {
            drawTreeLine(canvas, tree.getRootNode());
        }
        super.dispatchDraw(canvas);
    }

    private void drawTreeLine(Canvas canvas, TreeNode<String> root) {
        NodeView fatherView = findTreeNodeView(root);

        LinkedList<TreeNode<String>> childNodes = root.getChildNodes();
        for (TreeNode<String> node : childNodes) {
            drawLineToView(canvas, fatherView, findTreeNodeView(node));
            drawTreeLine(canvas, node);
        }
    }

    private void drawLineToView(Canvas canvas, View from, View to) {

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(Color.BLUE);

        int top = from.getTop();
        int formY = top + from.getMeasuredHeight() / 2;
        int formX = from.getRight();

        int top1 = to.getTop();
        int toY = top1 + to.getMeasuredHeight() / 2;
        int toX = to.getLeft();

        Path path = new Path();
        path.moveTo(formX, formY);
        path.quadTo(toX - dp2px(mContext, 2), toY, toX, toY);

        canvas.drawPath(path, paint);
    }

    private NodeView findTreeNodeView(TreeNode<String> node) {
        NodeView v = null;
        for (NodeView view : nodeViews) {
            if (view.getTreeNode() == node) {
                v = view;
                return v;
            }
        }
        return v;
    }

    public Tree<String> getTree() {
        return tree;
    }

    public void setTree(Tree<String> tree) {
        this.tree = tree;
        addNoteView();
        invalidate();
    }

    /**
     * dp to px
     */
    public int dp2px(Context context, float dpVal) {
        int result = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources()
                .getDisplayMetrics());
        return result;
    }


}
