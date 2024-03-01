package com.m4ykey.core.views

import android.widget.ImageView
import coil.load

fun loadImage(imageView: ImageView, imageUrl : String) {
    imageView.load(imageUrl) {
        crossfade(true)
        crossfade(500)
    }
}