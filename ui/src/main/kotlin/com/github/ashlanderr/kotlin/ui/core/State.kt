package com.github.ashlanderr.kotlin.ui.core

abstract class State<S : State<S, C>, C : Component<S, C>> {
    lateinit var component: C
    abstract fun render(): Node
    open fun dispose() {}

    inline fun <T> update(mutator: () -> T) {
        mutator()
        ancestorNode<Application>()!!.requestAnimationFrame()
    }

    inline fun <reified T : Node> ancestorNode() = component.ancestor<T>()
    inline fun <reified C : Component<S, C>, S : State<S, C>> ancestorState() = ancestorNode<C>()?.state
}