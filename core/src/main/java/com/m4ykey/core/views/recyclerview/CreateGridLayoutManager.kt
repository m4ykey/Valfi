package com.m4ykey.core.views.recyclerview

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.core.views.recyclerview.adapter.LoadStateAdapter

fun RecyclerView.createGridLayoutManager(
    headerAdapter : LoadStateAdapter,
    footerAdapter : LoadStateAdapter
) : GridLayoutManager {
    return GridLayoutManager(context, 3).apply {
        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when {
                    position == 0 && headerAdapter.itemCount > 0 -> 3
                    position == adapter?.itemCount?.minus(1) && footerAdapter.itemCount > 0 -> 3
                    else -> 1
                }
            }
        }
    }
}