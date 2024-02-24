package com.m4ykey.core.views.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CenterSpaceItemDecoration(
    private val space: Float,
    private val isVertical : Boolean = true
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (isVertical) {
            outRect.left = (space / 2).toInt()
            outRect.right = (space / 2).toInt()
            outRect.top = (space / 2).toInt()
            outRect.bottom = (space / 2).toInt()
        } else {
            outRect.left = (space / 2).toInt()
            outRect.right = (space / 2).toInt()
        }
    }
}