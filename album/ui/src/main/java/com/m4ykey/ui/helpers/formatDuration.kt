package com.m4ykey.ui.helpers

fun formatDuration(seconds : Int) : String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%d:%02d", minutes, remainingSeconds)
}