package com.m4ykey.core.views

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

fun openUrlBrowser(context : Context, url : String) {
    val customTabsIntent = CustomTabsIntent.Builder().build()
    customTabsIntent.launchUrl(context, Uri.parse(url))
}