package com.m4ykey.core.views.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.m4ykey.core.R

fun copyText(text: String, context : Context, label : String? = null) {
    if (text.isNotBlank()) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val data = ClipData.newPlainText(null, text)
        clipboard.setPrimaryClip(data)
        label?.takeIf { it.isNotBlank() }?.let {
            showToast(context, it)
        }
    } else {
        showToast(context, context.getString(R.string.nothing_to_copied))
    }
}