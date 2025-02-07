package com.m4ykey.authentication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "key_table")
data class KeyEntity(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    val clientId : String,
    val clientSecret : String
)
