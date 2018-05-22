package com.github.ashlanderr.kotlin.ui.core

import java.awt.Color

fun Color.copy(red: Int? = null, green: Int? = null, blue: Int? = null, alpha: Int? = null)
    = Color(red ?: this.red, green ?: this.green, blue ?: this.blue, alpha ?: this.alpha)