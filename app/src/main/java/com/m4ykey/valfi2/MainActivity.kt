package com.m4ykey.valfi2

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigationrail.NavigationRailView
import com.m4ykey.core.network.NetworkMonitor
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.valfi2.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BottomNavigationVisibility {

    private lateinit var binding: ActivityMainBinding
    private lateinit var networkMonitor: NetworkMonitor

    private var isInternetAvailable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        networkMonitor = NetworkMonitor(this)
        setupNavigation()

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.layoutHorizontal.isVisible = true
            binding.layoutvertical.isVisible = false
        } else {
            binding.layoutHorizontal.isVisible = false
            binding.layoutvertical.isVisible = true
        }
    }

    override fun onStart() {
        super.onStart()
        networkMonitor.startMonitoring()
        observeInternetConnection()
    }

    override fun onStop() {
        super.onStop()
        networkMonitor.stopMonitoring()
    }

    private fun observeInternetConnection() {
        lifecycleScope.launch {
            networkMonitor.isInternetAvailable.collect { available ->
                isInternetAvailable = available
                updateTextViewBasedOnInternet()
            }
        }
    }

    private fun updateTextViewBasedOnInternet() {
        if (isInternetAvailable) {
            binding.txtNoInternetH.isVisible = false
            binding.txtNoInternetV.isVisible = false
        } else {
            binding.txtNoInternetH.isVisible = true
            binding.txtNoInternetV.isVisible = true
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