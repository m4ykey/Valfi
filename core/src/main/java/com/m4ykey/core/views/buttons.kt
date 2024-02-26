package com.m4ykey.core.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import com.google.android.material.button.MaterialButton

fun buttonAnimation(imageView: ImageView, resourceId: Int) {
    imageView.animate()
        .alpha(0f)
        .setDuration(200)
        .withEndAction {
            imageView.setImageResource(resourceId)
            imageView.animate()
                .alpha(1f)
                .setDuration(200)
                .start()
        }
        .start()
}

fun setupButton(
    imageView : ImageView,
    isSaved : Boolean,
    onClick : () -> Unit,
    unSavedResourceId : Int,
    savedResourceId : Int
) {
    imageView.apply {
        setOnClickListener { onClick() }
        setImageResource(if (isSaved) savedResourceId else unSavedResourceId)
    }
}

fun buttonsIntents(button: MaterialButton, url: String, context : Context) {
    button.setOnClickListener {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}