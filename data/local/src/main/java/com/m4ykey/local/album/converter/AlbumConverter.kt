package com.m4ykey.local.album.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.m4ykey.local.album.entity.AlbumEntity
import com.m4ykey.local.album.entity.ListenLaterEntity
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

@ProvidedTypeConverter
class AlbumConverter(private val moshi : Moshi) {

    @TypeConverter
    fun fromArtistJson(json : String) : List<AlbumEntity.ArtistEntity> {
        val type = Types.newParameterizedType(List::class.java, AlbumEntity.ArtistEntity::class.java)
        return moshi.adapter<List<AlbumEntity.ArtistEntity>>(type).fromJson(json) ?: emptyList()
    }

    @TypeConverter
    fun toArtistJson(data : List<AlbumEntity.ArtistEntity>) : String {
        return moshi.adapter<List<AlbumEntity.ArtistEntity>>(
            Types.newParameterizedType(List::class.java, AlbumEntity.ArtistEntity::class.java)
        ).toJson(data)
    }

    @TypeConverter
    fun fromExternalUrlsJson(json : String) : AlbumEntity.ExternalUrlsEntity? {
        return moshi.adapter(AlbumEntity.ExternalUrlsEntity::class.java).fromJson(json)
    }

    @TypeConverter
    fun toExternalUrlsJson(data: AlbumEntity.ExternalUrlsEntity) : String {
        return moshi.adapter(AlbumEntity.ExternalUrlsEntity::class.java).toJson(data)
    }

    @TypeConverter
    fun fromImageJson(json : String) : AlbumEntity.ImageEntity? {
        return moshi.adapter(AlbumEntity.ImageEntity::class.java).fromJson(json)
    }

    @TypeConverter
    fun toImageJson(data : AlbumEntity.ImageEntity) : String {
        return moshi.adapter(AlbumEntity.ImageEntity::class.java).toJson(data)
    }

    @TypeConverter
    fun fromImageListenLaterJson(json : String) : ListenLaterEntity.ListenLaterImage? {
        return moshi.adapter(ListenLaterEntity.ListenLaterImage::class.java).fromJson(json)
    }

    @TypeConverter
    fun toImageListenLaterJson(data : ListenLaterEntity.ListenLaterImage) : String {
        return moshi.adapter(ListenLaterEntity.ListenLaterImage::class.java).toJson(data)
    }

    @TypeConverter
    fun fromTrackJson(json : String) : List<AlbumEntity.TrackListEntity> {
        val type = Types.newParameterizedType(List::class.java, AlbumEntity.TrackListEntity::class.java)
        return moshi.adapter<List<AlbumEntity.TrackListEntity>>(type).fromJson(json) ?: emptyList()
    }

    @TypeConverter
    fun toTrackJson(data : List<AlbumEntity.TrackListEntity>) : String {
        return moshi.adapter<List<AlbumEntity.TrackListEntity>>(
            Types.newParameterizedType(List::class.java, AlbumEntity.TrackListEntity::class.java)
        ).toJson(data)
    }

}