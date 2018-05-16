package com.github.ashlanderr.core

abstract class State<S : State<S, C>, C : Component<S, C>> {
    lateinit var component: C
    abstract fun render(): Node
    open fun dispose() {}

    inline fun <T> update(mutator: () -> T) {
        mutator()
        ancestorNode<Application>().requestAnimationFrame()
    }

    inline fun <reified T : Node> ancestorNode(): T {
        var parent: Node? = component
        while (parent != null && parent !is T) {
            parent = parent.parent
        }
        return parent as T
    }

    inline fun <reified C : Component<S, C>, S : State<S, C>> ancestorState(): S {
        return ancestorNode<C>().state
    }
}