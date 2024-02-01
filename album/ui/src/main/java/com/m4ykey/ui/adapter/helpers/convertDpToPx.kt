package com.m4ykey.ui.adapter.helpers

import android.content.res.Resources
import android.util.TypedValue

fun convertDpToPx(dp: Int): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        Resources.getSystem().displayMetrics
    )
}
