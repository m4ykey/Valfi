package com.m4ykey.local.movie.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.m4ykey.local.movie.entity.MovieEntity
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

@ProvidedTypeConverter
class MovieConverter(private val moshi : Moshi) {

    @TypeConverter
    fun toGenreJson(data : List<MovieEntity.GenreEntity>) : String {
        return moshi.adapter<List<MovieEntity.GenreEntity>>(
            Types.newParameterizedType(List::class.java, MovieEntity.GenreEntity::class.java)
        ).toJson(data)
    }

    @TypeConverter
    fun fromGenreJson(json : String) : List<MovieEntity.GenreEntity> {
        val type = Types.newParameterizedType(List::class.java, MovieEntity.GenreEntity::class.java)
        return moshi.adapter<List<MovieEntity.GenreEntity>>(type).fromJson(json) ?: emptyList()
    }

    @TypeConverter
    fun fromSpokenLanguagesJson(json : String) : List<MovieEntity.SpokenLanguageEntity> {
        val type = Types.newParameterizedType(List::class.java, MovieEntity.SpokenLanguageEntity::class.java)
        return moshi.adapter<List<MovieEntity.SpokenLanguageEntity>>(type).fromJson(json) ?: emptyList()
    }

    @TypeConverter
    fun toSpokenLanguagesJson(data : List<MovieEntity.SpokenLanguageEntity>) : String {
        return moshi.adapter<List<MovieEntity.SpokenLanguageEntity>>(
            Types.newParameterizedType(List::class.java, MovieEntity.SpokenLanguageEntity::class.java)
        ).toJson(data)
    }
}