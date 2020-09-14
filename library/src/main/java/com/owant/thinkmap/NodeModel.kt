package com.owant.thinkmap

import java.util.*

/**
 *  created by Kyle.Zhong on 2020/6/8
 *  节点
 */
open class NodeModel<T> {

    /**
     * 父节点
     */
    var parentNode: NodeModel<T>? = null

    /**
     * 当前节点的数据
     */
    var nodeValue: T

    /**
     * 子节点集合
     */
    private var childNodes: LinkedList<NodeModel<T>> = LinkedList<NodeModel<T>>()

    /**
     * 当前的层级
     */
    var floor: Int = 0

    /**
     * 是否关闭
     */
    var close: Boolean? = false

    /**
     * 是否对焦
     */
    var focusing: Boolean? = false


    fun addNode(child: NodeModel<T>) {
        child.parentNode = this
        child.floor = floor + 1
        getNodes().add(child)
    }

    fun getNodes(): LinkedList<NodeModel<T>> {
        return childNodes
    }

    constructor(nodeValue: T) {
        this.nodeValue = nodeValue
    }

    override fun toString(): String {
        return "NodeModel(parentNode not empty=${parentNode != null}, nodeValue=${nodeValue.toString()}, childSize=${childNodes.size}, floor=$floor, close=$close, focusing=$focusing)"
    }


}
