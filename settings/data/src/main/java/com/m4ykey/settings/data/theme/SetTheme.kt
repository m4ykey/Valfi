package com.m4ykey.settings.data.theme

import android.content.Context
import android.content.res.Configuration
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.m4ykey.settings.data.R

fun setCompatibleWithPhoneSettings(
    context: Context,
    textView: TextView,
    imageView: ImageView
) {
    val nightModeFlags = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    when (nightModeFlags) {
        Configuration.UI_MODE_NIGHT_YES -> {
            textView.text = context.getString(R.string.dark)
            imageView.setImageResource(R.drawable.ic_moon)
        }
        Configuration.UI_MODE_NIGHT_NO -> {
            textView.text = context.getString(R.string.light)
            imageView.setImageResource(R.drawable.ic_sun)
        }
        else -> {
            textView.text = context.getString(R.string.compatible_with_device_settings)
        }
    }
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
}

fun setDarkTheme(
    textView : TextView,
    imageView : ImageView,
    context : Context
) {
    textView.text = context.getString(R.string.dark)
    imageView.setImageResource(R.drawable.ic_moon)
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
}

fun setLightTheme(
    textView: TextView,
    imageView: ImageView,
    context : Context
) {
    textView.text = context.getString(R.string.light)
    imageView.setImageResource(R.drawable.ic_sun)
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
}