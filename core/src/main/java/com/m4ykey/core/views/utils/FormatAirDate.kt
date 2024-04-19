package com.m4ykey.core.views.utils

import java.time.Year
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatAirDate(airDate: String?): String? {
    return airDate?.let {
        val inputFormatter = if (it.length > 4) DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
        else DateTimeFormatter.ofPattern("yyyy", Locale.getDefault())

        val outputFormatter = DateTimeFormatter.ofPattern("yyyy", Locale.getDefault())

        try {
            val temporalAccessor = inputFormatter.parse(it)
            val year = Year.from(temporalAccessor)
            outputFormatter.format(year)
        } catch (e: Exception) {
            null
        }
    }
}
