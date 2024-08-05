package com.m4ykey.settings.theme

enum class ThemeOptions(val index : Int) {
    LIGHT(0),
    DARK(1),
    DEFAULT(2);

    companion object {
        fun fromIndex(index : Int) : ThemeOptions {
            return entries.firstOrNull { it.index == index } ?: DEFAULT
        }
    }
}