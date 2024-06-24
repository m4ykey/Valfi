package com.m4ykey.core.views.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.m4ykey.core.R

fun copyName(name: String, context : Context) {
    if (name.isNotBlank()) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val data = ClipData.newPlainText(null, name)
        clipboard.setPrimaryClip(data)
        showToast(context, context.getString(R.string.copied_to_clipboard))
    } else {
        showToast(context, context.getString(R.string.nothing_to_copied))
    }
}