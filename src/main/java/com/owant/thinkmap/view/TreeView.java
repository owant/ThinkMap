package com.owant.thinkmap.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.nineoldandroids.view.ViewHelper;
import com.owant.thinkmap.R;
import com.owant.thinkmap.control.MoveHandler;
import com.owant.thinkmap.model.NodeModel;
import com.owant.thinkmap.model.TreeModel;
import com.owant.thinkmap.util.DensityUtils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

import static com.owant.thinkmap.util.DensityUtils.dp2px;

/**
 * Created by owant on 07/03/2017.
 */

public class TreeView extends ViewGroup {

    private static final String TAG = "TreeView";

    private Context mContext;

    public TreeModel<String> mTreeModel;
    private TreeLayoutManager mTreeLayoutManager;

    //移动控制
    private MoveHandler mMoveHandler;

    //点击事件
    private TreeViewItemClick mTreeViewItemClick;
    private TreeViewItemLongClick mTreeViewItemLongClick;
    //最近点击的item
    private NodeModel<String> mCurrentFocus;

    private int mWidth;
    private int mHeight;


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

        mMoveHandler = new MoveHandler(this);
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int size = getChildCount();
        for (int i = 0; i < size; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mTreeLayoutManager != null) {

            //树形结构的分布
            mTreeLayoutManager.onTreeLayout(this);

            ViewBox box = mTreeLayoutManager.onTreeLayoutCallBack();
            Log.i("box", box.toString());

            int w = mWidth > box.right ? mWidth : box.right;
            int temTop = box.top < 0 ? -box.top : 0;
            int temBottom = box.bottom > mHeight ? box.bottom : 0;
            int h = temTop + temBottom;

            //重置View的大小
            this.setLayoutParams(new FrameLayout.LayoutParams(w, h));
            int measuredHeight = getMeasuredHeight();
            int measuredWidth = getMeasuredWidth();
            Log.i(TAG, "onLayout: " + measuredHeight + "," + measuredWidth);

            //移动节点
            moveNodeLayout(this, (NodeView) findNodeViewFromNodeModel(getTreeModel().getRootNode()), temTop);

            focusMidLocation();
        }
    }

    /**
     * 移动
     *
     * @param rootView
     * @param dy
     */
    private void moveNodeLayout(TreeView superTreeView, NodeView rootView, int dy) {

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
        if (mTreeModel != null) {
            drawTreeLine(canvas, mTreeModel.getRootNode());
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mMoveHandler.move(event);
    }

    public TreeModel<String> getTreeModel() {
        return mTreeModel;
    }

    public void setTreeModel(TreeModel<String> treeModel) {
        mTreeModel = treeModel;

        clearAllNoteViews();

        addNoteViews();

        setCurrentSelectedNode(mTreeModel.getRootNode());

        this.post(new Runnable() {
            @Override
            public void run() {
//                focusMidLocation();
            }
        });
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

            //回到原点后的中点
            int pointY = getHeight() / 2;
            int pointX = DensityUtils.dp2px(mContext, 20);

            //移动到中点
            ViewHelper.setTranslationX(this, focusX - pointX);
            ViewHelper.setTranslationY(this, focusY - pointY);
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

    private void addNodeViewToGroup(NodeModel<String> poll) {
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

    }

    public void setTreeViewItemClick(TreeViewItemClick treeViewItemClick) {
        mTreeViewItemClick = treeViewItemClick;
    }

    public void setTreeViewItemLongClick(TreeViewItemLongClick treeViewItemLongClick) {
        mTreeViewItemLongClick = treeViewItemLongClick;
    }

    private void preformTreeItemLongClick(View v) {
        if (mTreeViewItemLongClick != null) {
            mTreeViewItemLongClick.onLongClick(v);
        }
    }

    private void performTreeItemClick(View view) {
        if (mTreeViewItemClick != null) {
            setCurrentSelectedNode(((NodeView) view).getTreeNode());
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
}
