package com.m4ykey.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ColorViewModel : ViewModel() {

    private var _selectedColor = MutableLiveData<Int>()
    val selectedColor : LiveData<Int> get() = _selectedColor

    fun setColor(color : Int) {
        _selectedColor.value = color
    }

}