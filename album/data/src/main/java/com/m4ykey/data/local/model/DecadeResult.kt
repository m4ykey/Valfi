package com.m4ykey.data.local.model

import androidx.room.ColumnInfo

data class DecadeResult(
    @ColumnInfo(name = "decade") val decade: Int,
    @ColumnInfo(name = "album_count") val albumCount : Int
)
