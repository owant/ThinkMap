package com.owant.drawtreeview.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by owant on 16/12/2016.
 */

public class Tree<T> {

    /**
     * the root for the tree
     */
    public TreeNode<T> rootNode;

    public Tree(TreeNode<T> rootNode) {
        this.rootNode = rootNode;
    }

    /**
     * add the node in some father node
     *
     * @param start
     * @param nodes
     */
    public void addNode(TreeNode<T> start, TreeNode<T>... nodes) {
        int index = 1;
        TreeNode<T> temp = start;
        if (temp.getParentNode() != null) {
            index = temp.getParentNode().floor;
        }

        for (TreeNode<T> t : nodes) {
            t.setParentNode(start);
            t.setFloor(index);
            start.getChildNodes().add(t);
        }
    }

    public boolean remvoeNode(TreeNode<T> starNode, TreeNode<T> deleteNote) {
        boolean rm = false;
        int size = starNode.getChildNodes().size();
        if (size > 0) {
            rm = starNode.getChildNodes().remove(deleteNote);
        }
        return rm;
    }

    public TreeNode<T> getRootNode() {
        return rootNode;
    }

    public void setRootNode(TreeNode<T> rootNode) {
        this.rootNode = rootNode;
    }

    /**
     * 同一个父节点的上下
     *
     * @param midPreNode
     * @return
     * @throws NotFindNodeException
     */
    public TreeNode<T> getLowNode(TreeNode<T> midPreNode) {
        TreeNode<T> find = null;
        TreeNode<T> parentNode = midPreNode.getParentNode();

        if (parentNode != null && parentNode.getChildNodes().size() >= 2) {
            Deque<TreeNode<T>> queue = new ArrayDeque<>();
            TreeNode<T> rootNode = parentNode;
            queue.add(rootNode);
            boolean up = false;
            while (!queue.isEmpty()) {

                rootNode = (TreeNode<T>) queue.poll();
                if (up) {
                    if (rootNode.getFloor() == midPreNode.getFloor()) {
                        find = rootNode;
                    }
                    break;
                }

                //到了该元素
                if (rootNode == midPreNode) up = true;
                LinkedList<TreeNode<T>> childNodes = rootNode.getChildNodes();
                if (childNodes.size() > 0) {
                    for (TreeNode<T> item : childNodes) {
                        queue.add(item);
                    }
                }
            }
        }
        return find;
    }

    public TreeNode<T> getPreNode(TreeNode<T> midPreNode) {

        TreeNode<T> parentNode = midPreNode.getParentNode();
        TreeNode<T> find = null;

        if (parentNode != null && parentNode.getChildNodes().size() > 0) {

            Deque<TreeNode<T>> queue = new ArrayDeque<>();
            TreeNode<T> rootNode = parentNode;
            queue.add(rootNode);

            while (!queue.isEmpty()) {
                rootNode = (TreeNode<T>) queue.poll();
                //到了该元素
                if (rootNode == midPreNode) {
                    //返回之前的值
                    break;
                }

                find = rootNode;
                LinkedList<TreeNode<T>> childNodes = rootNode.getChildNodes();
                if (childNodes.size() > 0) {
                    for (TreeNode<T> item : childNodes) {
                        queue.add(item);
                    }
                }
            }

            if (find != null && find.getFloor() != midPreNode.getFloor()) {
                find = null;
            }
        }
        return find;
    }

    public ArrayList<TreeNode<T>> getAllLowNodes(TreeNode<T> addNode) {
        ArrayList<TreeNode<T>> array = new ArrayList<>();
        TreeNode<T> parentNode = addNode.getParentNode();
        while (parentNode != null) {
            TreeNode<T> lowNode = getLowNode(parentNode);
            while (lowNode != null) {
                array.add(lowNode);
                lowNode = getLowNode(lowNode);
            }
            parentNode = parentNode.getParentNode();
        }
        return array;
    }

    public ArrayList<TreeNode<T>> getAllPreNodes(TreeNode<T> addNode) {
        ArrayList<TreeNode<T>> array = new ArrayList<>();
        TreeNode<T> parentNode = addNode.getParentNode();
        while (parentNode != null) {
            TreeNode<T> lowNode = getPreNode(parentNode);
            while (lowNode != null) {
                array.add(lowNode);
                lowNode = getPreNode(lowNode);
            }
            parentNode = parentNode.getParentNode();
        }
        return array;
    }

    public LinkedList<TreeNode<T>> getNodeChildNodes(TreeNode<T> node) {
        return node.getChildNodes();
    }

    public void printTree() {
        Stack<TreeNode<T>> stack = new Stack<>();
        TreeNode<T> rootNode = getRootNode();
        stack.add(rootNode);
        while (!stack.isEmpty()) {
            TreeNode<T> pop = stack.pop();
            System.out.println(pop.getValue().toString());
            LinkedList<TreeNode<T>> childNodes = pop.getChildNodes();
            for (TreeNode<T> item : childNodes) {
                stack.add(item);
            }
        }
    }

    public void printTree2() {
        Deque<TreeNode<T>> queue = new ArrayDeque<>();
        TreeNode<T> rootNode = getRootNode();
        queue.add(rootNode);
        while (!queue.isEmpty()) {
            rootNode = (TreeNode<T>) queue.poll();
            System.out.println(rootNode.getValue().toString());

            LinkedList<TreeNode<T>> childNodes = rootNode.getChildNodes();
            if (childNodes.size() > 0) {
                for (TreeNode<T> item : childNodes) {
                    queue.add(item);
                }
            }
        }

    }


}
