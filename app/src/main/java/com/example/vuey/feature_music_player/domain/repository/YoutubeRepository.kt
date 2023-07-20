package com.example.vuey.feature_music_player.domain.repository

import com.example.vuey.feature_music_player.data.remote.model.name.YoutubeSearchItems
import com.example.vuey.util.network.Resource
import kotlinx.coroutines.flow.Flow

interface YoutubeRepository {

    suspend fun getYoutubeVideoByName(videoName : String) : Flow<Resource<List<YoutubeSearchItems>>>
}