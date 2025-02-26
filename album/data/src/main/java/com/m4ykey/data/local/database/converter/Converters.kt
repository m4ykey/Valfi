package com.m4ykey.data.local.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.m4ykey.data.local.model.ArtistEntity

class Converters {

    @TypeConverter
    fun fromArtistList(artists : List<ArtistEntity>) : String {
        return Gson().toJson(artists)
    }

    @TypeConverter
    fun toArtistList(artistsString : String) : List<ArtistEntity> {
        val type = object : TypeToken<List<ArtistEntity>>() {}.type
        return Gson().fromJson(artistsString, type)
    }

}