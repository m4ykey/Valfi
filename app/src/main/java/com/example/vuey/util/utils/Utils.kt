package com.example.vuey.util.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Locale

fun Double.formatVoteAverage(): String = String.format("%.1f", this)

object DateUtils {
    fun formatAirDate(airDate: String?): String? {
        return airDate?.let {
            val inputDateFormat = if (it.length > 4) SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ) else SimpleDateFormat("yyyy", Locale.getDefault())
            val outputDateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
            val date = try {
                inputDateFormat.parse(it)
            } catch (e: Exception) {
                null
            }
            date?.let { d ->
                outputDateFormat.format(d)
            }
        }
    }
}

fun showSnackbar(view: View, message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(view, message, duration).show()
}