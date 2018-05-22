package com.github.ashlanderr.kotlin.ui.graphics

import com.github.ashlanderr.kotlin.ui.core.State

interface Updatable {
    fun update(state: State): Boolean
}