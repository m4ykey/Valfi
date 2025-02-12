package com.m4ykey.ui.artist

import androidx.lifecycle.viewModelScope
import com.m4ykey.core.views.BaseViewModel
import com.m4ykey.data.domain.model.album.Artist
import com.m4ykey.data.domain.repository.ArtistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val repository: ArtistRepository
) : BaseViewModel() {

    private val _artists = MutableStateFlow<List<Artist>>(emptyList())
    val artists : StateFlow<List<Artist>> = _artists

    fun loadArtists(id : String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repository.getSeveralArtists(id).collect { artist ->
                    _artists.value = artist
                }
            } catch (e : Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

}