package com.owant.thinkmap.model;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by owant on 09/02/2017.
 */

public class TreeModel<T> implements Serializable {

    /**
     * the root for the tree
     */
    public NodeModel<T> rootNode;

    /**
     * 模型里的接口是不用序列号的
     */
    public transient ForTreeItem<NodeModel<T>> mForTreeItem;

    public TreeModel(NodeModel<T> rootNode) {
        this.rootNode = rootNode;
    }

    /**
     * add the node in some father node
     *
     * @param start
     * @param nodes
     */
    public void addNode(NodeModel<T> start, NodeModel<T>... nodes) {
        int index = 1;
        NodeModel<T> temp = start;
        if (temp.getParentNode() != null) {
            index = temp.getParentNode().floor;
        }

        LinkedList<NodeModel<T>> childNodes = temp.getChildNodes();

        for (NodeModel<T> t : nodes) {
            t.setParentNode(start);
            t.setFloor(index);

            //校验是否存在
            boolean exist = false;
            for (NodeModel<T> hash : childNodes) {
                if (hash == t) {
                    exist = true;
                    continue;
                }
            }
            if (!exist)
                start.getChildNodes().add(t);
        }
    }

    public boolean removeNode(NodeModel<T> starNode, NodeModel<T> deleteNote) {
        boolean rm = false;
        int size = starNode.getChildNodes().size();
        if (size > 0) {
            rm = starNode.getChildNodes().remove(deleteNote);
        }
        return rm;
    }

    public boolean inTree(NodeModel<T> starNode, NodeModel<T> deleteNote) {
        boolean in = false;
        Deque<NodeModel<T>> queue = new ArrayDeque<>();
        NodeModel<T> rootNode = starNode;
        queue.add(rootNode);

        while (!queue.isEmpty()) {
            rootNode = (NodeModel<T>) queue.poll();
            if (rootNode == deleteNote) {
                in = true;
            }

            LinkedList<NodeModel<T>> childNodes = rootNode.getChildNodes();
            if (childNodes.size() > 0) {
                for (NodeModel<T> item : childNodes) {
                    queue.add(item);
                }
            }
        }
        return in;
    }

    public NodeModel<T> getRootNode() {
        return rootNode;
    }

    public void setRootNode(NodeModel<T> rootNode) {
        this.rootNode = rootNode;
    }

    /**
     * 同一个父节点的上下
     *
     * @param midPreNode
     * @return
     */
    public NodeModel<T> getLowNode(NodeModel<T> midPreNode) {
        NodeModel<T> find = null;
        NodeModel<T> parentNode = midPreNode.getParentNode();

        if (parentNode != null && parentNode.getChildNodes().size() >= 2) {
            Deque<NodeModel<T>> queue = new ArrayDeque<>();
            NodeModel<T> rootNode = parentNode;
            queue.add(rootNode);
            boolean up = false;
            while (!queue.isEmpty()) {

                rootNode = (NodeModel<T>) queue.poll();
                if (up) {
                    if (rootNode.getFloor() == midPreNode.getFloor()) {
                        find = rootNode;
                    }
                    break;
                }

                //到了该元素
                if (rootNode == midPreNode) up = true;
                LinkedList<NodeModel<T>> childNodes = rootNode.getChildNodes();
                if (childNodes.size() > 0) {
                    for (NodeModel<T> item : childNodes) {
                        queue.add(item);
                    }
                }
            }
        }
        return find;
    }

    public NodeModel<T> getPreNode(NodeModel<T> midPreNode) {

        NodeModel<T> parentNode = midPreNode.getParentNode();
        NodeModel<T> find = null;

        if (parentNode != null && parentNode.getChildNodes().size() > 0) {

            Deque<NodeModel<T>> queue = new ArrayDeque<>();
            NodeModel<T> rootNode = parentNode;
            queue.add(rootNode);

            while (!queue.isEmpty()) {
                rootNode = (NodeModel<T>) queue.poll();
                //到了该元素
                if (rootNode == midPreNode) {
                    //返回之前的值
                    break;
                }

                find = rootNode;
                LinkedList<NodeModel<T>> childNodes = rootNode.getChildNodes();
                if (childNodes.size() > 0) {
                    for (NodeModel<T> item : childNodes) {
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

    public ArrayList<NodeModel<T>> getAllLowNodes(NodeModel<T> addNode) {
        ArrayList<NodeModel<T>> array = new ArrayList<>();
        NodeModel<T> parentNode = addNode.getParentNode();
        while (parentNode != null) {
            NodeModel<T> lowNode = getLowNode(parentNode);
            while (lowNode != null) {
                array.add(lowNode);
                lowNode = getLowNode(lowNode);
            }
            parentNode = parentNode.getParentNode();
        }
        return array;
    }

    public ArrayList<NodeModel<T>> getAllPreNodes(NodeModel<T> addNode) {
        ArrayList<NodeModel<T>> array = new ArrayList<>();
        NodeModel<T> parentNode = addNode.getParentNode();
        while (parentNode != null) {
            NodeModel<T> lowNode = getPreNode(parentNode);
            while (lowNode != null) {
                array.add(lowNode);
                lowNode = getPreNode(lowNode);
            }
            parentNode = parentNode.getParentNode();
        }
        return array;
    }

    public LinkedList<NodeModel<T>> getNodeChildNodes(NodeModel<T> node) {
        return node.getChildNodes();
    }

    public void ergodicTreeInDeep(int msg) {
        Stack<NodeModel<T>> stack = new Stack<>();
        NodeModel<T> rootNode = getRootNode();
        stack.add(rootNode);
        while (!stack.isEmpty()) {
            NodeModel<T> pop = stack.pop();
            if (mForTreeItem != null) {
                mForTreeItem.next(msg, pop);
            }
            LinkedList<NodeModel<T>> childNodes = pop.getChildNodes();
            for (NodeModel<T> item : childNodes) {
                stack.add(item);
            }
        }
    }

    public void ergodicTreeInWith(int msg) {
        Deque<NodeModel<T>> queue = new ArrayDeque<>();
        NodeModel<T> rootNode = getRootNode();
        queue.add(rootNode);
        while (!queue.isEmpty()) {
            rootNode = (NodeModel<T>) queue.poll();
            if (mForTreeItem != null) {
                mForTreeItem.next(msg, rootNode);
            }
            LinkedList<NodeModel<T>> childNodes = rootNode.getChildNodes();
            if (childNodes.size() > 0) {
                for (NodeModel<T> item : childNodes) {
                    queue.add(item);
                }
            }
        }
    }

    public void addForTreeItem(ForTreeItem<NodeModel<T>> forTreeItem) {
        this.mForTreeItem = forTreeItem;
    }

}
