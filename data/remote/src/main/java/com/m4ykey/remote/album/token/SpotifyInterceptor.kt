package com.m4ykey.remote.album.token

import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.m4ykey.remote.BuildConfig.SPOTIFY_CLIENT_ID
import com.m4ykey.remote.BuildConfig.SPOTIFY_CLIENT_SECRET
import com.m4ykey.remote.album.api.AuthApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class SpotifyInterceptor @Inject constructor(
    private val dataStore : DataStore<Preferences>,
    private val authApi: AuthApi
) : Interceptor {

    private val accessTokenKey = stringPreferencesKey("access_token")
    private val expireTimeKey = longPreferencesKey("expire_token")

    private var currentAccessToken : String? = null

    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        val request = chain.request()

        var accessToken : String?
        var expireTime: Long

        dataStore.data.first().let { preferences ->
            accessToken = preferences[accessTokenKey]
            expireTime = preferences[expireTimeKey] ?: 0L
        }

        if (accessToken == null || System.currentTimeMillis() > expireTime) {
            accessToken = getAccessToken()
            expireTime = System.currentTimeMillis() + 3600 * 1000 // 3600 seconds in milliseconds (1hour)
            saveAccessToken(accessToken!!, expireTime)
        } else if (System.currentTimeMillis() > expireTime) {
            // Token expired, remove the old token from datastore
            dataStore.edit { preferences ->
                preferences.remove(accessTokenKey)
                preferences.remove(expireTimeKey)
            }
            accessToken = getAccessToken()
        }

        currentAccessToken = accessToken

        val newRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        chain.proceed(newRequest)
    }

    private suspend fun saveAccessToken(accessToken: String, expireTime: Long) {
        dataStore.edit { preferences ->
            preferences[accessTokenKey] = accessToken
            preferences[expireTimeKey] = expireTime
        }
    }

    suspend fun getAccessToken(): String {
        val authHeader = "Basic " + Base64.encodeToString(
            "${SPOTIFY_CLIENT_ID}:${SPOTIFY_CLIENT_SECRET}".toByteArray(),
            Base64.NO_WRAP
        )

        return currentAccessToken ?: run {
            val response = authApi.getAccessToken(authHeader, "client_credentials")
            response.accessToken
        }
    }
}