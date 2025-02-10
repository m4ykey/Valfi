package com.m4ykey.data.local.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "search_result_table",
    indices = [Index(value = ["name"], unique = true)]
)
data class SearchResult(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    val name : String
)
