package com.owant.drawtreeview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.owant.drawtreeview.model.Tree;
import com.owant.drawtreeview.model.TreeNode;
import com.owant.drawtreeview.view.TreeView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TreeNode<String> root = new TreeNode<>("我的项目\n计划");
        TreeNode<String> nodeB = new TreeNode<>("消息");
        TreeNode<String> nodeC = new TreeNode<>("通讯录");
        TreeNode<String> nodeD = new TreeNode<>("空间");

        TreeNode<String> nodeE = new TreeNode<>("未读消息");
        TreeNode<String> nodeF = new TreeNode<>("已读消息");

        TreeNode<String> nodeG = new TreeNode<>("我的好友");
        TreeNode<String> nodeH = new TreeNode<>("同事");
        TreeNode<String> nodeI = new TreeNode<>("家人");

        TreeNode<String> nodeJ = new TreeNode<>("动态");
        TreeNode<String> nodeK = new TreeNode<>("商城");
        TreeNode<String> nodeL = new TreeNode<>("更多");

        TreeNode<String> nodeM = new TreeNode<>("M");
        TreeNode<String> nodeN = new TreeNode<>("N");
        TreeNode<String> nodeO = new TreeNode<>("O");
        TreeNode<String> nodeP = new TreeNode<>("P");
        TreeNode<String> nodeR = new TreeNode<>("R");
        TreeNode<String> nodeS = new TreeNode<>("S");
        TreeNode<String> nodeZ = new TreeNode<>("Z");
        TreeNode<String> nodey = new TreeNode<>("Y");
        TreeNode<String> nodey1 = new TreeNode<>("你好");

        Tree<String> tree = new Tree<>(root);
        tree.addNode(root, nodeB, nodeC, nodeD);
        tree.addNode(nodeB, nodeE, nodeF);
        tree.addNode(nodeC, nodeG, nodeH, nodeI);
        tree.addNode(nodeD, nodeJ, nodeK, nodeL);
        tree.addNode(nodeH, nodeM, nodeR, nodeS, nodeZ);

        TreeView treeView = (TreeView) findViewById(R.id.tree_view);
        treeView.setTree(tree);
    }

}
