package com.m4ykey.valfi2.notification

import com.m4ykey.valfi2.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object MusicNotificationState {

    private val _title = MutableStateFlow<String?>(null)
    val title = _title.asStateFlow()

    private val _artist = MutableStateFlow<String?>(null)
    val artist = _artist.asStateFlow()

    private val _backgroundColor = MutableStateFlow(R.color.white)
    val backgroundColor = _backgroundColor.asStateFlow()

    private val _strokeColor = MutableStateFlow(R.color.gray)
    val strokeColor = _strokeColor.asStateFlow()

    fun updateStrokeColor(colorRes: Int) {
        _strokeColor.value = colorRes
    }

    fun updateBackgroundColor(colorRes : Int) {
        _backgroundColor.value = colorRes
    }

    fun updateArtist(info : String) {
        _artist.value = info
    }

    fun updateTitle(info : String) {
        _title.value = info
    }

}