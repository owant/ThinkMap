package com.owant.drawtreeview;

import com.owant.drawtreeview.model.Tree;
import com.owant.drawtreeview.model.TreeNode;

/**
 * Created by owant on 17/12/2016.
 */

public class MainTest {

    public static void main(String[] args) {

//        String format = "TreeNode<String> node{0} = new TreeNode<>(\"{0}\");";
//        char a = 'A';
//        for (char i = a; i < 'Z'; i++) {
//            System.out.println(format.replace("{0}", i + ""));
//        }
//
//        TreeNode<String> nodeA = new TreeNode<>("A");
//        TreeNode<String> nodeB = new TreeNode<>("B");
//        TreeNode<String> nodeC = new TreeNode<>("C");
//        TreeNode<String> nodeD = new TreeNode<>("D");
//        TreeNode<String> nodeE = new TreeNode<>("E");
//        TreeNode<String> nodeF = new TreeNode<>("F");
//        TreeNode<String> nodeG = new TreeNode<>("G");
//        TreeNode<String> nodeH = new TreeNode<>("H");
//        TreeNode<String> nodeI = new TreeNode<>("I");
//        TreeNode<String> nodeJ = new TreeNode<>("J");
//        TreeNode<String> nodeK = new TreeNode<>("K");
//        TreeNode<String> nodeL = new TreeNode<>("L");
//        TreeNode<String> nodeM = new TreeNode<>("M");
//        TreeNode<String> nodeN = new TreeNode<>("N");
//        TreeNode<String> nodeO = new TreeNode<>("O");
//        TreeNode<String> nodeP = new TreeNode<>("P");
//        TreeNode<String> nodeQ = new TreeNode<>("Q");
//        TreeNode<String> nodeR = new TreeNode<>("R");
//        TreeNode<String> nodeS = new TreeNode<>("S");
//        TreeNode<String> nodeT = new TreeNode<>("T");
//        TreeNode<String> nodeU = new TreeNode<>("U");
//        TreeNode<String> nodeV = new TreeNode<>("V");
//        TreeNode<String> nodeW = new TreeNode<>("W");
//        TreeNode<String> nodeX = new TreeNode<>("X");
//        TreeNode<String> nodeY = new TreeNode<>("Y");
//
//
//        Tree<String> tree = new Tree<>(nodeA);
//        tree.addNode(nodeA, nodeB, nodeC, nodeD, nodeJ);
//        tree.addNode(nodeC, nodeO, nodeP, nodeQ);
//        tree.addNode(nodeD, nodeE, nodeF, nodeG, nodeH);
//        tree.addNode(nodeG, nodeK, nodeL, nodeM, nodeN);
//
//        synchronized (args) {
//
//            tree.printTree2();
//            System.out.println("-----");
//
//
//            try {
//                TreeNode<String> temp = tree.getPreNode(nodeE);
//                while (temp != null) {
//                    System.out.println("pre:" + temp.getValue());
//                    temp = tree.getPreNode(temp);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }


        for (char i = 'A'; i <= 'Z'; i++) {
            System.out.printf("TreeNode<String>  node%c= new TreeNode<>(\"%c\");\n",i,i);
        }

    }
}
