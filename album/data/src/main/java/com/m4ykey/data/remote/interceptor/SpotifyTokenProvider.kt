package com.m4ykey.data.remote.interceptor

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.m4ykey.data.BuildConfig.SPOTIFY_CLIENT_ID
import com.m4ykey.data.BuildConfig.SPOTIFY_CLIENT_SECRET
import com.m4ykey.data.remote.api.AuthApi
import com.m4ykey.data.remote.interceptor.token.TokenProvider
import com.m4ykey.data.remote.interceptor.token.fetchAccessToken
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SpotifyTokenProvider @Inject constructor(
    private val api : AuthApi,
    private val dataStore : DataStore<Preferences>
) : TokenProvider {

    private val accessTokenKey = stringPreferencesKey("access_token")
    private val expireKey = longPreferencesKey("expire_token")

    override suspend fun getAccessToken(): String {
        val cachedToken = dataStore.data.first()[accessTokenKey]
        val expireTime = dataStore.data.first()[expireKey] ?: 0L

        return if (!cachedToken.isNullOrBlank() && System.currentTimeMillis() < expireTime) {
            cachedToken
        } else {
            val newAccessToken = fetchAccessToken(
                api = api,
                clientSecret = SPOTIFY_CLIENT_SECRET,
                clientId = SPOTIFY_CLIENT_ID
            )

            val newExpireTime = System.currentTimeMillis() + 3600 * 1000
            saveAccessToken(newAccessToken, newExpireTime)
            newAccessToken
        }
    }

    private suspend fun saveAccessToken(token : String, time : Long) {
        dataStore.edit { preferences ->
            preferences[accessTokenKey] = token
            preferences[expireKey] = time
        }
    }
}