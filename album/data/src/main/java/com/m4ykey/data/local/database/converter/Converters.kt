package com.m4ykey.data.local.database.converter

import androidx.room.TypeConverter
import com.m4ykey.data.local.model.ArtistEntity
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val type = Types.newParameterizedType(List::class.java, ArtistEntity::class.java)
    val adapter = moshi.adapter<List<ArtistEntity>>(type).lenient()
    val singleAdapter = moshi.adapter(ArtistEntity::class.java).lenient()

    @TypeConverter
    fun fromArtistEntityList(artists : List<ArtistEntity>) : String {
        return adapter.toJson(artists)
    }

    @TypeConverter
    fun toArtistEntityList(artistsJson : String) : List<ArtistEntity> {
        return try {
            adapter.fromJson(artistsJson) ?: emptyList()
        } catch (e: JsonDataException) {
            val singleArtist = singleAdapter.fromJson(artistsJson)
            if (singleArtist != null) listOf(singleArtist) else emptyList()
        }
    }

}