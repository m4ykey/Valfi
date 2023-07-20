package com.example.vuey.feature_music_player.domain.use_case

import com.example.vuey.feature_music_player.data.remote.model.name.YoutubeSearchItems
import com.example.vuey.feature_music_player.domain.repository.YoutubeRepository
import com.example.vuey.util.network.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class YoutubeMusicNameUseCase @Inject constructor(
    private val repository: YoutubeRepository
) {
    suspend operator fun invoke(videoName : String) : Flow<Resource<List<YoutubeSearchItems>>> {
        return repository.getYoutubeVideoByName(videoName = videoName)
    }
}