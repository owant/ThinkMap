package com.owant.drawtreeview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.owant.drawtreeview.model.Tree;
import com.owant.drawtreeview.model.TreeNode;
import com.owant.drawtreeview.view.TreeView;

public class MainActivity extends AppCompatActivity {

    TreeView treeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        treeView = (TreeView) findViewById(R.id.activity_main);

//        TreeNode<String> root = new TreeNode<>("A");
//        TreeNode<String> nodeB = new TreeNode<>("B");
//        TreeNode<String> nodeC = new TreeNode<>("C");
//        TreeNode<String> nodeD = new TreeNode<>("D");
//        TreeNode<String> nodeE = new TreeNode<>("E");
//        TreeNode<String> nodeF = new TreeNode<>("F");
//        TreeNode<String> nodeG = new TreeNode<>("G");
//        TreeNode<String> nodeH = new TreeNode<>("H");
//        TreeNode<String> nodeI = new TreeNode<>("I");
//
//        TreeNode<String> nodeJ = new TreeNode<>("J");
//        TreeNode<String> nodeK = new TreeNode<>("K");
//        TreeNode<String> nodeL = new TreeNode<>("L");
//
//        TreeNode<String> nodeM = new TreeNode<>("M");
//        TreeNode<String> nodeN = new TreeNode<>("N");
//        TreeNode<String> nodeO = new TreeNode<>("O");
//        TreeNode<String> nodeP = new TreeNode<>("P");


        TreeNode<String> root = new TreeNode<>("我的项目");

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

        Tree<String> tree = new Tree<>(root);

        tree.addNode(root, nodeB, nodeC, nodeD);
        tree.addNode(nodeB, nodeE, nodeF);
//        tree.addNode(nodeC, nodeG,nodeH);
        tree.addNode(nodeD, nodeJ, nodeK, nodeL);
        tree.addNode(nodeK, nodeM, nodeN, nodeO, nodeP);

        tree.addNode(nodeE, nodeG, nodeH, nodeI);
//        tree.addNode(nodeD, nodeG);


        treeView.setTree(tree);
    }

}
