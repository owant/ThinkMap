package com.owant.thinkmap.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.owant.thinkmap.R;
import com.owant.thinkmap.control.MoveHandler;
import com.owant.thinkmap.model.NodeModel;
import com.owant.thinkmap.model.TreeModel;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

import static com.owant.thinkmap.util.DensityUtils.dp2px;

/**
 * Created by owant on 07/03/2017.
 */

public class TreeView extends ViewGroup {

    private Context mContext;

    public TreeModel<String> mTreeModel;
    private TreeLayoutManager mTreeLayoutManager;

    //移动控制
    private MoveHandler mMoveHandler;

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
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mTreeLayoutManager != null) {

            //树形结构的分布
            mTreeLayoutManager.onTreeLayout(this);

            //TODO 进行大小的控制
            ViewBox box = mTreeLayoutManager.onTreeLayoutCallBack();
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
                // performTreeItemClick(view);
            }
        });
        nodeView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // preformTreeItemLongClick(v);
                return true;
            }
        });

        this.addView(nodeView);

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
     * 同步模型查找NodeView
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
