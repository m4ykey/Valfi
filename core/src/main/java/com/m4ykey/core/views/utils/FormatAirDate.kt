package com.m4ykey.core.views.utils

import java.time.Year
import java.time.format.DateTimeFormatter
import java.util.Locale

private val datePatterns = listOf("yyyy-MM-dd", "yyyy/MM/dd", "yyyy.MM.dd", "yyyy")

fun formatAirDate(airDate: String?): String? {
    if (airDate.isNullOrBlank()) return null

    for (pattern in datePatterns) {
        try {
            val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
            val parsed = formatter.parse(airDate)
            val year = Year.from(parsed)
            return year.toString()
        } catch (_ : Exception) {
            continue
        }
    }

    return null
}
