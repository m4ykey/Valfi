package com.m4ykey.core.views

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri

fun openUrlBrowser(context : Context, url : String) {
    if (url.isBlank() || !url.startsWith("http")) return

    val customTabsIntent = CustomTabsIntent.Builder().build()

    try {
        customTabsIntent.launchUrl(context, url.toUri())
    } catch (e : ActivityNotFoundException) {
        val browser = Intent(Intent.ACTION_VIEW, url.toUri())
        context.startActivity(browser)
    }
}