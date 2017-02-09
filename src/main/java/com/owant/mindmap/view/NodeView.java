package com.owant.mindmap.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.owant.mindmap.R;
import com.owant.mindmap.model.NodeModel;

/**
 * Created by owant on 09/02/2017.
 */
public class NodeView extends TextView {

    public NodeModel<String> treeNode;

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

    public NodeModel<String> getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(NodeModel<String> treeNode) {
        this.treeNode = treeNode;
        setText(treeNode.getValue());
    }

}