package com.owant.thinkmap

/**
 *  created by Kyle.Zhong on 2020/8/23
 */

class NodeHandler<T> {

    /**
     * 查找当前的下一个节点
     */
    fun getUpNode(currentNode: NodeModel<T>): NodeModel<T>? {
        val parentNode = currentNode.parentNode
        //父节点不为空并且子节点至少有两个
        val childNodes = parentNode?.getNodes()
        var item: NodeModel<T>? = null
        childNodes?.forEach {
            //搜索到当前
            if (it == currentNode) {
                //获取上一次
                return item
            }
            item = it
        }
        return null
    }

    /**
     * 查找当前的下一个节点
     */
    fun getLowNode(currentNode: NodeModel<T>): NodeModel<T>? {
        val parentNode = currentNode.parentNode
        //父节点不为空并且子节点至少有两个
        val childNodes = parentNode?.getNodes()
        var targetCurrent = false
        var item: NodeModel<T>? = null
        childNodes?.forEach {
            item = it
            if (targetCurrent) {
                return it
            }
            if (item == currentNode) {
                targetCurrent = true
            }
        }
        return null
    }


}