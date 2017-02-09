package com.owant.mindmap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.owant.mindmap.model.NodeModel;
import com.owant.mindmap.model.TreeModel;
import com.owant.mindmap.view.TreeView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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


        final TreeView treeView = (TreeView) findViewById(R.id.tree_view);
        final TreeModel<String> tree = new TreeModel<>(nodeA);
        tree.addNode(nodeA, nodeB, nodeC, nodeD);
        tree.addNode(nodeC, nodeE, nodeF, nodeG, nodeH, nodeI);
        tree.addNode(nodeB, nodeJ, nodeK, nodeL);
        tree.addNode(nodeD, nodeM, nodeN, nodeO);
        tree.addNode(nodeF, nodeP, nodeQ, nodeR, nodeS);
        tree.addNode(nodeR, nodeT, nodeU, nodeV, nodeW, nodeX);
        tree.addNode(nodeT, nodeY, nodeZ);
        treeView.setTreeModel(tree);


    }
}