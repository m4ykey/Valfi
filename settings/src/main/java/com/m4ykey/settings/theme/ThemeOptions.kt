package com.m4ykey.settings.theme

import com.m4ykey.settings.R

sealed class ThemeOptions(
    val index : Int,
    val name : Int,
    val icon : Int
) {
    data object Light : ThemeOptions(0, R.string.light, R.drawable.ic_sun)
    data object Dark : ThemeOptions(1, R.string.dark, R.drawable.ic_moon)
    data object Default : ThemeOptions(2, R.string.compatible_with_phone_settings, R.drawable.ic_sun)

    companion object {
        fun fromIndex(index : Int) : ThemeOptions {
            return when (index) {
                0 -> Light
                1 -> Dark
                2 -> Default
                else -> Default
            }
        }
    }
}