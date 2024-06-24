package com.m4ykey.core.views.recyclerview

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowInsets
import android.view.WindowManager
import android.view.WindowMetrics
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager

fun setupGridLayoutManager(
    context: Context,
    elementWidthDp: Float
) : GridLayoutManager {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics : WindowMetrics = context.getSystemService(WindowManager::class.java).currentWindowMetrics
        val insets = windowMetrics.windowInsets
            .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        val screenWidth = windowMetrics.bounds.width() - insets.left - insets.right

        val metrics = context.resources.displayMetrics
        val elementWidthPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, elementWidthDp, metrics)

        val spanCount = (screenWidth / elementWidthPx).toInt().coerceAtLeast(1)

        return GridLayoutManager(context, spanCount)
    } else {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val displayMetrics = DisplayMetrics()
        display.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels

        val metrics = context.resources.displayMetrics
        val elementWidthPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, elementWidthDp, metrics)

        val spanCount = (screenWidth / elementWidthPx).toInt().coerceAtLeast(1)

        return GridLayoutManager(context, spanCount)
    }
}