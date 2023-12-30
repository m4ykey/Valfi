package com.m4ykey.core.views

import android.content.res.Configuration
import android.content.res.Resources

fun isNightMode(resources: Resources) : Boolean {
    val currentMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return currentMode == Configuration.UI_MODE_NIGHT_YES
}