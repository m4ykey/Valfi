package com.m4ykey.core.views.recyclerview

import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.m4ykey.core.views.recyclerview.animations.slideInFromRight
import com.m4ykey.core.views.recyclerview.animations.slideOutToRight

fun scrollListener(button : FloatingActionButton) = object : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val shouldShowButton = recyclerView.computeVerticalScrollOffset() > 5000
        button.let {
            if (shouldShowButton) {
                if (it.isGone) it.slideInFromRight()
            } else {
                if (it.isVisible) it.slideOutToRight()
            }
        }
    }
}