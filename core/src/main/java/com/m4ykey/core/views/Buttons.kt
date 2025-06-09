package com.m4ykey.core.views

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import androidx.core.net.toUri
import com.google.android.material.button.MaterialButton

fun ImageView.buttonAnimation(resourceId: Int) {
    animate()
        .alpha(0f)
        .setDuration(200)
        .withEndAction {
            setImageResource(resourceId)
            animate()
                .alpha(1f)
                .setDuration(200)
                .start()
        }
        .start()
}

fun buttonsIntents(button: MaterialButton, url: String, context: Context) {
    button.setOnClickListener {
        context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
    }
}