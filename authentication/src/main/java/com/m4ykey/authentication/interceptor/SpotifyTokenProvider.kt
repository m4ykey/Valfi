package com.m4ykey.authentication.interceptor

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.m4ykey.authentication.api.AuthApi
import com.m4ykey.authentication.data.repository.KeyRepository
import com.m4ykey.authentication.interceptor.token.TokenProvider
import com.m4ykey.authentication.interceptor.token.fetchAccessToken
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class SpotifyTokenProvider @Inject constructor(
    private val api: AuthApi,
    private val dataStore: DataStore<Preferences>,
    private val context : Context,
    private val repository: KeyRepository
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
                    val keyEntity = repository.getKeys().firstOrNull()
                    val newAccessToken = fetchAccessToken(
                        api = api,
                        clientId = keyEntity?.clientId ?: return "",
                        clientSecret = keyEntity.clientSecret ?: return ""
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

    private suspend fun saveAccessToken(token : String?, expireTime : Long) {
        if (token != null) {
            dataStore.edit { preferences ->
                preferences[accessTokenKey] = token
                preferences[expireKey] = expireTime
            }
        }
    }
}