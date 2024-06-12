package com.m4ykey.valfi2

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigationrail.NavigationRailView
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.valfi2.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BottomNavigationVisibility {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.layoutHorizontal.isVisible = true
            binding.layoutVertical.isVisible = false
        } else {
            binding.layoutHorizontal.isVisible = false
            binding.layoutVertical.isVisible = true
        }
    }

    private fun setupNavigation() {
        val currentOrientation = resources.configuration.orientation
        val navHostFragment = if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_horizontal) as NavHostFragment
        } else {
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_vertical) as NavHostFragment
        }

        val navController = navHostFragment.navController

        findViewById<BottomNavigationView>(R.id.bottomNavigation)?.setupWithNavController(navController)
        findViewById<NavigationRailView>(R.id.navigationRailView)?.setupWithNavController(navController)
    }

    override fun showBottomNavigation() {
        findViewById<BottomNavigationView>(R.id.bottomNavigation)?.isVisible = true
        findViewById<NavigationRailView>(R.id.navigationRailView)?.isVisible = true
    }

    override fun hideBottomNavigation() {
        findViewById<BottomNavigationView>(R.id.bottomNavigation)?.isVisible = false
        findViewById<NavigationRailView>(R.id.navigationRailView)?.isVisible = false
    }
}