package com.m4ykey.ui

import androidx.annotation.StringRes

enum class ThemeOptions(@StringRes val displayName : Int) {
    DEFAULT(R.string.default_theme),
    DARK(R.string.dark_theme),
    LIGHT(R.string.light_theme)
}