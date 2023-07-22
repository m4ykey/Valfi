package com.example.vuey.feature_music_player.data.remote.model.name

data class MusicName(
    val etag: String,
    val items: List<MusicItem>,
    val kind: String,
    val nextPageToken: String,
    val pageInfo: PageInfo,
    val regionCode: String
)