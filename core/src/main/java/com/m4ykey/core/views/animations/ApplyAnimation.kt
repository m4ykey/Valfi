package com.m4ykey.core.views.animations

import android.view.View
import android.view.animation.AlphaAnimation

fun applyAnimation(view : View) {
    if (view.animation == null) {
        val fadeIn = AlphaAnimation(0f, 1f).apply {
            duration = 300
        }
        view.animation = fadeIn
    }
}