package com.m4ykey.authentication.interceptor

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.m4ykey.core.BuildConfig.SPOTIFY_CLIENT_ID
import com.m4ykey.core.BuildConfig.SPOTIFY_CLIENT_SECRET
import com.m4ykey.authentication.api.AuthApi
import com.m4ykey.authentication.interceptor.token.TokenProvider
import com.m4ykey.authentication.interceptor.token.fetchAccessToken
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class SpotifyTokenProvider @Inject constructor(
    private val api: AuthApi,
    private val dataStore: DataStore<Preferences>,
    private val context : Context
) : TokenProvider {

    private val accessTokenKey = stringPreferencesKey("access_token")
    private val expireKey = longPreferencesKey("expire_token")

    override suspend fun getAccessToken(): String? {
        return try {
            val dataStoreData = dataStore.data.firstOrNull() ?: return ""
            val cachedToken = dataStoreData[accessTokenKey]
            val expireTime = dataStoreData[expireKey] ?: 0L

            if (!cachedToken.isNullOrBlank() && System.currentTimeMillis() < expireTime) {
                cachedToken
            } else {
                if (isInternetAvailable(context)) {
                    val newAccessToken = fetchAccessToken(
                        clientId = SPOTIFY_CLIENT_ID,
                        clientSecret = SPOTIFY_CLIENT_SECRET,
                        api = api
                    )

                    val newExpireTime = System.currentTimeMillis() + 3600 * 1000
                    saveAccessToken(newAccessToken, newExpireTime)
                    newAccessToken
                } else {
                    ""
                }
            }
        } catch (e : Exception) {
            ""
        }
    }

    private suspend fun saveAccessToken(token : String, expireTime : Long) {
        dataStore.edit { preferences ->
            preferences[accessTokenKey] = token
            preferences[expireKey] = expireTime
        }
    }
}