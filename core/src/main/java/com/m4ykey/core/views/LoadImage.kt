package com.m4ykey.core.views

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

fun loadImage(imageView: ImageView, imageUrl : String, context : Context) {
    Glide.with(context)
        .load(imageUrl)
        .transition(DrawableTransitionOptions.withCrossFade(500))
        .into(imageView)
}