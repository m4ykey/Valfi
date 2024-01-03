package com.m4ykey.valfi2.album.data.domain.model

data class Albums(
    val items : List<Item>,
    val limit : Int,
    val next : String,
    val offset : Int,
    val previous : String
)
