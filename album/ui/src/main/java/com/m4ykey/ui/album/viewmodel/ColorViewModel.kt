package com.m4ykey.ui.album.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ColorViewModel : ViewModel() {

    private var _selectedColor = MutableStateFlow<Int?>(null)
    val selectedColor = _selectedColor.asStateFlow()

    fun setColor(color : Int) {
        _selectedColor.value = color
    }

}