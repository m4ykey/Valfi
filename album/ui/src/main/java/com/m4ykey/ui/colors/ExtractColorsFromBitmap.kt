package com.m4ykey.ui.colors

import android.graphics.Bitmap
import androidx.palette.graphics.Palette

fun extractColorsFromBitmap(bitmap: Bitmap) : List<Int> {
    val palette = Palette.from(bitmap).generate()
    return palette.swatches.map { it.rgb }
}