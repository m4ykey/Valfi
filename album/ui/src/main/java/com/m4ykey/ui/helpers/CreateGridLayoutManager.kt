package com.m4ykey.ui.helpers

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.core.views.recyclerview.setupGridLayoutManager

fun createGridLayoutManager(context : Context) : RecyclerView.LayoutManager {
    val isTablet = context.resources.configuration.smallestScreenWidthDp >= 600
    return setupGridLayoutManager(context, if (isTablet) 150f else 110f)
}