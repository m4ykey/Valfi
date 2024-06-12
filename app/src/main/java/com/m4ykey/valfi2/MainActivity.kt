package com.m4ykey.valfi2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
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
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        findViewById<BottomNavigationView>(R.id.bottomNavigation)?.setupWithNavController(navController)
    }

    override fun showBottomNavigation() {
        findViewById<BottomNavigationView>(R.id.bottomNavigation)?.isVisible = true
    }

    override fun hideBottomNavigation() {
        findViewById<BottomNavigationView>(R.id.bottomNavigation)?.isVisible = false
    }
}