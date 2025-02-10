package com.m4ykey.core.views.recyclerview

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowInsets
import android.view.WindowManager
import android.view.WindowMetrics
import androidx.recyclerview.widget.GridLayoutManager

fun setupGridLayoutManager(
    context: Context,
    elementWidthDp: Float
): GridLayoutManager {
    val screenWidth = getScreenWidth(context)
    val metrics = context.resources.displayMetrics
    val elementWidthPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, elementWidthDp, metrics)

    val spanCount = (screenWidth / elementWidthPx).toInt().coerceAtLeast(1)

    return GridLayoutManager(context, spanCount)
}

fun getScreenWidth(context: Context): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics: WindowMetrics = context.getSystemService(WindowManager::class.java).currentWindowMetrics
        val insets = windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        windowMetrics.bounds.width() - insets.left - insets.right
    } else {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayMetrics.widthPixels
    }
}
