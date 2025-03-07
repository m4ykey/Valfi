package com.m4ykey.settings.data.file

import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.ArtistEntity
import com.m4ykey.data.local.model.CopyrightEntity

fun convertMapToAlbumEntity(map: Map<String, Any>): AlbumEntity {
    val id = map["id"] as? String ?: ""
    val name = map["name"] as? String ?: ""
    val albumType = map["albumType"] as? String ?: ""
    val albumUrl = map["albumUrl"] as? String ?: ""
    val releaseDate = map["releaseDate"] as? String ?: ""
    val images = map["images"] as? String ?: ""
    val totalTracks = (map["totalTracks"] as? Double)?.toInt() ?: 0
    val artistsMapList = map["artists"] as? List<Map<String, Any>> ?: emptyList()
    val artists = artistsMapList.map { convertMapToArtistEntity(it) }
    val copyrightsMapList = map["copyrights"] as? List<Map<String, Any>> ?: emptyList()
    val copyrights = copyrightsMapList.map { convertMapToCopyrightsEntity(it) }
    return AlbumEntity(
        id = id,
        name = name,
        albumType = albumType,
        albumUrl = albumUrl,
        releaseDate = releaseDate,
        totalTracks = totalTracks,
        images = images,
        artists = artists,
        copyrights = copyrights
    )
}

fun convertMapToCopyrightsEntity(map: Map<String, Any>) : CopyrightEntity {
    val text = map["text"] as? String ?: ""
    return CopyrightEntity(
        text = text
    )
}

fun convertMapToArtistEntity(map: Map<String, Any>): ArtistEntity {
    val albumId = map["albumId"] as? String ?: ""
    val artistId = map["artistId"] as? String ?: ""
    val name = map["name"] as? String ?: ""
    val url = map["url"] as? String ?: ""
    return ArtistEntity(
        albumId = albumId,
        artistId = artistId,
        name = name,
        url = url
    )
}