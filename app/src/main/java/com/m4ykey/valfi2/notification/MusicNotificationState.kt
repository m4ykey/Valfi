package com.m4ykey.valfi2.notification

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object MusicNotificationState {

    private val _title = MutableStateFlow<String?>(null)
    val title : StateFlow<String?> get() = _title

    private val _artist = MutableStateFlow<String?>(null)
    val artist : StateFlow<String?> get() = _artist

    fun updateArtist(info : String) {
        _artist.value = info
    }

    fun updateTitle(info : String) {
        _title.value = info
    }

}