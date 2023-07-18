package com.example.vuey.feature_music_player.domain.use_case

data class YoutubeUseCase(
    val getYoutubeVideoUseCase : YoutubeVideoUseCase,
    val getYoutubeMusicNameUseCase : YoutubeMusicNameUseCase
)