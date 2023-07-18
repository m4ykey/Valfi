package com.example.vuey.feature_music_player.data.repository

import com.example.vuey.feature_album.data.remote.api.YoutubeApi
import com.example.vuey.feature_music_player.domain.repository.YoutubeRepository
import javax.inject.Inject

class YoutubeRepositoryImpl @Inject constructor(
    private val youtubeApi: YoutubeApi
) : YoutubeRepository {
}