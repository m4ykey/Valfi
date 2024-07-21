package com.m4ykey.data.local.database.converter

import androidx.room.TypeConverter
import com.m4ykey.data.local.model.ArtistEntity
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {

    private val moshi by lazy {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }
    private val type = Types.newParameterizedType(List::class.java, ArtistEntity::class.java)
    private val adapter by lazy {
        moshi.adapter<List<ArtistEntity>>(type).lenient()
    }
    private val singleAdapter by lazy {
        moshi.adapter(ArtistEntity::class.java).lenient()
    }

    @TypeConverter
    fun fromArtistEntityList(artists : List<ArtistEntity>) : String {
        return adapter.toJson(artists)
    }

    @TypeConverter
    fun toArtistEntityList(artistsJson : String) : List<ArtistEntity> {
        return try {
            adapter.fromJson(artistsJson) ?: emptyList()
        } catch (e: JsonDataException) {
            singleAdapter.fromJson(artistsJson)?.let { listOf(it) } ?: emptyList()
        }
    }

}