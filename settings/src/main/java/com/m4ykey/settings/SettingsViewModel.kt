package com.m4ykey.settings

import androidx.lifecycle.ViewModel
import com.m4ykey.data.domain.repository.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: AlbumRepository
) : ViewModel() {



}