package com.m4ykey.valfi2.notification

import com.m4ykey.valfi2.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object MusicNotificationState {

    private val _title = MutableStateFlow<String?>(null)
    val title : StateFlow<String?> get() = _title

    private val _artist = MutableStateFlow<String?>(null)
    val artist : StateFlow<String?> get() = _artist

    private val _backgroundColor = MutableStateFlow(R.color.white)
    val backgroundColor : StateFlow<Int?> get() = _backgroundColor

    private val _strokeColor = MutableStateFlow(R.color.gray)
    val strokeColor : StateFlow<Int?> get() = _strokeColor

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