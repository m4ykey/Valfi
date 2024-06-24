package com.m4ykey.core.views.animations

import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.ViewHolder.applyAnimation(
    position : Int,
    lastVisibleItemPosition : Int
) {
    if (position > lastVisibleItemPosition) {
        val fadeIn = AlphaAnimation(0f, 1f).apply { duration = 300 }
        itemView.startAnimation(fadeIn)
    }
}