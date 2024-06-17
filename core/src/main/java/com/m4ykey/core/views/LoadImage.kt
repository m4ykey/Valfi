package com.m4ykey.core.views

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.m4ykey.core.R

fun loadImage(imageView: ImageView, imageUrl : String, context : Context) {
    Glide.with(context)
        .load(imageUrl)
        .error(R.drawable.album_error)
        .transition(DrawableTransitionOptions.withCrossFade(500))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(imageView)
}