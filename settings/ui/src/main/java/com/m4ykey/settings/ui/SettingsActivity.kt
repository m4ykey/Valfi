package com.m4ykey.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.m4ykey.core.views.BottomNavigationVisibility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }
}