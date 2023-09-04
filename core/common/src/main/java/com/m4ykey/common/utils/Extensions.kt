package com.m4ykey.common.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.m4ykey.remote.album.model.spotify.album.AlbumList
import com.m4ykey.remote.movie.model.MovieList
import java.text.SimpleDateFormat
import java.util.Locale

fun Double.formatVoteAverage(): String = String.format("%.1f", this)

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

fun showSnackbar(view: View, message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(view, message, duration).show()
}

fun calculateAlbumMatchingScore(album: AlbumList, query: String): Double {
    val albumTitle = album.albumName.lowercase(Locale.ROOT)
    val queryLowercase = query.lowercase(Locale.ROOT)

    val maxLength = maxOf(albumTitle.length, queryLowercase.length)
    var matchingCharacters = 0

    for (i in 0 until minOf(albumTitle.length, queryLowercase.length)) {
        if (albumTitle[i] == queryLowercase[i]) {
            matchingCharacters++
        }
    }

    return matchingCharacters.toDouble() / maxLength
}

fun calculateMovieMatchingScore(movie: MovieList, query: String): Double {
    val movieTitle = movie.title.lowercase(Locale.ROOT)
    val queryLowercase = query.lowercase(Locale.ROOT)

    val maxLength = maxOf(movieTitle.length, queryLowercase.length)
    var matchingCharacters = 0

    for (i in 0 until minOf(movieTitle.length, queryLowercase.length)) {
        if (movieTitle[i] == queryLowercase[i]) {
            matchingCharacters++
        }
    }

    return matchingCharacters.toDouble() / maxLength
}

fun Fragment.hideBottomNavigation(@IdRes bottomNavigationView : Int) {
    val bottomNavigation = requireActivity().findViewById<BottomNavigationView>(bottomNavigationView)
    bottomNavigation.visibility = View.GONE
}

fun Fragment.showBottomNavigation(@IdRes bottomNavigationView : Int) {
    val bottomNavigation = requireActivity().findViewById<BottomNavigationView>(bottomNavigationView)
    bottomNavigation.visibility = View.VISIBLE
}