package com.m4ykey.data.remote.interceptor

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.m4ykey.core.BuildConfig.SPOTIFY_CLIENT_ID
import com.m4ykey.core.BuildConfig.SPOTIFY_CLIENT_SECRET
import com.m4ykey.data.remote.api.AuthApi
import com.m4ykey.data.remote.interceptor.token.TokenProvider
import com.m4ykey.data.remote.interceptor.token.fetchAccessToken
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class SpotifyTokenProvider @Inject constructor(
    private val api : AuthApi,
    private val dataStore : DataStore<Preferences>,
    private val context : Context
) : TokenProvider {

    private val accessTokenKey = stringPreferencesKey("access_token")
    private val expireKey = longPreferencesKey("expire_token")

    override suspend fun getAccessToken(): String {
        try {
            val dataStoreData = dataStore.data.firstOrNull() ?: return ""
            val cachedToken = dataStoreData[accessTokenKey]
            val expireTime = dataStoreData[expireKey] ?: 0L

            if (!cachedToken.isNullOrBlank() && System.currentTimeMillis() < expireTime) {
                return cachedToken
            } else {
                val newAccessToken = fetchAccessToken(
                    api = api,
                    clientSecret = SPOTIFY_CLIENT_SECRET,
                    clientId = SPOTIFY_CLIENT_ID,
                    context = context
                )

                val newExpireTime = System.currentTimeMillis() + 3600 * 1000
                saveAccessToken(newAccessToken, newExpireTime)
                return newAccessToken
            }
        } catch (e : Exception) {
            throw RuntimeException("Error fetching access token", e)
        }
    }

    private suspend fun saveAccessToken(token : String, expireTime : Long) {
        dataStore.edit { preferences ->
            preferences[accessTokenKey] = token
            preferences[expireKey] = expireTime
        }
    }
}