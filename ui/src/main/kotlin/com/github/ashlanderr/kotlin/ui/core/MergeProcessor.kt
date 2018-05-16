package com.github.ashlanderr.kotlin.ui.core

import kotlin.math.min
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

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

            if (currentValue is Node && nextValue is Node) {
                prop.set(current, merge(currentValue, nextValue, current))
            } else if (currentValue != nextValue) {
                prop.set(current, nextValue)
            }
        }
    }

    private fun mergeLists(currentClazz: KClass<out Node>, current: Node, next: Node) {
        getLists(currentClazz).forEach { list ->
            val currentList = list.get(current)
            val nextList = list.get(next)

            val minSize = min(currentList.size, nextList.size)

            for (i in 0 until minSize) {
                val currentValue = currentList[i]
                val nextValue = nextList[i]
                currentList[i] = merge(currentValue, nextValue, current)
            }

            for (i in currentList.size until nextList.size) {
                val nextValue = nextList[i]
                mount(nextValue::class, nextValue, current)
                currentList.add(nextValue)
            }

            for (i in (currentList.size - 1) downTo nextList.size) {
                val currentValue = currentList[i]
                unmount(currentValue::class, currentValue)
                currentList.removeAt(i)
            }
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

        getProps(clazz)
                .mapNotNull { it.get(node) as? Node }
                .forEach { mount(it::class, it, node) }
        getLists(clazz)
                .flatMap { it.get(node) }
                .forEach { mount(it::class, it, node) }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getProps(clazz: KClass<out Node>): List<KMutableProperty1<Node, Any?>> {
        return clazz.memberProperties
                .filter { it.findAnnotation<ReactiveProperty>() != null }
                .map { it as KMutableProperty1<Node, Any?> }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getLists(clazz: KClass<out Node>): List<KProperty1<Node, MutableList<Node>>> {
        return clazz.memberProperties
                .filter { it.findAnnotation<ReactiveList>() != null }
                .map { it as KProperty1<Node, MutableList<Node>> }
    }
}