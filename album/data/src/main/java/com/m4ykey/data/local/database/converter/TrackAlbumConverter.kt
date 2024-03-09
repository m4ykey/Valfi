package com.m4ykey.data.local.database.converter

import androidx.room.TypeConverter
import com.m4ykey.data.domain.model.album.Artist
import com.m4ykey.data.domain.model.album.ExternalUrls
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type

class TrackAlbumConverter {

    private val moshi: Moshi = Moshi.Builder().build()

    @TypeConverter
    fun fromJsonArtist(value: String): List<Artist>? {
        val type: Type = Types.newParameterizedType(List::class.java, Artist::class.java)
        val adapter: JsonAdapter<List<Artist>> = moshi.adapter(type)
        return adapter.fromJson(value)
    }

    @TypeConverter
    fun toJsonArtist(value: List<Artist>?): String {
        val type: Type = Types.newParameterizedType(List::class.java, Artist::class.java)
        val adapter: JsonAdapter<List<Artist>> = moshi.adapter(type)
        return adapter.toJson(value)
    }

    @TypeConverter
    fun fromJson(value: String): ExternalUrls? {
        val adapter: JsonAdapter<ExternalUrls> = moshi.adapter(ExternalUrls::class.java)
        return adapter.fromJson(value)
    }

    @TypeConverter
    fun toJson(value: ExternalUrls?): String {
        val adapter: JsonAdapter<ExternalUrls> = moshi.adapter(ExternalUrls::class.java)
        return adapter.toJson(value)
    }

}