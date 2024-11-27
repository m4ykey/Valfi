package com.m4ykey.data.domain.model.top_tracks

import com.m4ykey.data.domain.model.Image

data class Album(
    val images : List<Image>,
    val id : String
)
