package com.m4ykey.data.local.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.m4ykey.data.local.model.ArtistEntity
import com.m4ykey.data.local.model.CopyrightEntity

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromArtistList(artists : List<ArtistEntity>) : String {
        return gson.toJson(artists)
    }

    @TypeConverter
    fun toArtistList(artistsString : String) : List<ArtistEntity> {
        val type = object : TypeToken<List<ArtistEntity>>() {}.type
        return gson.fromJson(artistsString, type)
    }

    @TypeConverter
    fun fromCopyrightList(copyrights : List<CopyrightEntity>) : String {
        return gson.toJson(copyrights)
    }

    @TypeConverter
    fun toCopyrightList(copyrightsString : String) : List<CopyrightEntity> {
        val type = object : TypeToken<List<CopyrightEntity>>() {}.type
        return gson.fromJson(copyrightsString, type)
    }

}