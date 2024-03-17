package com.m4ykey.data.local.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.IsAlbumSaved
import com.m4ykey.data.local.model.IsListenLaterSaved

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
