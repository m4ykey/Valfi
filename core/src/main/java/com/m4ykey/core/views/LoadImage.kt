package com.m4ykey.core.views

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

fun loadImage(imageView: ImageView, imageUrl : String, context : Context) {
    Glide.with(context)
        .load(imageUrl)
        .into(imageView)
}