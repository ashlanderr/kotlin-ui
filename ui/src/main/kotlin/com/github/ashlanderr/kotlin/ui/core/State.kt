package com.github.ashlanderr.kotlin.ui.core

abstract class State {
    abstract val component: StatefulComponent<*, *>
    abstract fun render(): Node
    open fun dispose() {}

    inline fun <T> update(mutator: () -> T) {
        mutator()
        ancestorNode<Application>()!!.requestAnimationFrame()
    }

    inline fun <reified T : Node> ancestorNode() = component.ancestor<T>()
    inline fun <reified C : StatefulComponent<S, C>, S : State> ancestorState() = ancestorNode<C>()?.state
}