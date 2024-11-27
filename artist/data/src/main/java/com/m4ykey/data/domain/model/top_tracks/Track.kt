package com.m4ykey.data.domain.model.top_tracks

data class Track(
    val durationMs : Int,
    val album : Album,
    val explicit : Boolean,
    val name: String
)
