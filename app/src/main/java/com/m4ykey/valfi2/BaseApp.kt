package com.m4ykey.valfi2

import android.app.Application
import com.m4ykey.core.timberSetup
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()

        timberSetup()
    }
}