package com.m4ykey.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NetworkMonitor(context : Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    private val _isInternetAvailable = MutableStateFlow(false)
    val isInternetAvailable : StateFlow<Boolean> get() = _isInternetAvailable

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            _isInternetAvailable.value = true
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            _isInternetAvailable.value = false
        }
    }

    init {
        requireNotNull(connectivityManager) { "ConnectivityManager not available" }
    }

    fun stopMonitoring() {
        try {
            connectivityManager?.unregisterNetworkCallback(networkCallback)
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    fun startMonitoring() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        try {
            connectivityManager?.registerNetworkCallback(networkRequest, networkCallback)
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    fun isInternetAvailable() : Boolean {
        val network = connectivityManager?.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

}