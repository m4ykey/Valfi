package com.m4ykey.core.views

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatAirDate(airDate: String?): String? {
    return airDate?.let {
        val inputFormatter = if (it.length > 4) DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
        else DateTimeFormatter.ofPattern("yyyy", Locale.getDefault())

        val outputFormatter = DateTimeFormatter.ofPattern("yyyy", Locale.getDefault())

        try {
            val date = LocalDate.parse(it, inputFormatter)
            outputFormatter.format(date)
        } catch (e: Exception) {
            null
        }
    }
}