package com.github.ashlanderr.kotlin.ui.graphics

import com.github.ashlanderr.kotlin.ui.core.State

class AnimationManager(private val state: State) {
    private val animations = mutableListOf<Updatable>()

    fun <T: Updatable> add(animation: T): T {
        animations.add(animation)
        return animation
    }

    fun remove(animation: Updatable) {
        animations.remove(animation)
    }

    fun update() {
        animations.removeIf { it.update(state) }
    }
}