package com.m4ykey.ui

import androidx.lifecycle.viewModelScope
import com.m4ykey.core.views.BaseViewModel
import com.m4ykey.data.domain.model.Artist
import com.m4ykey.data.domain.repository.ArtistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val artistRepository: ArtistRepository
) : BaseViewModel() {

    private var _artist = MutableStateFlow<Artist?>(null)
    val artist : StateFlow<Artist?> get() = _artist

    fun getArtist(artistId : String) = viewModelScope.launch {
        runFlow(
            flow = artistRepository.getArtist(artistId),
            resultState = _artist
        )
    }

}