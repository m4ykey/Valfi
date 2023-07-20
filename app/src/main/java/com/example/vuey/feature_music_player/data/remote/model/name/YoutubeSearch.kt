package com.example.vuey.feature_music_player.data.remote.model.name

data class YoutubeSearch(
    val etag: String,
    val items: List<YoutubeSearchItems>,
    val kind: String,
    val nextPageToken: String,
    val pageInfo: PageInfo,
    val regionCode: String
)