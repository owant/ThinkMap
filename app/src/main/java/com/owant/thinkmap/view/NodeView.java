package com.owant.thinkmap.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.owant.thinkmap.R;
import com.owant.thinkmap.model.NodeModel;
import com.owant.thinkmap.util.DensityUtils;


/**
 * Created by owant on 09/02/2017.
 */
@SuppressLint("AppCompatCustomView")
public class NodeView extends TextView{

    public NodeModel<String> treeNode = null;

    public NodeView(Context context) {
        this(context, null, 0);
    }

    public NodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setTextColor(Color.WHITE);
        setPadding(0, 10, 0, 10);

        Drawable drawable = context.getResources().getDrawable(R.drawable.node_view_bg);
        setBackgroundDrawable(drawable);
    }

    public NodeModel<String> getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(NodeModel<String> treeNode) {
        this.treeNode = treeNode;
        setSelected(treeNode.isFocus());
        setText(treeNode.getValue());
    }

}