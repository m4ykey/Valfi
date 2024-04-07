package com.m4ykey.core.views.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

fun copyName(name: String, context : Context) {
    if (name.isNotBlank()) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val data = ClipData.newPlainText(null, name)
        clipboard.setPrimaryClip(data)
        showToast(context, "Copied to clipboard")
    } else {
        showToast(context, "Nothing to copy")
    }
}