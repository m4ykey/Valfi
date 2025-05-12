package com.m4ykey.ui.album.colors

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.toBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun loadImageWithColors(
    imageView: ImageView,
    url: String,
    context : Context,
    callback: (Bitmap) -> Unit
) {
    val request = ImageRequest.Builder(context)
        .data(url)
        .target(
            onSuccess = { result ->
                val bitmap = result.toBitmap()
                imageView.setImageBitmap(bitmap)
                callback(bitmap)
            }
        )
        .build()

    CoroutineScope(Dispatchers.Main).launch {
        context.imageLoader.enqueue(request)
    }
}