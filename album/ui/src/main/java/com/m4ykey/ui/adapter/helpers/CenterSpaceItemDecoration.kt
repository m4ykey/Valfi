package com.m4ykey.ui.adapter.helpers

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CenterSpaceItemDecoration(private val space: Float) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = (space / 2).toInt()
        outRect.right = (space / 2).toInt()
    }
}