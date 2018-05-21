package com.github.ashlanderr.kotlin.ui.core

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

object MergeProcessor {
    fun merge(current: Node, next: Node, parent: Node? = null): Node {
        val currentClazz = current::class
        val nextClazz = next::class

        if (currentClazz != nextClazz) {
            unmount(currentClazz, current)
            mount(nextClazz, next, parent)
            return next
        }

        mergeProps(currentClazz, current, next)
        mergeLists(currentClazz, current, next)

        return current
    }

    private fun mergeProps(clazz: KClass<out Node>, current: Node, next: Node) {
        getProps(clazz).forEach { prop ->
            val currentValue = prop.get(current)
            val nextValue = prop.get(next)
            if (currentValue != nextValue) {
                prop.set(current, nextValue)
            }
        }
        getNodes(clazz).forEach { prop ->
            val currentValue = prop.get(current)
            val nextValue = prop.get(next)
            prop.set(current, merge(currentValue, nextValue, current))
        }
    }

    private fun mergeLists(currentClazz: KClass<out Node>, current: Node, next: Node) {
        getLists(currentClazz).forEach { list ->
            val currentList = list.get(current)
            val nextList = list.get(next)
            val newList = ArrayList<Node>(currentList.size)
            val mergedSet = HashSet<Node>(currentList.size)

            val currentKeys = currentList
                .filter { it.key != null }
                .associateBy { it.key }

            for (nextIndex in nextList.indices) {
                val nextValue = nextList[nextIndex]

                val currentValue = if (nextValue.key != null)
                    currentKeys[nextValue.key]
                else
                    currentList.getOrNull(nextIndex)

                val mergedValue = merge(currentValue ?: EmptyNode, nextValue, current)
                newList.add(mergedValue)

                if (currentValue != null) mergedSet.add(currentValue)
            }

            for (currentValue in currentList) {
                if (currentValue !in mergedSet) {
                    unmount(currentValue::class, currentValue)
                }
            }

            currentList.clear()
            currentList.addAll(newList)
        }
    }

    private fun unmount(clazz: KClass<out Node>, node: Node) {
        getProps(clazz)
            .mapNotNull { it.get(node) as? Node }
            .forEach { unmount(it::class, it) }
        getLists(clazz)
            .flatMap { it.get(node) }
            .forEach { unmount(it::class, it) }

        println("unmount $node")
        node.unmount()
    }

    private fun mount(clazz: KClass<out Node>, node: Node, parent: Node?) {
        println("mount $node")
        node.mount(parent)

        getNodes(clazz)
            .map { it.get(node) }
            .forEach { mount(it::class, it, node) }
        getLists(clazz)
            .flatMap { it.get(node) }
            .forEach { mount(it::class, it, node) }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getProps(clazz: KClass<out Node>): List<KMutableProperty1<Node, Any?>> {
        val names = mutableSetOf<String>()
        clazz.primaryConstructor
            ?.parameters
            ?.mapNotNullTo(names) { it.name }

        return clazz.memberProperties
            .filter { it.name in names && it.findAnnotation<RxNode>() == null && it.findAnnotation<RxList>() == null }
            .map { it as KMutableProperty1<Node, Any?> }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getNodes(clazz: KClass<out Node>): List<KMutableProperty1<Node, Node>> {
        return clazz.memberProperties
            .filter { it.findAnnotation<RxNode>() != null }
            .map { it as KMutableProperty1<Node, Node> }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getLists(clazz: KClass<out Node>): List<KProperty1<Node, MutableList<Node>>> {
        return clazz.memberProperties
            .filter { it.findAnnotation<RxList>() != null }
            .map { it as KProperty1<Node, MutableList<Node>> }
    }
}