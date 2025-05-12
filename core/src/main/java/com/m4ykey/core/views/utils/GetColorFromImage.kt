package com.m4ykey.core.views.utils

import android.widget.ImageView
import androidx.palette.graphics.Palette
import coil3.load
import coil3.request.allowHardware
import coil3.toBitmap

fun getColorFromImage(
    imageUrl : String,
    onColorReady : (Int) -> Unit,
    imageView : ImageView
) {
    imageView.load(imageUrl) {
        allowHardware(false)
        listener(
            onSuccess = { _, result ->
                Palette.Builder(result.image.toBitmap()).generate { palette ->
                    val mutedSwatchColor = palette?.mutedSwatch?.rgb
                    val dominantColor = palette?.dominantSwatch?.rgb
                    if (mutedSwatchColor != null) {
                        onColorReady(mutedSwatchColor)
                    } else if (dominantColor != null) {
                        onColorReady(dominantColor)
                    }
                }
            }
        )
    }
}
