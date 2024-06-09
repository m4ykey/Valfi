package com.m4ykey.core.views

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

fun formatDate(date : String?, inputPattern : String, outputPattern : String) : String? {
    return date?.takeIf { it.isNotEmpty() }?.let {
        try {
            val inputFormatter = DateTimeFormatter.ofPattern(inputPattern, Locale.getDefault())
            val outputFormatter = DateTimeFormatter.ofPattern(outputPattern, Locale.getDefault())

            val parsedDate = LocalDateTime.parse(it, inputFormatter)
            outputFormatter.format(parsedDate)
        } catch (e : DateTimeParseException) {
            e.printStackTrace()
            null
        }
    }
}