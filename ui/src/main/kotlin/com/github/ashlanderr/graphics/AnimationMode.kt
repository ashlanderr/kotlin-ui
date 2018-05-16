package com.github.ashlanderr.graphics

interface AnimationMode {
    fun isCompleted(times: Int): Boolean

    object SINGLE : AnimationMode {
        override fun isCompleted(times: Int): Boolean = times > 0
    }

    object INFINITE : AnimationMode {
        override fun isCompleted(times: Int): Boolean = false
    }
}