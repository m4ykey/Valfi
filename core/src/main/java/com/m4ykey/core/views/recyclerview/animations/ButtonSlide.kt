package com.m4ykey.core.views.recyclerview.animations

import android.view.View

fun View.slideInFromRight() {
    this.animate()
        .translationX(0f)
        .alpha(1f)
        .setDuration(500)
        .withStartAction { this.visibility = View.VISIBLE }
        .start()
}

fun View.slideOutToRight() {
    this.animate()
        .translationX(this.width.toFloat())
        .alpha(0f)
        .setDuration(500)
        .withEndAction { this.visibility = View.GONE }
        .start()
}