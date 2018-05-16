package com.github.ashlanderr.kotlin.ui.core

typealias Builder<T> = T.() -> Unit

inline fun <reified T : Any> build(builder: Builder<T>): T = T::class.java.newInstance().apply(builder)