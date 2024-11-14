package com.m4ykey.core.views.recyclerview

import android.view.View
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
                if (it.visibility == View.GONE) it.slideInFromRight()
            } else {
                if (it.visibility == View.VISIBLE) it.slideOutToRight()
            }
        }
    }
}