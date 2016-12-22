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

public class TreeView extends ViewGroup {

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

    public TreeView(Context context) {
        this(context, null, 0);
    }

    public TreeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TreeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mNodesViews = new ArrayList<>();
        mContext = context;

        mDx = dp2px(mContext, 26);
        mDy = dp2px(mContext, 22);


//        test();
    }


//    TreeNode<String> nodeA = new TreeNode<>("A");
//    TreeNode<String> nodeB = new TreeNode<>("B");
//    TreeNode<String> nodeC = new TreeNode<>("C");
//    TreeNode<String> nodeD = new TreeNode<>("D");
//    TreeNode<String> nodeE = new TreeNode<>("E");
//    TreeNode<String> nodeF = new TreeNode<>("F");
//    TreeNode<String> nodeG = new TreeNode<>("G");
//    TreeNode<String> nodeH = new TreeNode<>("H");
//    TreeNode<String> nodeI = new TreeNode<>("I");
//    TreeNode<String> nodeJ = new TreeNode<>("J");
//    TreeNode<String> nodeK = new TreeNode<>("K");
//    TreeNode<String> nodeL = new TreeNode<>("L");
//    TreeNode<String> nodeM = new TreeNode<>("M");
//    TreeNode<String> nodeN = new TreeNode<>("N");
//    TreeNode<String> nodeO = new TreeNode<>("O");
//    TreeNode<String> nodeP = new TreeNode<>("P");
//    TreeNode<String> nodeQ = new TreeNode<>("Q");
//    TreeNode<String> nodeR = new TreeNode<>("R");
//    TreeNode<String> nodeS = new TreeNode<>("S");
//    TreeNode<String> nodeT = new TreeNode<>("T");
//    TreeNode<String> nodeU = new TreeNode<>("U");
//    TreeNode<String> nodeV = new TreeNode<>("V");
//    TreeNode<String> nodeW = new TreeNode<>("W");
//    TreeNode<String> nodeX = new TreeNode<>("X");
//    TreeNode<String> nodeY = new TreeNode<>("Y");
//
//    private void test() {
//        Tree<String> tree = new Tree<>(nodeA);
//        tree.addNode(nodeA, nodeC, nodeD, nodeJ);
//        tree.addNode(nodeC, nodeO, nodeP, nodeQ);
//        tree.addNode(nodeD, nodeE, nodeF, nodeG, nodeH);
//        tree.addNode(nodeE, nodeY, nodeX, nodeW, nodeR, nodeT);
//        tree.addNode(nodeG, nodeK, nodeL, nodeM, nodeN);
//        this.setTree(tree);
//    }

    public void setTree(Tree<String> tree) {
        this.mTree = tree;
        this.mNodesViews.clear();

        onAddNodeViews();
        invalidate();
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

        if (mTree != null) {
            onNodeViewsLayout();
        }
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

    /**
     * 进行NodeView的位置计算
     */
    private void onNodeViewsLayout() {

        //基本分布
        nodeLayout(mTree.getRootNode());

        //基于父子纠正
        for (NodeView nv : mNodesViews) {
            fatherChildCorrect(nv);
        }

        //基于同层纠正
        Deque<TreeNode<String>> queue = new ArrayDeque<>();
        TreeNode<String> rootNode2 = mTree.getRootNode();
        queue.add(rootNode2);
        TreeNode<String> temp = null;
        while (!queue.isEmpty()) {
            rootNode2 = (TreeNode<String>) queue.poll();
            if (temp != null) {
                if (temp.getFloor() == rootNode2.getFloor() &&
                        temp.getParentNode() != null &&
                        temp.getParentNode() != rootNode2.getParentNode()) {

                    Log.i("samefloorunsameparent", temp.getValue() + "," + rootNode2.getValue());
                    correctInSameFloorTB(temp, rootNode2);
                }
            }

            temp = rootNode2;
            LinkedList<TreeNode<String>> childNodes = rootNode2.getChildNodes();
            if (childNodes.size() > 0) {
                for (TreeNode<String> item : childNodes) {
                    queue.add(item);
                }
            }
        }


    }

    private void fatherChildCorrect(NodeView nv) {
        int count = nv.getTreeNode().getChildNodes().size();
        if (nv.getParent() != null && count >= 2) {
            TreeNode<String> tn = nv.getTreeNode().getChildNodes().get(0);
            TreeNode<String> bn = nv.getTreeNode().getChildNodes().get(count - 1);

            Log.i("see fc", nv.getTreeNode().getValue() + ":" + tn.getValue() + "," + bn.getValue());
            correctInRootToChildTB(
                    nv.getTreeNode(),
                    tn,
                    bn
            );
        }
    }

    /**
     * 基本位置分布
     */
    private void nodeLayout(TreeNode<String> rootNode) {
        NodeView rootView = findTreeNodeView(rootNode);

        int fatherWidth = rootView.getMeasuredWidth();
        int fatherHeight = rootView.getMeasuredHeight();

        TreeNode<String> treeNode = rootView.getTreeNode();

        if (treeNode.getParentNode() == null) {
            int top = mHeight / 2 - fatherHeight / 2;
            rootView.layout(getPaddingLeft() + mDy, top,
                    getPaddingLeft() + mDy + fatherWidth, top + fatherHeight);
        }

        //获得子叶子
        LinkedList<TreeNode<String>> childNodes = treeNode.getChildNodes();

        int size = treeNode.getChildNodes().size();
        //获取中间item
        int mid = size / 2;
        //获取中间item//奇数偶数
        int r = size % 2;

        if (size == 0) {
            return;
        } else if (size == 1) {

            TreeNode<String> midTreeNode = childNodes.get(0);
            NodeView midView = findTreeNodeView(midTreeNode);

            int starY = rootView.getTop() + rootView.getMeasuredHeight() / 2;
            starY = starY - midView.getMeasuredHeight() / 2;

            int startX = rootView.getLeft() + fatherWidth + mDx;
            midView.layout(startX, starY, startX + midView.getMeasuredWidth(), starY + midView.getMeasuredHeight());

            nodeLayout(midTreeNode);

        } else {

            //基线
            int startY = rootView.getTop() + rootView.getMeasuredHeight() / 2;
            int startX = rootView.getLeft() + fatherWidth + mDx;

            int dxTopStartY = startY;
            int dxBottomStartY = startY;

            //梯度减少
            double pow = Math.pow(0.5, rootNode.getFloor());
            Log.i("pow", pow + "");

            if (r != 0) {
                //奇数
                //layout中间的
                TreeNode<String> midTreeNode = childNodes.get(mid);
                NodeView midView = findTreeNodeView(midTreeNode);
                Log.i("view", midView + "");
                startY = startY - midView.getMeasuredHeight() / 2;
                midView.layout(startX, startY, startX + midView.getMeasuredWidth(), startY + midView.getMeasuredHeight());
                dxTopStartY = midView.getTop();
                dxBottomStartY = midView.getTop() + midView.getMeasuredHeight();
                nodeLayout(midTreeNode);
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
                dxTopStartY = (int) (dxTopStartY - mDy * pow - topView.getMeasuredHeight());
                topView.layout(startX, dxTopStartY, startX + topView.getMeasuredWidth(), dxTopStartY + topView.getMeasuredHeight());
                nodeLayout(topNode);

                //下
                dxBottomStartY = (int) (dxBottomStartY + mDy * pow);
                bottomView.layout(startX, dxBottomStartY, startX + bottomView.getMeasuredWidth(), dxBottomStartY + bottomView.getMeasuredHeight());
                dxBottomStartY = dxBottomStartY + bottomView.getMeasuredHeight();
                nodeLayout(bottomNode);
            }

        }
    }

    /**
     * 基于父节点和子节点的顶层和底层进行纠正
     */
    private void correctInRootToChildTB(TreeNode<String> rootNode, TreeNode<String> topNode, TreeNode<String> bottomNode) {
        TreeNode<String> fTRootNode = null;
        TreeNode<String> fBRootNode = null;

        try {
            fTRootNode = mTree.getPreNode(rootNode);
        } catch (NotFindNodeException e) {
            e.printStackTrace();
        }

        try {
            fBRootNode = mTree.getLowNode(rootNode);
        } catch (NotFindNodeException e) {
            e.printStackTrace();
        }


        int topH = 0;
        int botH = 0;

        NodeView rootView = findTreeNodeView(rootNode);

        if (fTRootNode != null) {
            Log.i("fTNode", fTRootNode.getValue());


            NodeView fTNodeView = findTreeNodeView(fTRootNode);
            NodeView cTopView = findTreeNodeView(topNode);
            int have = rootView.getTop() - fTNodeView.getBottom();
            int should = rootView.getTop() - cTopView.getTop() + mDy / 2;
            topH = should - have;
        }

        if (fBRootNode != null) {
            Log.i("fBNode", fBRootNode.getValue());

            NodeView fBNodeView = findTreeNodeView(fBRootNode);
            NodeView cBotView = findTreeNodeView(bottomNode);


            int have = fBNodeView.getTop() - rootView.getBottom();
            int should = cBotView.getBottom() - rootView.getBottom() + mDy / 2;
            botH = should - have;
        }


        TreeNode<String> parentNode = rootNode.getParentNode();
        if (parentNode != null) {
            LinkedList<TreeNode<String>> childNodes = parentNode.getChildNodes();
            int size = childNodes.size();

            if (size >= 2) {
                int mid = size / 2;
                int index = 0;
                for (int i = 0; i < size; i++) {
                    if (childNodes.get(i) == rootNode) {
                        index = i;
                        Log.i("index", index + ">>>" + rootNode.getValue());
                        continue;
                    }
                }

                Log.i("mid", mid + ">>>" + rootNode.getValue());
                Log.i("topH,botH", topH + ":" + botH);


                if (index == mid) {//在中间

                    midNoteFCTB(rootNode, topH, botH);

                } else if (index < mid) {//在上

                    topFCTB(rootNode, topH, botH, rootView);

                } else {//在下

                    botFCTB(rootNode, topH, botH, rootView);

                }

            }
        }

    }

    /**
     * 父节点在队伍的底部位置
     *
     * @param rootNode
     * @param topH
     * @param botH
     * @param rootView
     */
    private void botFCTB(TreeNode<String> rootNode, int topH, int botH, NodeView rootView) {
        if (topH > 0) {
            //基于上移动
            try {

                moveNodeLayout(rootView, topH);

                TreeNode<String> tempTopRootNode = mTree.getLowNode(rootNode);
                while (tempTopRootNode != null) {
                    moveNodeLayout(findTreeNodeView(tempTopRootNode), topH);
                    tempTopRootNode = mTree.getLowNode(tempTopRootNode);
                }

            } catch (NotFindNodeException e) {
                e.printStackTrace();
            }
        }

        if (botH > 0) {

            try {
                TreeNode<String> tempRootNode = mTree.getLowNode(rootNode);
                while (tempRootNode != null) {
                    moveNodeLayout(findTreeNodeView(tempRootNode), botH);
                    tempRootNode = mTree.getLowNode(tempRootNode);
                }
            } catch (NotFindNodeException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 父节点在队伍的顶部位置
     *
     * @param rootNode
     * @param topH
     * @param botH
     * @param rootView
     */
    private void topFCTB(TreeNode<String> rootNode, int topH, int botH, NodeView rootView) {
        if (botH > 0) {
            //基于下移动
            moveNodeLayout(rootView, -botH);
            try {
                TreeNode<String> tempRootNode = mTree.getPreNode(rootNode);
                while (tempRootNode != null) {
                    moveNodeLayout(findTreeNodeView(tempRootNode), -botH);
                    tempRootNode = mTree.getPreNode(tempRootNode);
                }
            } catch (NotFindNodeException e) {
                e.printStackTrace();
            }
        }


        if (topH > 0) {
            //基于上移动
            try {
                TreeNode<String> tempTopRootNode = mTree.getPreNode(rootNode);
                while (tempTopRootNode != null) {
                    moveNodeLayout(findTreeNodeView(tempTopRootNode), -topH);
                    tempTopRootNode = mTree.getPreNode(tempTopRootNode);
                }

            } catch (NotFindNodeException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 父节点在队伍的中间位置
     *
     * @param rootNode
     * @param topH
     * @param botH
     */
    private void midNoteFCTB(TreeNode<String> rootNode, int topH, int botH) {
        Log.i("enter", "midNoteFCTB");

        if (botH > 0) {
            try {
                TreeNode<String> tempBNode = mTree.getLowNode(rootNode);
                while (tempBNode != null) {
                    moveNodeLayout(findTreeNodeView(tempBNode), botH);
                    tempBNode = mTree.getLowNode(tempBNode);
                }
            } catch (NotFindNodeException e) {
                e.printStackTrace();
            }
        }

        if (topH > 0) {
            try {
                TreeNode<String> tempTNode = mTree.getPreNode(rootNode);
                while (tempTNode != null) {
                    moveNodeLayout(findTreeNodeView(tempTNode), -topH);
                    tempTNode = mTree.getPreNode(tempTNode);
                }
            } catch (NotFindNodeException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 基于同层不同父节点纠正
     */
    private void correctInSameFloorTB(TreeNode<String> topNode, TreeNode<String> bottomNode) {

        NodeView topNodeView = findTreeNodeView(topNode);
        NodeView botNodeView = findTreeNodeView(bottomNode);

        TreeNode<String> tParentNode = topNode.getParentNode();
        TreeNode<String> bParentNode = bottomNode.getParentNode();


        int bottom = topNodeView.getBottom();
        int top = botNodeView.getTop();
        int dx = bottom - top + dp2px(mContext, 2);

        Log.i("should_move", topNodeView.getText() + "," + botNodeView.getText());

        if (dx > 0) {
            //前一个节点有移动的可能
            //后一个节点也有移动的可能；

            //如果节点在父节点的上，前节点向上移动

            //如果在父节点的中间，需要后节点下移动
            //如果在父节点的下，tParentNode
            LinkedList<TreeNode<String>> childNodes = tParentNode.getParentNode().getChildNodes();
            int size = childNodes.size();
            int mid = size / 2;
            int index = 0;
            for (int i = 0; i < size; i++) {
                if (childNodes.get(i) == tParentNode) {
                    index = i;
                    Log.i("...", index + ">>>" + tParentNode.getValue());
                    continue;
                }
            }

            if (index < mid) {
                try {
                    TreeNode<String> temp = tParentNode;
                    while (temp != null) {
                        moveNodeLayout(findTreeNodeView(temp), -dx);
                        temp = mTree.getPreNode(temp);
                    }
                } catch (NotFindNodeException e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    TreeNode<String> temp = bParentNode;
                    while (temp != null) {
                        moveNodeLayout(findTreeNodeView(temp), dx);
                        temp = mTree.getLowNode(temp);
                    }
                } catch (NotFindNodeException e) {
                    e.printStackTrace();
                }
            }
        }


    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mTree != null) {
            drawTreeLine(canvas, mTree.getRootNode());
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

        float width = 2f;
        if (from instanceof NodeView) {
            NodeView fromNodeView = (NodeView) from;
            double pow = Math.pow(-0.5, fromNodeView.getTreeNode().getFloor() + 1);
            width = (float) (width - width * pow);
        }
        if (width < 0.5f) {
            width = 0.5f;
        }

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

    /**
     * dp to px
     */
    public int dp2px(Context context, float dpVal) {
        int result = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources()
                .getDisplayMetrics());
        return result;
    }


}