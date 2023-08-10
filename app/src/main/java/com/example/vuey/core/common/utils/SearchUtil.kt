package com.example.vuey.core.common.utils

import com.example.vuey.feature_album.data.remote.model.spotify.album.Album
import java.util.Locale

object SearchUtil {
    fun calculateAlbumMatchingScore(album: Album, query : String) : Double {
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
}