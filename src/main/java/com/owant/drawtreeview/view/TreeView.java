package com.owant.drawtreeview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;

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
        mDy = dp2px(mContext, 22);

//        test();

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
                float textSize = (float) (18 - 5 * rootNode.getFloor());
                if (textSize < 9) {
                    textSize = 9;
                }
                nodeView.setTextSize(textSize);

                nodeViews.add(nodeView);
                this.addView(nodeView);
                Log.i("node", nodeView.getText().toString());

                LinkedList<TreeNode<String>> childNodes = rootNode.getChildNodes();
                if (childNodes.size() > 0) {
                    for (TreeNode<String> item : childNodes) {
                        queue.add(item);
                    }
                }
            }
        }
    }

//    TreeNode<String> root = new TreeNode<>("A");
//    TreeNode<String> nodeB = new TreeNode<>("B");
//    TreeNode<String> nodeC = new TreeNode<>("C");
//    TreeNode<String> nodeD = new TreeNode<>("D");
//    TreeNode<String> nodeE = new TreeNode<>("E");
//    TreeNode<String> nodeF = new TreeNode<>("F");
//    TreeNode<String> nodeG = new TreeNode<>("G");
//    TreeNode<String> nodeH = new TreeNode<>("H");
//    TreeNode<String> nodeI = new TreeNode<>("I");
//
//    TreeNode<String> nodeJ = new TreeNode<>("J");
//    TreeNode<String> nodeK = new TreeNode<>("K");
//    TreeNode<String> nodeL = new TreeNode<>("L");
//
//    TreeNode<String> nodeM = new TreeNode<>("M");
//    TreeNode<String> nodeN = new TreeNode<>("N");
//    TreeNode<String> nodeO = new TreeNode<>("O");
//    TreeNode<String> nodeP = new TreeNode<>("P");
//    Tree<String> tree1 = new Tree<>(root);
//
//    public void test() {
//
//        tree1.addNode(root, nodeB, nodeC, nodeD);
//        tree1.addNode(nodeB, nodeE, nodeF);
//
////        tree1.addNode(nodeC, nodeG,nodeH);
//        tree1.addNode(nodeE, nodeJ, nodeK, nodeL);
//
//        tree1.addNode(nodeK, nodeM, nodeN, nodeO, nodeP);
//
//        tree1.addNode(nodeF, nodeG);
//
////        tree1.addNode(nodeD, nodeG);
//
//        this.tree = tree1;
//
//    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mHeight = getMeasuredHeight();
        mWith = getMeasuredWidth();

        if (tree != null) {
            TreeNode<String> rootNode = tree.getRootNode();

            setNoteViewLayout(rootNode);

            //矫正

            for (NodeView v : nodeViews) {
                if (v.getTreeNode().getParentNode() != null && v.getTreeNode().getChildNodes().size() >= 2) {

                    int size = v.getTreeNode().getChildNodes().size();
                    notifyFatherLayout(v.getTreeNode(),
                            findTreeNodeView(v.getTreeNode().getChildNodes().get(0)),
                            findTreeNodeView(v.getTreeNode().getChildNodes().get(size - 1)));
                }
            }

            reLayoutNode();

        }
    }

    private void reLayoutNode() {
        if (tree != null) {
//            TreeNode<String> rootNode = tree.getRootNode();
//            ArrayDeque<TreeNode<String>> queue = new ArrayDeque<>();
//            queue.add(rootNode);
//            while (!queue.isEmpty()) {
//                TreeNode<String> poll = queue.poll();
//
//                LinkedList<TreeNode<String>> childNodes = poll.getChildNodes();
//                for (TreeNode<String> item : childNodes) {
//                    queue.add(item);
//                }
//            }

            int size = nodeViews.size();
            NodeView pre = null;
            for (int i = 0; i < size; i++) {
                //同层不同父亲
                NodeView nodeView = nodeViews.get(i);
                TreeNode<String> treeNode = nodeView.getTreeNode();
                if (pre != null)
                    correctLayout(pre, nodeView);
                pre = nodeView;
            }
        }
    }

    public void correctLayout(NodeView preNodeView, NodeView botNodeView) {
        TreeNode<String> preParentNode = preNodeView.getTreeNode().getParentNode();
        TreeNode<String> botParentNode = botNodeView.getTreeNode().getParentNode();

        if (preNodeView.getTreeNode().getFloor() == botNodeView.getTreeNode().getFloor()
                && preParentNode != null && botParentNode != null
                && preParentNode != botParentNode) {

            int bottom = preNodeView.getBottom();
            int top = botNodeView.getTop();
            int dx = bottom - top + dp2px(mContext, 2);

            Log.i("should move", preNodeView.getText() + "," + botNodeView.getText());

            if (dx > 0) {
                //前一个节点有移动的可能
                //后一个节点也有移动的可能；

                //如果节点在父节点的上，前节点向上移动

                //如果在父节点的中间，需要后节点下移动
                //如果在父节点的下，preParentNode
                LinkedList<TreeNode<String>> childNodes = preParentNode.getParentNode().getChildNodes();
                int size = childNodes.size();
                int mid = size / 2;
                int index = 0;
                for (int i = 0; i < size; i++) {
                    if (childNodes.get(i) == preParentNode) {
                        index = i;
                        Log.i("...", index + ">>>" + preParentNode.getValue());
                        continue;
                    }
                }

                if (index < mid) {
                    moveLayout(findTreeNodeView(preParentNode), -dx);
                } else {
                    moveLayout(findTreeNodeView(botParentNode), dx);
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

            //梯度减少
            double pow = Math.pow(0.5, rootNode.getFloor());
            Log.i("pow", pow + "");

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
                dxTopStartY = (int) (dxTopStartY - mDy * pow - topView.getMeasuredHeight());
                topView.layout(startX, dxTopStartY, startX + topView.getMeasuredWidth(), dxTopStartY + topView.getMeasuredHeight());

                setNoteViewLayout(topNode);

                //下
                dxBottomStartY = (int) (dxBottomStartY + mDy * pow);
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

        int should_dy = 0;
        int upDy = 0;
        int downDy = 0;
        NodeView rootView = findTreeNodeView(fatherNode);
        if (preRootView != null) {
            //先处理上层
            int haveHeightTop = rootView.getTop() - (preRootView.getTop() + preRootView.getMeasuredHeight());
            int shouldHeightTop = rootView.getTop() - (shouldTopView.getTop() - mDy);
            upDy = shouldHeightTop - haveHeightTop;

        }

        if (botRootView != null) {
            //下层
            int haveHeightTop = botRootView.getTop() - (rootView.getTop() + rootView.getMeasuredHeight() + mDy);
            int shouldHeightTop = shouldBottomView.getTop() - rootView.getTop();
            downDy = shouldHeightTop - haveHeightTop;
        }

        //判断是否在中线
        LinkedList<TreeNode<String>> childNodes = fatherNode.getParentNode().getChildNodes();
        int size = childNodes.size();

        if (size >= 2) {

            int mid = size / 2;
            int index = 0;
            for (int i = 0; i < size; i++) {
                if (childNodes.get(i) == fatherNode) {
                    index = i;
                    Log.i("index", index + ">>>" + fatherNode.getValue());
                    continue;
                }
            }

            should_dy = downDy + upDy;

            if (should_dy > 0) {
                if (index == mid) {
                    moveLayout(preRootView, -upDy);
                    moveLayout(botRootView, downDy);
                } else if (index < mid) {
                    moveLayout(rootView, -should_dy);
                } else {
                    moveLayout(rootView, should_dy);
                }

            }

        }
    }

    private void moveLayout(NodeView rootView, int should_dy) {

        Deque<TreeNode<String>> queue = new ArrayDeque<>();
        TreeNode<String> rootNode = rootView.getTreeNode();
        queue.add(rootNode);
        while (!queue.isEmpty()) {

            rootNode = (TreeNode<String>) queue.poll();
            rootView = findTreeNodeView(rootNode);
            int l = rootView.getLeft();
            int t = rootView.getTop() + should_dy;
            rootView.layout(l, t, l + rootView.getMeasuredWidth(), t + rootView.getMeasuredHeight());

            LinkedList<TreeNode<String>> childNodes = rootNode.getChildNodes();
            for (TreeNode<String> item : childNodes) {
                queue.add(item);
            }
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
