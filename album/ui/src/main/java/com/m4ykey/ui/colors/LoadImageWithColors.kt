package com.m4ykey.ui.colors

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

fun loadImageWithColors(
    imageView: ImageView,
    url : String,
    context : Context,
    callback : (Bitmap) -> Unit
) {
    Glide.with(context)
        .asBitmap()
        .load(url)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                imageView.setImageBitmap(resource)
                callback(resource)
            }
            override fun onLoadCleared(placeholder: Drawable?) {}
        })
}