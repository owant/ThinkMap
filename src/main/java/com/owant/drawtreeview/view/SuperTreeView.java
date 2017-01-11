package com.owant.drawtreeview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.owant.drawtreeview.R;
import com.owant.drawtreeview.model.Tree;
import com.owant.drawtreeview.model.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by owant on 09/01/2017.
 */

public class SuperTreeView extends ViewGroup {

    /**
     * the default x,y mDx
     */
    private int mDx;
    private int mDy;
    private int mWith;
    private int mHeight;
    private Context mContext;
    private Tree<String> mTree;
    private ArrayList<NodeView> mNodesViews;

    public SuperTreeView(Context context) {
        this(context, null, 0);
    }

    public SuperTreeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperTreeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mNodesViews = new ArrayList<>();
        mContext = context;

        mDx = dp2px(mContext, 26);
        mDy = dp2px(mContext, 22);

        test();
    }

    TreeNode<String> nodeA = new TreeNode<>("A");
    TreeNode<String> nodeB = new TreeNode<>("B");
    TreeNode<String> nodeC = new TreeNode<>("C");
    TreeNode<String> nodeD = new TreeNode<>("D");
    TreeNode<String> nodeE = new TreeNode<>("E");
    TreeNode<String> nodeF = new TreeNode<>("F");
    TreeNode<String> nodeG = new TreeNode<>("G");
    TreeNode<String> nodeH = new TreeNode<>("H");
    TreeNode<String> nodeI = new TreeNode<>("I");
    TreeNode<String> nodeJ = new TreeNode<>("J");
    TreeNode<String> nodeK = new TreeNode<>("K");
    TreeNode<String> nodeL = new TreeNode<>("L");
    TreeNode<String> nodeM = new TreeNode<>("M");
    TreeNode<String> nodeN = new TreeNode<>("N");
    TreeNode<String> nodeO = new TreeNode<>("O");
    TreeNode<String> nodeP = new TreeNode<>("P");
    TreeNode<String> nodeQ = new TreeNode<>("Q");
    TreeNode<String> nodeR = new TreeNode<>("R");
    TreeNode<String> nodeS = new TreeNode<>("S");
    TreeNode<String> nodeT = new TreeNode<>("T");
    TreeNode<String> nodeU = new TreeNode<>("U");
    TreeNode<String> nodeV = new TreeNode<>("V");
    TreeNode<String> nodeW = new TreeNode<>("W");
    TreeNode<String> nodeX = new TreeNode<>("X");
    TreeNode<String> nodeY = new TreeNode<>("Y");
    TreeNode<String> nodeZ = new TreeNode<>("Z");

    private void test() {
        Tree<String> tree = new Tree<>(nodeA);
        tree.setRootNode(nodeA);
//        tree.addNode(nodeA, nodeB, nodeC, nodeD, nodeE);
//        tree.addNode(nodeC, nodeI, nodeJ, nodeK, nodeL, nodeM, nodeN, nodeO);
//        tree.addNode(nodeJ, nodeP, nodeQ, nodeR, nodeS);
//        tree.addNode(nodeR, nodeT, nodeU, nodeV);
//        tree.addNode(nodeK,nodeW,nodeX,nodeY,nodeZ);

        tree.addNode(nodeA,nodeB,nodeC,nodeE);

        tree.addNode(nodeB,nodeD);
        tree.addNode(nodeC,nodeF);
        tree.addNode(nodeE,nodeG);

        tree.addNode(nodeD,nodeH);
        tree.addNode(nodeF,nodeI);
        tree.addNode(nodeG,nodeJ);

        tree.addNode(nodeI,nodeK,nodeL,nodeM);
        tree.addNode(nodeK,nodeN,nodeO,nodeP,nodeQ);
        tree.addNode(nodeP,nodeR,nodeS,nodeT,nodeU,nodeV);

        tree.addNode(nodeH,nodeW,nodeX,nodeY,nodeZ);

        setTree(tree);
    }

    /**
     * 添加view到Group
     */
    private void onAddNodeViews() {
        if (mTree != null) {
            TreeNode<String> rootNode = mTree.getRootNode();
            Deque<TreeNode<String>> deque = new ArrayDeque<>();
            deque.add(rootNode);
            while (!deque.isEmpty()) {
                TreeNode<String> poll = deque.poll();
                NodeView nodeView = new NodeView(mContext);
                nodeView.setTreeNode(poll);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                nodeView.setLayoutParams(lp);

                this.addView(nodeView);
                mNodesViews.add(nodeView);

                LinkedList<TreeNode<String>> childNodes = poll.getChildNodes();
                for (TreeNode<String> ch : childNodes) {
                    deque.push(ch);
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int size = getChildCount();
        for (int i = 0; i < size; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mHeight = getMeasuredHeight();
        mWith = getMeasuredWidth();

        NodeView rootView = findTreeNodeView(mTree.getRootNode());
        if (rootView != null) {
            //root的位置
            rootTreeViewLayout(rootView);
            //标准位置
            for (NodeView nv : mNodesViews) {
                standardTreeChildLayout(nv);
            }

//            //基于父子的移动
            for (NodeView nv : mNodesViews) {
                fatherChildCorrect(nv);
            }

//            fatherChildCorrect(findTreeNodeView(nodeC));
        }


    }

    private void rootTreeViewLayout(NodeView rootView) {
        int lr = mDy;
        int tr = mHeight / 2 - rootView.getMeasuredHeight() / 2;
        int rr = lr + rootView.getMeasuredWidth();
        int br = tr + rootView.getMeasuredHeight();
        rootView.layout(lr, tr, rr, br);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mTree != null) {
            drawTreeLine(canvas, mTree.getRootNode());
        }
        super.dispatchDraw(canvas);
    }

    /**
     * 标准的位置分布
     *
     * @param rootView
     */
    private void standardTreeChildLayout(NodeView rootView) {
        TreeNode<String> treeNode = rootView.getTreeNode();
        if (treeNode != null) {
            //所有的子节点
            LinkedList<TreeNode<String>> childNodes = treeNode.getChildNodes();
            int size = childNodes.size();
            int mid = size / 2;
            int r = size % 2;

            //基线
            //        b
            //    a-------
            //        c
            //
            int left = rootView.getRight() + mDx;
            int top = rootView.getTop() + rootView.getMeasuredHeight() / 2;

            int right = 0;
            int bottom = 0;

            if (size == 0) {
                return;
            } else if (size == 1) {
                NodeView midChildNodeView = findTreeNodeView(childNodes.get(0));

                top = top - midChildNodeView.getMeasuredHeight() / 2;
                right = left + midChildNodeView.getMeasuredWidth();
                bottom = top + midChildNodeView.getMeasuredHeight();
                midChildNodeView.layout(left, top, right, bottom);
            } else {

                int topLeft = left;
                int topTop = top;
                int topRight = 0;
                int topBottom = 0;

                int bottomLeft = left;
                int bottomTop = top;
                int bottomRight = 0;
                int bottomBottom = 0;

                if (r == 0) {//偶数
                    for (int i = mid - 1; i >= 0; i--) {
                        NodeView topView = findTreeNodeView(childNodes.get(i));
                        NodeView bottomView = findTreeNodeView(childNodes.get(size - i - 1));


                        if (i == mid - 1) {
                            topTop = topTop - mDy / 2 - topView.getMeasuredHeight();
                            topRight = topLeft + topView.getMeasuredWidth();
                            topBottom = topTop + topView.getMeasuredHeight();

                            bottomTop = bottomTop + mDy / 2;
                            bottomRight = bottomLeft + bottomView.getMeasuredWidth();
                            bottomBottom = bottomTop + bottomView.getMeasuredHeight();
                        } else {
                            topTop = topTop - mDy - topView.getMeasuredHeight();
                            topRight = topLeft + topView.getMeasuredWidth();
                            topBottom = topTop + topView.getMeasuredHeight();

                            bottomTop = bottomTop + mDy;
                            bottomRight = bottomLeft + bottomView.getMeasuredWidth();
                            bottomBottom = bottomTop + bottomView.getMeasuredHeight();
                        }

                        topView.layout(topLeft, topTop, topRight, topBottom);
                        bottomView.layout(bottomLeft, bottomTop, bottomRight, bottomBottom);

                        bottomTop = bottomView.getBottom();
                    }

                } else {
                    NodeView midView = findTreeNodeView(childNodes.get(mid));
                    midView.layout(left, top - midView.getMeasuredHeight() / 2, left + midView.getMeasuredWidth(),
                            top - midView.getMeasuredHeight() / 2 + midView.getMeasuredHeight());

                    topTop = midView.getTop();
                    bottomTop = midView.getBottom();

                    for (int i = mid - 1; i >= 0; i--) {
                        NodeView topView = findTreeNodeView(childNodes.get(i));
                        NodeView bottomView = findTreeNodeView(childNodes.get(size - i - 1));

                        topTop = topTop - mDy - topView.getMeasuredHeight();
                        topRight = topLeft + topView.getMeasuredWidth();
                        topBottom = topTop + topView.getMeasuredHeight();

                        bottomTop = bottomTop + mDy;
                        bottomRight = bottomLeft + bottomView.getMeasuredWidth();
                        bottomBottom = bottomTop + bottomView.getMeasuredHeight();

                        topView.layout(topLeft, topTop, topRight, topBottom);
                        bottomView.layout(bottomLeft, bottomTop, bottomRight, bottomBottom);
                        bottomTop = bottomView.getBottom();
                    }
                }
            }
        }

    }

    /**
     * 移动
     *
     * @param rootView
     * @param dy
     */
    private void moveNodeLayout(NodeView rootView, int dy) {

        Deque<TreeNode<String>> queue = new ArrayDeque<>();
        TreeNode<String> rootNode = rootView.getTreeNode();
        queue.add(rootNode);
        while (!queue.isEmpty()) {
            rootNode = (TreeNode<String>) queue.poll();
            rootView = findTreeNodeView(rootNode);
            int l = rootView.getLeft();
            int t = rootView.getTop() + dy;
            rootView.layout(l, t, l + rootView.getMeasuredWidth(), t + rootView.getMeasuredHeight());

            LinkedList<TreeNode<String>> childNodes = rootNode.getChildNodes();
            for (TreeNode<String> item : childNodes) {
                queue.add(item);
            }
        }
    }

    private void fatherChildCorrect(NodeView nv) {
        int count = nv.getTreeNode().getChildNodes().size();

        if (nv.getParent() != null && count >= 2) {
            TreeNode<String> tn = nv.getTreeNode().getChildNodes().get(0);
            TreeNode<String> bn = nv.getTreeNode().getChildNodes().get(count - 1);
            Log.i("see fc", nv.getTreeNode().getValue() + ":" + tn.getValue() + "," + bn.getValue());

            int topDr = nv.getTop() - findTreeNodeView(tn).getBottom()+mDy;
            int bnDr = findTreeNodeView(bn).getTop() - nv.getBottom()+mDy;
            //上移动
            ArrayList<TreeNode<String>> allLowNodes = mTree.getAllLowNodes(bn);
            ArrayList<TreeNode<String>> allPreNodes = mTree.getAllPreNodes(tn);

            for (TreeNode<String> low : allLowNodes) {
                NodeView view = findTreeNodeView(low);
                moveNodeLayout(view, bnDr);
            }

            for (TreeNode<String> pre : allPreNodes) {
                NodeView view = findTreeNodeView(pre);
                moveNodeLayout(view, -topDr);
            }
        }
    }


    /**
     * 绘制树形的连线
     *
     * @param canvas
     * @param root
     */
    private void drawTreeLine(Canvas canvas, TreeNode<String> root) {
        NodeView fatherView = findTreeNodeView(root);
        if (fatherView != null) {
            LinkedList<TreeNode<String>> childNodes = root.getChildNodes();
            for (TreeNode<String> node : childNodes) {
                drawLineToView(canvas, fatherView, findTreeNodeView(node));
                drawTreeLine(canvas, node);
            }
        }
    }

    /**
     * 绘制两个View直接的连线
     *
     * @param canvas
     * @param from
     * @param to
     */
    private void drawLineToView(Canvas canvas, View from, View to) {

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        float width = 2f;
//        if (from instanceof NodeView) {
//            NodeView fromNodeView = (NodeView) from;
//            double pow = Math.pow(-0.5, fromNodeView.getTreeNode().getFloor() + 1);
//            width = (float) (width - width * pow);
//        }
//        if (width < 0.5f) {
//            width = 0.5f;
//        }

        paint.setStrokeWidth(dp2px(mContext, width));
        paint.setColor(mContext.getResources().getColor(R.color.chelsea_cucumber));

        int top = from.getTop();
        int formY = top + from.getMeasuredHeight() / 2;
        int formX = from.getRight();

        int top1 = to.getTop();
        int toY = top1 + to.getMeasuredHeight() / 2;
        int toX = to.getLeft();

        Path path = new Path();
        path.moveTo(formX, formY);
        path.quadTo(toX - dp2px(mContext, 15), toY, toX, toY);

        canvas.drawPath(path, paint);
    }

    private NodeView findTreeNodeView(TreeNode<String> node) {
        NodeView v = null;
        for (NodeView view : mNodesViews) {
            if (view.getTreeNode() == node) {
                v = view;
                continue;
            }
        }
        return v;
    }

    public int dp2px(Context context, float dpVal) {
        int result = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources()
                .getDisplayMetrics());
        return result;
    }

    public void setTree(Tree<String> tree) {
        this.mTree = tree;
        onAddNodeViews();
    }

    public Tree<String> getTree() {
        return mTree;
    }
}
