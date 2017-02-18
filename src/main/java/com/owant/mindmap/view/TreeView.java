package com.owant.mindmap.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.owant.mindmap.R;
import com.owant.mindmap.control.MoveHandler;
import com.owant.mindmap.model.NodeModel;
import com.owant.mindmap.model.TreeModel;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

import static com.owant.mindmap.until.DensityUtils.dp2px;

/**
 * Created by owant on 09/02/2017.
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
    private TreeModel<String> mTree;
    private ArrayList<NodeView> mNodesViews;

    private MoveHandler mMoveHandler;
    private TreeViewItemClick mTreeViewItemClick;

    public TreeView(Context context) {
        this(context, null, 0);
    }

    public TreeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TreeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        mNodesViews = new ArrayList<>();
        mContext = context;

        setClipChildren(false);
        setClipToPadding(false);

        mDx = dp2px(mContext, 26);
        mDy = dp2px(mContext, 22);

        mMoveHandler = new MoveHandler(this);
    }

    public TreeModel<String> getTreeModel() {
        return mTree;
    }

    public void setTreeModel(TreeModel<String> mTree) {
        this.mTree = mTree;

        //进行添加节点
        onAddNodeViews();
    }

    /**
     * 添加view到Group
     */
    private void onAddNodeViews() {
        if (mTree != null) {
            NodeModel<String> rootNode = mTree.getRootNode();
            Deque<NodeModel<String>> deque = new ArrayDeque<>();
            deque.add(rootNode);
            while (!deque.isEmpty()) {
                NodeModel<String> poll = deque.poll();
                final NodeView nodeView = new NodeView(mContext);
                nodeView.setTreeNode(poll);
                LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                nodeView.setLayoutParams(lp);

                this.addView(nodeView);

                //set the nodeclick
                nodeView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        performTreeItemClick(view);
                    }
                });

                mNodesViews.add(nodeView);

                LinkedList<NodeModel<String>> childNodes = poll.getChildNodes();
                for (NodeModel<String> ch : childNodes) {
                    deque.push(ch);
                }
            }
        }
    }

    /**
     * 点击实现
     *
     * @param view
     */
    private void performTreeItemClick(View view) {
        if (mTreeViewItemClick != null) {
            mTreeViewItemClick.onItemClick(view);
        }
    }

    /**
     * 测量自身和子控件的大小
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
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
            NodeView rootView = findTreeNodeView(mTree.getRootNode());
            if (rootView != null) {
                //root的位置
                rootTreeViewLayout(rootView);
                //标准位置
                for (NodeView nv : mNodesViews) {
                    standardTreeChildLayout(nv);
                }

                //基于父子的移动
                for (NodeView nv : mNodesViews) {
                    fatherChildCorrect(nv);
                }

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
        int tr = mHeight / 2 - rootView.getMeasuredHeight() / 2;
        int rr = lr + rootView.getMeasuredWidth();
        int br = tr + rootView.getMeasuredHeight();
        rootView.layout(lr, tr, rr, br);
    }

    /**
     * 标准的位置分布
     *
     * @param rootView
     */
    private void standardTreeChildLayout(NodeView rootView) {
        NodeModel<String> treeNode = rootView.getTreeNode();
        if (treeNode != null) {
            //所有的子节点
            LinkedList<NodeModel<String>> childNodes = treeNode.getChildNodes();
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

    private void fatherChildCorrect(NodeView nv) {
        int count = nv.getTreeNode().getChildNodes().size();

        if (nv.getParent() != null && count >= 2) {
            NodeModel<String> tn = nv.getTreeNode().getChildNodes().get(0);
            NodeModel<String> bn = nv.getTreeNode().getChildNodes().get(count - 1);
            Log.i("see fc", nv.getTreeNode().getValue() + ":" + tn.getValue() + "," + bn.getValue());

            int topDr = nv.getTop() - findTreeNodeView(tn).getBottom() + mDy;
            int bnDr = findTreeNodeView(bn).getTop() - nv.getBottom() + mDy;
            //上移动
            ArrayList<NodeModel<String>> allLowNodes = mTree.getAllLowNodes(bn);
            ArrayList<NodeModel<String>> allPreNodes = mTree.getAllPreNodes(tn);

            for (NodeModel<String> low : allLowNodes) {
                NodeView view = findTreeNodeView(low);
                moveNodeLayout(view, bnDr);
            }

            for (NodeModel<String> pre : allPreNodes) {
                NodeView view = findTreeNodeView(pre);
                moveNodeLayout(view, -topDr);
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

        Deque<NodeModel<String>> queue = new ArrayDeque<>();
        NodeModel<String> rootNode = rootView.getTreeNode();
        queue.add(rootNode);
        while (!queue.isEmpty()) {
            rootNode = (NodeModel<String>) queue.poll();
            rootView = findTreeNodeView(rootNode);
            int l = rootView.getLeft();
            int t = rootView.getTop() + dy;
            rootView.layout(l, t, l + rootView.getMeasuredWidth(), t + rootView.getMeasuredHeight());

            LinkedList<NodeModel<String>> childNodes = rootNode.getChildNodes();
            for (NodeModel<String> item : childNodes) {
                queue.add(item);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mMoveHandler.move(event);
    }

    private NodeView findTreeNodeView(NodeModel<String> node) {
        NodeView v = null;
        for (NodeView view : mNodesViews) {
            if (view.getTreeNode() == node) {
                v = view;
                continue;
            }
        }
        return v;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mTree != null) {
            drawTreeLine(canvas, mTree.getRootNode());
        }
        super.dispatchDraw(canvas);
    }

    /**
     * 绘制树形的连线
     *
     * @param canvas
     * @param root
     */
    private void drawTreeLine(Canvas canvas, NodeModel<String> root) {
        NodeView fatherView = findTreeNodeView(root);
        if (fatherView != null) {
            LinkedList<NodeModel<String>> childNodes = root.getChildNodes();
            for (NodeModel<String> node : childNodes) {

                //连线
                drawLineToView(canvas, fatherView, findTreeNodeView(node));
                //递归
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

        if (to.getVisibility() == GONE) {
            return;
        }

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        float width = 2f;

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

    public TreeViewItemClick getTreeViewItemClick() {
        return mTreeViewItemClick;
    }

    public void setTreeViewItemClick(TreeViewItemClick click) {
        this.mTreeViewItemClick = click;
    }
}
