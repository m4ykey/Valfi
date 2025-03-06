package com.m4ykey.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "copyright_table")
data class CopyrightEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "text") val text : String
)
