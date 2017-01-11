package com.owant.drawtreeview;

import com.owant.drawtreeview.model.Tree;
import com.owant.drawtreeview.model.TreeNode;

import java.util.ArrayList;

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


//        for (char i = 'A'; i <= 'Z'; i++) {
//            System.out.printf("TreeNode<String>  node%c= new TreeNode<>(\"%c\");\n", i, i);
//        }

//        testDraw();

        testGetAllPreView();
    }

    private static void testGetAllPreView() {
        TreeNode<String> nodeA = new TreeNode<>("A");
        TreeNode<String> nodeB = new TreeNode<>("B");
        TreeNode<String> nodeC = new TreeNode<>("C");
        TreeNode<String> nodeD = new TreeNode<>("D");
        TreeNode<String> nodeE = new TreeNode<>("E");
        TreeNode<String> nodeF = new TreeNode<>("F");
        TreeNode<String> nodeG = new TreeNode<>("G");
        TreeNode<String> nodeH = new TreeNode<>("H");
        TreeNode<String> nodeI = new TreeNode<>("I");
        TreeNode<String> nodeJ = new TreeNode<>("J");
        TreeNode<String> nodeK = new TreeNode<>("K");
        TreeNode<String> nodeL = new TreeNode<>("L");
        TreeNode<String> nodeM = new TreeNode<>("M");
        TreeNode<String> nodeN = new TreeNode<>("N");
        TreeNode<String> nodeO = new TreeNode<>("O");
        TreeNode<String> nodeP = new TreeNode<>("P");
        TreeNode<String> nodeQ = new TreeNode<>("Q");
        TreeNode<String> nodeR = new TreeNode<>("R");
        TreeNode<String> nodeS = new TreeNode<>("S");
        TreeNode<String> nodeT = new TreeNode<>("T");
        TreeNode<String> nodeU = new TreeNode<>("U");
        TreeNode<String> nodeV = new TreeNode<>("V");
        TreeNode<String> nodeW = new TreeNode<>("W");
        TreeNode<String> nodeX = new TreeNode<>("X");
        TreeNode<String> nodeY = new TreeNode<>("Y");
        TreeNode<String> nodeZ = new TreeNode<>("Z");

        Tree<String> tree = new Tree<>(nodeA);
        tree.addNode(nodeA, nodeB, nodeC, nodeD, nodeE);
        tree.addNode(nodeC, nodeI, nodeJ, nodeK, nodeL, nodeM, nodeN, nodeO);
        tree.addNode(nodeJ, nodeP, nodeQ, nodeR, nodeS);
        tree.addNode(nodeR, nodeT, nodeU, nodeV);

//        ArrayList<TreeNode<String>> allPreNodes = tree.getAllPreNodes(nodeU);
//        for (TreeNode<String> tn : allPreNodes) {
//            System.out.printf(">%s;", tn.getValue());
//        }
        ArrayList<TreeNode<String>> allPreNodes = tree.getAllLowNodes(nodeU);
        for (TreeNode<String> tn : allPreNodes) {
            System.out.printf(">%s;", tn.getValue());
        }

    }


    public static void testDraw() {
        ArrayList<String> array = new ArrayList<>();
        array.add("a");//0
        array.add("b");//1
        array.add("c");//2
        array.add("d");//3
        array.add("e");//4
        array.add("f");//4

        int size = array.size();
        int mid = size / 2;
        int r = size % 2;

        if (r == 0) {//偶数
            for (int i = mid - 1; i >= 0; i--) {
                String top = array.get(i);
                String bottom = array.get(array.size() - i - 1);
                System.out.printf("draw %s,%s\n", top, bottom);
            }
        } else {

            System.out.printf("draw %s\n", array.get(mid));

            for (int i = mid - 1; i >= 0; i--) {
                String top = array.get(i);
                String bottom = array.get(array.size() - i - 1);
                System.out.printf("draw %s,%s\n", top, bottom);
            }
        }

    }


}
