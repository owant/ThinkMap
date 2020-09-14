package com.owant.thinkmap

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * created by Kyle.Zhong on 2020/9/13
 */

class NodeHandlerTest {

    lateinit var rootNode: NodeModel<Person>
    lateinit var child: NodeModel<Person>
    lateinit var child2: NodeModel<Person>
    lateinit var child3: NodeModel<Person>
    lateinit var child4: NodeModel<Person>

    @Before
    fun setUp() {
        rootNode = NodeModel<Person>(Person(100, "Father"))

        child = NodeModel<Person>(Person(12, "Child"))
        child.floor = 2
        child.focusing = false

        child2 = NodeModel<Person>(Person(10, "Child2"))
        child2.focusing = false

        child3 = NodeModel<Person>(Person(10, "Child3"))
        child3.focusing = false

        child4 = NodeModel<Person>(Person(10, "Child4"))
        child4.focusing = false

        rootNode.addNode(child)
        rootNode.addNode(child2)
        rootNode.addNode(child3)
        rootNode.addNode(child4)
    }

    @Test
    fun getLowNode() {
        var nodeHandler = NodeHandler<Person>()
        val lowNode = nodeHandler.getLowNode(child2);
        assertEquals(lowNode, child3)
    }

    @Test
    fun getUpNode() {
        var nodeHandler = NodeHandler<Person>()
        val upNode = nodeHandler.getUpNode(child2);
        assertEquals(upNode, child)
    }

}