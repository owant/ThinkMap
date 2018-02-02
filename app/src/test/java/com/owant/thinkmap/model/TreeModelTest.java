package com.owant.thinkmap.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by owant on 14/04/2017.
 */
public class TreeModelTest {

    TreeModel<String> mTreeModel;

    @Before
    public void setUp() throws Exception {
        NodeModel<String> root = new NodeModel<>("root");
        mTreeModel = new TreeModel<>(root);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void addNode() throws Exception {
        NodeModel<String> addNode = new NodeModel<>("add note");
        mTreeModel.addNode(mTreeModel.getRootNode(), addNode);

        mTreeModel.addForTreeItem(new ForTreeItem<NodeModel<String>>() {
            @Override
            public void next(int msg, NodeModel<String> next) {
                System.out.printf("%s>>", next.getValue());
            }
        });
        mTreeModel.ergodicTreeInWith(1);
    }

    @Test
    public void removeNode() throws Exception {

    }

    @Test
    public void inTree() throws Exception {

    }

    @Test
    public void getRootNode() throws Exception {

    }

    @Test
    public void setRootNode() throws Exception {

    }

    @Test
    public void getLowNode() throws Exception {

    }

    @Test
    public void getPreNode() throws Exception {

    }

    @Test
    public void getAllLowNodes() throws Exception {

    }

    @Test
    public void getAllPreNodes() throws Exception {

    }

    @Test
    public void getNodeChildNodes() throws Exception {

    }

    @Test
    public void ergodicTreeInDeep() throws Exception {

    }

    @Test
    public void ergodicTreeInWith() throws Exception {

    }

    @Test
    public void addForTreeItem() throws Exception {

    }

}