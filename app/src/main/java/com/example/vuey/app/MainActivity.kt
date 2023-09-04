package com.example.vuey.app

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.vuey.R
import com.example.vuey.databinding.ActivityMainBinding
import com.m4ykey.common.network.NetworkStateMonitor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var connectivityManager: ConnectivityManager
    private val networkStateMonitor: NetworkStateMonitor by lazy {
        NetworkStateMonitor(connectivityManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkStateMonitor.startMonitoring()

        with(binding) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
            val navController = navHostFragment.navController
            bottomNavigation.setupWithNavController(navController)

            navController.addOnDestinationChangedListener { _, destination, _ ->
                val fragmentsWithHiddenBottomNavigation = setOf(
                    R.id.albumDetailFragment,
                    R.id.searchAlbumFragment,
                    R.id.albumStatisticsFragment,
                    R.id.albumListenLaterFragment,
                    R.id.detailMovieFragment,
                    R.id.searchMovieFragment,
                    R.id.movieStatisticsFragment,
                    R.id.movieWatchLaterFragment
                )

                bottomNavigation.visibility = if (destination.id in fragmentsWithHiddenBottomNavigation) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }

            lifecycleScope.launch {
                networkStateMonitor.isInternetAvailable.collect { isAvailable ->
                    if (isAvailable) {
                        txtNoInternet.visibility = View.GONE
                    } else {
                        txtNoInternet.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkStateMonitor.stopMonitoring()
    }
}
