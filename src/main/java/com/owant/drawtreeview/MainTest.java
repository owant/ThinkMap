package com.owant.drawtreeview;

import com.owant.drawtreeview.model.NotFindNodeException;
import com.owant.drawtreeview.model.Tree;
import com.owant.drawtreeview.model.TreeNode;

/**
 * Created by owant on 17/12/2016.
 */

public class MainTest {

    public static void main(String[] args) {

        TreeNode<String> root = new TreeNode<>("A");
        Tree<String> tree = new Tree<>(root);

        //nodes
        TreeNode<String> node1 = new TreeNode<>("B");
        TreeNode<String> node2 = new TreeNode<>("C");
        TreeNode<String> node3 = new TreeNode<>("D");
        TreeNode<String> node4 = new TreeNode<>("E");
        TreeNode<String> node5 = new TreeNode<>("F");
        TreeNode<String> node6 = new TreeNode<>("G");
        TreeNode<String> node7 = new TreeNode<>("H");
        TreeNode<String> node8 = new TreeNode<>("I");

        /**
         *                A
         *           C        B
         *     F     E    D
         * G  H  I
         */
        //build tree
        tree.addNode(root, node1, node2);
        tree.addNode(node1, node3, node4, node5);
        tree.addNode(node5, node6, node7, node8);

        tree.printTree();

        System.out.println("\n");

        tree.printTree2();

        System.out.println("\n");

        TreeNode<String> preNode = null;
        try {
            preNode = tree.getPreNode(node6);
            System.out.println("find:" + preNode.getValue());

        } catch (NotFindNodeException e) {
            e.printStackTrace();
        }


//        System.out.println("......");
//        TreeNode<String> lowNode = tree.getLowNode(node6);
//        System.out.println("find:"+lowNode.getValue());
//        System.out.println("......");


    }
}
