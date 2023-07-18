package com.example.vuey.feature_music_player.domain.use_case

import com.example.vuey.feature_music_player.domain.repository.YoutubeRepository
import javax.inject.Inject

class YoutubeVideoUseCase @Inject constructor(
    private val repository: YoutubeRepository
) {



}