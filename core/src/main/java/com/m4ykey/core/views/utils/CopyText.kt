package com.m4ykey.core.views.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.m4ykey.core.R

fun copyText(text: String, context : Context, label : String) {
    if (text.isNotBlank()) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val data = ClipData.newPlainText(null, text)
        clipboard.setPrimaryClip(data)
        showToast(context, label)
    } else {
        showToast(context, context.getString(R.string.nothing_to_copied))
    }
}