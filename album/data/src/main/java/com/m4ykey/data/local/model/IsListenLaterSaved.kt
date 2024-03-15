package com.m4ykey.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "listen_later_table")
data class IsListenLaterSaved(
    @PrimaryKey val albumId : String,
    val isListenLaterSaved : Boolean
)