package com.m4ykey.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.m4ykey.core.views.BaseViewModel
import com.m4ykey.data.domain.repository.LyricsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LyricsViewModel @Inject constructor(
    private val repository: LyricsRepository
) : BaseViewModel() {

    private val _songLyrics = MutableStateFlow("")
    val songLyrics : StateFlow<String> get() = _songLyrics

    fun fetchSongLyrics(artist : String, song : String) {
        viewModelScope.launch {
            _songLyrics.value = withContext(Dispatchers.IO) {
                repository.searchLyrics(artist, song)
            }
        }
    }

}