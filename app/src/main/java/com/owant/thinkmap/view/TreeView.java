package com.owant.thinkmap.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.owant.thinkmap.R;
import com.owant.thinkmap.control.MoveAndScaleHandler;
import com.owant.thinkmap.line.EaseCubicInterpolator;
import com.owant.thinkmap.model.NodeModel;
import com.owant.thinkmap.model.TreeModel;
import com.owant.thinkmap.util.DensityUtils;
import com.owant.thinkmap.util.LooperFlag;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

import static com.owant.thinkmap.util.DensityUtils.dp2px;

/**
 * Created by owant on 07/03/2017.
 */

public class TreeView extends ViewGroup implements ScaleGestureDetector.OnScaleGestureListener {

    private static final String TAG = "TreeView";

    private Context mContext;

    //树形结构
    public TreeModel<String> mTreeModel;
    private TreeLayoutManager mTreeLayoutManager;

    //移动控制
    private MoveAndScaleHandler mMoveAndScaleHandler;

    //点击事件
    private TreeViewItemClick mTreeViewItemClick;
    //长按
    private TreeViewItemLongClick mTreeViewItemLongClick;

    //最近点击的item
    private NodeModel<String> mCurrentFocus;

    private int mWidth;
    private int mHeight;

    //触摸循环事件，放大，等同，缩小
    private Integer[] looperBody = new Integer[]{0, 1, 0, -1};
    private LooperFlag<Integer> mLooperFlag;


    private GestureDetector mGestureDetector;

    private Paint mPaint;
    private Path mPath;

    public TreeView(Context context) {
        this(context, null, 0);
    }

    public TreeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TreeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setClipChildren(false);
        setClipToPadding(false);


        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mPath = new Path();
        mPath.reset();

        mMoveAndScaleHandler = new MoveAndScaleHandler(context, this);
        mContext = context;

        mLooperFlag = new LooperFlag<>(looperBody, new LooperFlag.LooperListener<Integer>() {
            @Override
            public void onLooper(Integer item) {
                looperBusiness(item);
            }
        });

        mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                mLooperFlag.next();
                return true;
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int size = getChildCount();
        for (int i = 0; i < size; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        if (mTreeLayoutManager != null && mTreeModel != null) {
            //树形结构的分布
            mTreeLayoutManager.onTreeLayout(this);
            ViewBox viewBox = mTreeLayoutManager.onTreeLayoutCallBack();
            setMeasuredDimension(viewBox.right+Math.abs(viewBox.left),viewBox.bottom+Math.abs(viewBox.top));

            boxCallBackChange();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            childAt.layout(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    public void looperBusiness(Integer item) {
        EaseCubicInterpolator easeCubicInterpolator = new EaseCubicInterpolator(0.39f, 0.13f, 0.33f, 1f);
        ObjectAnimator animator1;
        ObjectAnimator animator2;

        if (item == -1) {

            animator1 = ObjectAnimator.ofFloat(TreeView.this, "scaleX", getScaleX(), 0.3f)
                    .setDuration(500);
            animator2 = ObjectAnimator.ofFloat(TreeView.this, "scaleY", getScaleX(), 0.3f)
                    .setDuration(500);

        } else if (item == 0) {
            animator1 = ObjectAnimator.ofFloat(TreeView.this, "scaleX", getScaleX(), 1.0f)
                    .setDuration(500);
            animator2 = ObjectAnimator.ofFloat(TreeView.this, "scaleY", getScaleX(), 1.0f)
                    .setDuration(500);
        } else {
            animator1 = ObjectAnimator.ofFloat(TreeView.this, "scaleX", getScaleX(), 1.6f)
                    .setDuration(500);
            animator2 = ObjectAnimator.ofFloat(TreeView.this, "scaleY", getScaleX(), 1.6f)
                    .setDuration(500);
        }

        animator1.setInterpolator(easeCubicInterpolator);
        animator2.setInterpolator(easeCubicInterpolator);
        animator1.start();
        animator2.start();
    }

    private void boxCallBackChange() {
        int dy = DensityUtils.dp2px(getContext().getApplicationContext(), 20);
        int moreWidth = DensityUtils.dp2px(getContext().getApplicationContext(), 200);

        ViewBox viewBox = mTreeLayoutManager.onTreeLayoutCallBack();
        ViewBox box = viewBox;
        Log.i(TAG,"box="+ box.toString());

        int w = box.right + dy;
        int h = box.bottom +Math.abs(box.top);
        Log.i(TAG, "beLayout: " + getMeasuredWidth() + "," + getMeasuredHeight());

        //重置View的大小
        LayoutParams layoutParams = this.getLayoutParams();
        layoutParams.height = h > getMeasuredHeight() ? h + moreWidth : getMeasuredHeight();
        layoutParams.width = w > getMeasuredWidth() ? w + moreWidth : getMeasuredWidth();
        this.setLayoutParams(layoutParams);
        Log.i(TAG, "onLayout: " + w + "," + h);

        //移动节点
        NodeModel<String> rootNode = getTreeModel().getRootNode();
        if (rootNode != null) {
            moveNodeLayout(this, (NodeView) findNodeViewFromNodeModel(rootNode), Math.abs(box.top));
        }
    }

    /**
     * 移动
     *
     * @param rootView
     * @param dy
     */
    private void moveNodeLayout(TreeView superTreeView, NodeView rootView, int dy) {

        if (dy == 0) {
            return;
        }

        Deque<NodeModel<String>> queue = new ArrayDeque<>();
        NodeModel<String> rootNode = rootView.getTreeNode();
        queue.add(rootNode);
        while (!queue.isEmpty()) {
            rootNode = queue.poll();
            rootView = (NodeView) superTreeView.findNodeViewFromNodeModel(rootNode);
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
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (mTreeModel != null) {
            drawTreeLine(canvas, mTreeModel.getRootNode());
        }

    }

    /**
     * 绘制树形的连线
     *
     * @param canvas
     * @param root
     */
    private void drawTreeLine(Canvas canvas, NodeModel<String> root) {
        NodeView fatherView = (NodeView) findNodeViewFromNodeModel(root);
        if (fatherView != null) {
            LinkedList<NodeModel<String>> childNodes = root.getChildNodes();
            for (NodeModel<String> node : childNodes) {

                //连线
                drawLineToView(canvas, fatherView, findNodeViewFromNodeModel(node));

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

        mPaint.setStyle(Paint.Style.STROKE);

        float width = 2f;

        mPaint.setStrokeWidth(dp2px(mContext, width));
        mPaint.setColor(mContext.getResources().getColor(R.color.chelsea_cucumber));

        int top = from.getTop();
        int formY = top + from.getMeasuredHeight() / 2;
        int formX = from.getRight();

        int top1 = to.getTop();
        int toY = top1 + to.getMeasuredHeight() / 2;
        int toX = to.getLeft();

        mPath.reset();
        mPath.moveTo(formX, formY);
        mPath.quadTo(toX - dp2px(mContext, 15), toY, toX, toY);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return mMoveAndScaleHandler.onTouchEvent(event);
    }

    public TreeModel<String> getTreeModel() {
        return mTreeModel;
    }

    public void setTreeModel(TreeModel<String> treeModel) {
        mTreeModel = treeModel;

        clearAllNoteViews();

        addNoteViews();

        setCurrentSelectedNode(mTreeModel.getRootNode());
    }

    /**
     * 中点对焦
     */
    public void focusMidLocation() {
        if (mTreeModel != null) {

            //计算屏幕中点
            WindowManager systemService = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display defaultDisplay = systemService.getDefaultDisplay();
            int displayH = defaultDisplay.getHeight();
            int focusX = DensityUtils.dp2px(mContext, 20);
            int focusY = displayH / 2;

            //回到原点(0,0)
            ViewHelper.setTranslationX(this, 0);
            ViewHelper.setTranslationY(this, 0);

            View view = findNodeViewFromNodeModel(mTreeModel.getRootNode());
            //回到原点后的中点
            int pointY = (int) view.getY() + view.getMeasuredHeight() / 2;
            if (pointY >= focusY) {
                pointY = -(pointY - focusY);
            } else {
                pointY = focusY - pointY;
            }
            ViewHelper.setTranslationY(this, pointY);
        }
    }

    /**
     * 清除所有的NoteView
     */
    private void clearAllNoteViews() {
        int count = getChildCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                View childView = getChildAt(i);
                if (childView instanceof NodeView) {
                    removeView(childView);
                }
            }
        }
    }

    /**
     * 添加所有的NoteView
     */
    private void addNoteViews() {
        if (mTreeModel != null) {
            NodeModel<String> rootNode = mTreeModel.getRootNode();
            Deque<NodeModel<String>> deque = new ArrayDeque<>();
            deque.add(rootNode);
            while (!deque.isEmpty()) {
                NodeModel<String> poll = deque.poll();

                addNodeViewToGroup(poll);

                LinkedList<NodeModel<String>> childNodes = poll.getChildNodes();
                for (NodeModel<String> ch : childNodes) {
                    deque.push(ch);
                }
            }
        }
    }

    private View addNodeViewToGroup(NodeModel<String> poll) {
        final NodeView nodeView = new NodeView(mContext);
        nodeView.setFocusable(true);
        nodeView.setClickable(true);
        nodeView.setSelected(false);

        nodeView.setTreeNode(poll);

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        nodeView.setLayoutParams(lp);
        //set the nodeclick
        nodeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                performTreeItemClick(view);
            }
        });
        nodeView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                preformTreeItemLongClick(v);
                return true;
            }
        });

        this.addView(nodeView);
        return nodeView;
    }

    public void setTreeViewItemClick(TreeViewItemClick treeViewItemClick) {
        mTreeViewItemClick = treeViewItemClick;
    }

    public void setTreeViewItemLongClick(TreeViewItemLongClick treeViewItemLongClick) {
        mTreeViewItemLongClick = treeViewItemLongClick;
    }

    private void preformTreeItemLongClick(View v) {
        setCurrentSelectedNode(((NodeView) v).getTreeNode());

        if (mTreeViewItemLongClick != null) {
            mTreeViewItemLongClick.onLongClick(v);
        }
    }

    private void performTreeItemClick(View view) {
        setCurrentSelectedNode(((NodeView) view).getTreeNode());
        if (mTreeViewItemClick != null) {
            mTreeViewItemClick.onItemClick(view);
        }
    }

    public void setCurrentSelectedNode(NodeModel<String> nodeModel) {
        if (mCurrentFocus != null) {
            mCurrentFocus.setFocus(false);
            NodeView treeNodeView = (NodeView) findNodeViewFromNodeModel(mCurrentFocus);
            if (treeNodeView != null) {
                treeNodeView.setSelected(false);
            }
        }

        nodeModel.setFocus(true);
        findNodeViewFromNodeModel(nodeModel).setSelected(true);
        mCurrentFocus = nodeModel;
    }

    /**
     * 设置树形结构分布管理器
     *
     * @param treeLayoutManager
     */
    public void setTreeLayoutManager(TreeLayoutManager treeLayoutManager) {
        mTreeLayoutManager = treeLayoutManager;
    }

    /**
     * 模型查找NodeView
     *
     * @param nodeModel
     * @return
     */
    public View findNodeViewFromNodeModel(NodeModel<String> nodeModel) {
        View view = null;
        int size = getChildCount();
        for (int i = 0; i < size; i++) {
            View childView = getChildAt(i);
            if (childView instanceof NodeView) {
                NodeModel<String> treeNode = ((NodeView) childView).getTreeNode();
                if (treeNode == nodeModel) {
                    view = childView;
                    continue;
                }
            }
        }
        return view;
    }

    public void changeNodeValue(NodeModel<String> model, String value) {
        NodeView treeNodeView = (NodeView) findNodeViewFromNodeModel(model);
        NodeModel<String> treeNode = treeNodeView.getTreeNode();
        treeNode.setValue(value);
        treeNodeView.setTreeNode(treeNode);
    }

    public NodeModel<String> getCurrentFocusNode() {
        return mCurrentFocus;
    }

    /**
     * 添加同层节点
     *
     * @param nodeValue
     */
    public void addNode(String nodeValue) {
        NodeModel<String> addNode = new NodeModel<>(nodeValue);
        NodeModel<String> parentNode = getCurrentFocusNode().getParentNode();
        if (parentNode != null) {
            mTreeModel.addNode(parentNode, addNode);
            Log.i(TAG, "addNode: true");
            addNodeViewToGroup(addNode);


        }
    }

    /**
     * 添加子节点
     *
     * @param nodeValue
     */
    public void addSubNode(String nodeValue) {
        NodeModel<String> addNode = new NodeModel<>(nodeValue);
        mTreeModel.addNode(getCurrentFocusNode(), addNode);
        addNodeViewToGroup(addNode);
    }

    public void deleteNode(NodeModel<String> node) {

        //设置current的选择
        setCurrentSelectedNode(node.getParentNode());

        NodeModel<String> parentNode = node.getParentNode();
        if (parentNode != null) {
            //切断
            mTreeModel.removeNode(parentNode, node);
        }

        //清理碎片
        Queue<NodeModel<String>> queue = new ArrayDeque<>();
        queue.add(node);

        while (!queue.isEmpty()) {
            NodeModel<String> poll = queue.poll();
            NodeView treeNodeView = (NodeView) findNodeViewFromNodeModel(poll);
            removeView(treeNodeView);
            for (NodeModel<String> nm : poll.getChildNodes()) {
                queue.add(nm);
            }
        }
    }

    public TreeLayoutManager getTreeLayoutManager() {
        return mTreeLayoutManager;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scaleFactor = detector.getScaleFactor();
        setScaleX(scaleFactor);
        setScaleY(scaleFactor);
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }


}
