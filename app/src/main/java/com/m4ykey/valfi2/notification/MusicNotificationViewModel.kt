package com.m4ykey.valfi2.notification

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MusicNotificationViewModel : ViewModel() {

    private val _isNotificationAccessGranted = MutableLiveData<Boolean>()
    val isNotificationAccessGranted : LiveData<Boolean> get() = _isNotificationAccessGranted

    fun checkNotificationAccess(context : Context) {
        val isGranted = isNotificationListenerEnabled(context)
        _isNotificationAccessGranted.value = isGranted
    }

}