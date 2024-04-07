package com.m4ykey.core.views.utils

import android.content.Context
import android.widget.Toast

fun showToast(context : Context, message : String, duration : Int = Toast.LENGTH_SHORT) {
    return Toast.makeText(context, message, duration).show()
}