package com.owant.thinkmap.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.owant.thinkmap.R;
import com.owant.thinkmap.base.BasePresenter;
import com.owant.thinkmap.model.NodeModel;
import com.owant.thinkmap.model.TreeModel;
import com.owant.thinkmap.view.NodeView;
import com.owant.thinkmap.view.TreeView;

/**
 * Created by owant on 10/03/2017.
 */

public class EditThinkMapPresenter extends BasePresenter {

    final static String TAG = "EditThinkMapPresenter";
    final static String save_tree_model_key = "tree_model";

    private TreeModel<String> mTreeModel;

    @Override
    public void onCreated(Context context) {
        super.onCreated(context);
    }

    @Override
    public void onRecycle() {
        super.onRecycle();
    }

    public void initTreeViewModel(TreeView treeView) {
        final NodeModel<String> nodeA = new NodeModel<>("A");
        final NodeModel<String> nodeB = new NodeModel<>("B");
        final NodeModel<String> nodeC = new NodeModel<>("C");
        final NodeModel<String> nodeD = new NodeModel<>("D");
        final NodeModel<String> nodeE = new NodeModel<>("E");
        final NodeModel<String> nodeF = new NodeModel<>("F");
        final NodeModel<String> nodeG = new NodeModel<>("G");
        final NodeModel<String> nodeH = new NodeModel<>("H");
        final NodeModel<String> nodeI = new NodeModel<>("I");
        final NodeModel<String> nodeJ = new NodeModel<>("J");
        final NodeModel<String> nodeK = new NodeModel<>("K");
        final NodeModel<String> nodeL = new NodeModel<>("L");
        final NodeModel<String> nodeM = new NodeModel<>("M");
        final NodeModel<String> nodeN = new NodeModel<>("N");
        final NodeModel<String> nodeO = new NodeModel<>("O");
        final NodeModel<String> nodeP = new NodeModel<>("P");
        final NodeModel<String> nodeQ = new NodeModel<>("Q");
        final NodeModel<String> nodeR = new NodeModel<>("R");
        final NodeModel<String> nodeS = new NodeModel<>("S");
        final NodeModel<String> nodeT = new NodeModel<>("T");
        final NodeModel<String> nodeU = new NodeModel<>("U");
        final NodeModel<String> nodeV = new NodeModel<>("V");
        final NodeModel<String> nodeW = new NodeModel<>("W");
        final NodeModel<String> nodeX = new NodeModel<>("X");
        final NodeModel<String> nodeY = new NodeModel<>("Y");
        final NodeModel<String> nodeZ = new NodeModel<>("Z");


        final TreeModel<String> tree = new TreeModel<>(nodeA);
        tree.addNode(nodeA, nodeB, nodeC, nodeD);
        tree.addNode(nodeC, nodeE, nodeF, nodeG, nodeH, nodeI);
        tree.addNode(nodeB, nodeJ, nodeK, nodeL);
        tree.addNode(nodeD, nodeM, nodeN, nodeO);
        tree.addNode(nodeF, nodeP, nodeQ, nodeR, nodeS);
        tree.addNode(nodeR, nodeT, nodeU, nodeV, nodeW, nodeX);
        tree.addNode(nodeT, nodeY, nodeZ);

//        NodeModel<String> rootNode = new NodeModel<String>(mContext.
//                getString(R.string.defualt_my_plan));
//        mTreeModel = new TreeModel<>(rootNode);
//        treeView.setTreeModel(mTreeModel);
        treeView.setTreeModel(tree);
    }

    public void saveTreeModel(Bundle outState, TreeView editMapTreeView) {
        Log.i(TAG, "saveTreeModel: 保存数据");
        TreeModel<String> treeModel = editMapTreeView.getTreeModel();
        outState.putSerializable(save_tree_model_key, treeModel);
    }

    public void restoreTreeModel(TreeView treeView, Bundle savedInstanceState) {
        try {
            Log.i(TAG, "restoreTreeModel: 恢复数据");
            TreeModel<String> saveTreeModel = (TreeModel<String>) savedInstanceState.getSerializable(save_tree_model_key);
            if (saveTreeModel != null) this.mTreeModel = saveTreeModel;
            treeView.setTreeModel(mTreeModel);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String translationNoteViewValue(View view) {
        String value = "";
        if (view instanceof NodeView) {
            value = ((NodeView) view).getTreeNode().getValue();
        } else {
            value = mContext.getResources().getString(R.string.unkown_error);
        }
        return value;
    }

    public NodeModel<String> getTreeModelFromView(View view) {
        return ((NodeView) view).getTreeNode();
    }

    public boolean currentViewIsRootNote(TreeView view) {
        if (view.getCurrentFocusNode() == view.getTreeModel().getRootNode()) {
            return true;
        } else {
            return false;
        }
    }
}
