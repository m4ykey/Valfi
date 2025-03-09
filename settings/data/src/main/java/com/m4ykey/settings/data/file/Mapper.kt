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
    val artists = (map["artists"] as? List<*>)?.mapNotNull { artistMap ->
        if (artistMap is Map<*,*>) {
            convertMapToArtistEntity(artistMap)
        } else null
    } ?: emptyList()
    val copyrights = (map["copyrights"] as? List<*>)?.mapNotNull { copyrightMap ->
        if (copyrightMap is Map<*,*>) {
            convertMapToCopyrightsEntity(copyrightMap)
        } else null
    } ?: emptyList()
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

fun convertMapToCopyrightsEntity(map: Map<*,*>) : CopyrightEntity {
    val text = map["text"] as? String ?: ""
    return CopyrightEntity(
        text = text
    )
}

fun convertMapToArtistEntity(map: Map<*, *>): ArtistEntity {
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