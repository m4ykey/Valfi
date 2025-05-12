package com.m4ykey.core.views

import android.widget.ImageView
import coil3.load
import coil3.request.CachePolicy
import coil3.request.crossfade

fun loadImage(imageView: ImageView, imageUrl : String) {
    imageView.load(imageUrl) {
        crossfade(true)
        diskCachePolicy(CachePolicy.ENABLED)
        networkCachePolicy(CachePolicy.ENABLED)
    }
}