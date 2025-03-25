package com.m4ykey.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stars_table")
data class StarsEntity(
    @PrimaryKey(autoGenerate = false) val albumId : String,
    val stars : Float
)
