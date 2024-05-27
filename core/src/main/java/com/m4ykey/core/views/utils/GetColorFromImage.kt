package com.m4ykey.core.views.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

fun getColorFromImage(imageUrl : String, context : Context, onColorReady : (Int) -> Unit) {
    Glide.with(context)
        .asBitmap()
        .load(imageUrl)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                Palette.from(resource).generate { palette ->
                    val mutedSwatchColor = palette?.mutedSwatch?.rgb
                    val dominantColor = palette?.dominantSwatch?.rgb
                    if (mutedSwatchColor != null) {
                        onColorReady(mutedSwatchColor)
                    } else if (dominantColor != null) {
                        onColorReady(dominantColor)
                    }
                }
            }
            override fun onLoadCleared(placeholder: Drawable?) {}
        })
}
