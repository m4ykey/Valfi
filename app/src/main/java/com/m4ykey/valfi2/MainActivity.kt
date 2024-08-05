package com.m4ykey.valfi2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.settings.theme.ThemeOptions
import com.m4ykey.settings.theme.ThemePreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BottomNavigationVisibility {

    @Inject
    lateinit var themePreferences : ThemePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        findViewById<BottomNavigationView>(R.id.bottomnavigation)?.setupWithNavController(navController)
    }

    override fun showBottomNavigation() {
        findViewById<BottomNavigationView>(R.id.bottomnavigation)?.isVisible = true
    }

    override fun hideBottomNavigation() {
        findViewById<BottomNavigationView>(R.id.bottomnavigation)?.isVisible = false
    }

    private fun readSelectedThemeOption() {
        lifecycleScope.launch {
            val themeOption = themePreferences.getSelectedThemeOptions(this@MainActivity)
            when (themeOption) {
                ThemeOptions.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                ThemeOptions.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                ThemeOptions.DEFAULT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                null -> {}
            }
        }
    }

    override fun onStart() {
        super.onStart()
        readSelectedThemeOption()
    }
}