package com.owant.drawtreeview.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.owant.drawtreeview.R;
import com.owant.drawtreeview.model.TreeNode;

/**
 * Created by owant on 17/12/2016.
 */
public class NodeView extends TextView {

    public TreeNode<String> treeNode;

    public NodeView(Context context) {
        this(context, null, 0);
    }

    public NodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setTextColor(Color.WHITE);
        setPadding(12, 10, 12, 10);

        Drawable drawable = context.getResources().getDrawable(R.drawable.node_view_bg);
        setBackgroundDrawable(drawable);
    }

    public TreeNode<String> getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(TreeNode<String> treeNode) {
        this.treeNode = treeNode;
        setText(treeNode.getValue());
    }
}
