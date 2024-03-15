package com.m4ykey.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class AlbumWithStates(
    @Embedded val album : AlbumEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "albumId"
    )
    val isAlbumSaved : IsAlbumSaved?,
    @Relation(
        parentColumn = "id",
        entityColumn = "albumId"
    )
    val isListenLaterSaved : IsListenLaterSaved?
)
