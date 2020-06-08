package com.owant.thinkmap

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * created by Kyle.Zhong on 2020/6/8
 */

@RunWith(JUnit4::class)
class NodeModelTest {

    @Test
    fun createNode() {
        var rootNode = NodeModel<Person>(Person(100, "Father"));
        rootNode.float = 1
        rootNode.focusing = true


        var child = NodeModel<Person>(Person(12, "Child"))
        child.float = 2
        child.focusing = false

        var child2 = NodeModel<Person>(Person(10, "Child2"))
        child2.focusing = false

        rootNode.childNodes?.add(child)
        rootNode.childNodes?.add(child2)

        Assert.assertEquals(rootNode.focusing, true)
        Assert.assertEquals(rootNode.childNodes?.get(0)?.focusing, false)
        Assert.assertEquals(rootNode.childNodes?.get(0)?.nodeValue?.name, "Child")
        Assert.assertEquals(rootNode.childNodes?.get(1)?.nodeValue?.name, "Child2")
    }

}