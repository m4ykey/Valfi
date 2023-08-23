package com.example.vuey.feature_movie.data.local.converter

import androidx.room.TypeConverter
import com.example.vuey.feature_movie.data.local.entity.MovieEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MovieConverter {

    @TypeConverter
    fun toGenreJson(genres : List<MovieEntity.GenreEntity>) : String {
        return Gson().toJson(genres)
    }

    @TypeConverter
    fun fromGenreJson(json : String) : List<MovieEntity.GenreEntity> {
        val type = object : TypeToken<List<MovieEntity.GenreEntity>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromSpokenLanguagesJson(json : String) : List<MovieEntity.SpokenLanguageEntity> {
        val type = object : TypeToken<List<MovieEntity.SpokenLanguageEntity>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toSpokenLanguagesJson(spokenLanguages : List<MovieEntity.SpokenLanguageEntity>) : String {
        return Gson().toJson(spokenLanguages)
    }
}