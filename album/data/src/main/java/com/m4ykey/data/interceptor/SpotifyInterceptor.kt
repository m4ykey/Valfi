package com.m4ykey.data.interceptor

import android.util.Base64
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.m4ykey.data.BuildConfig
import com.m4ykey.data.remote.api.AuthApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class SpotifyInterceptor @Inject constructor(
    private val api : AuthApi,
    private val dataStore : DataStore<Preferences>
) : Interceptor {

    private val accessTokenKey = stringPreferencesKey("access_token")
    private val expireKey = longPreferencesKey("expire_token")

    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        val request = chain.request()

        var accessToken : String?
        var expireTime : Long

        withContext(Dispatchers.IO) {
            dataStore.data.first().let { preferences ->
                accessToken = preferences[accessTokenKey]
                expireTime = preferences[expireKey] ?: 0L
            }
        }

        if (accessToken == null || System.currentTimeMillis() > expireTime) {
            accessToken = getAccessToken()
            expireTime = System.currentTimeMillis() + 3600 * 1000
            saveAccessToken(accessToken!!, expireTime)
        } else if (System.currentTimeMillis() > expireTime) {
            withContext(Dispatchers.IO) {
                dataStore.edit { preferences ->
                    preferences.remove(accessTokenKey)
                    preferences.remove(expireKey)
                }
            }
        }

        val newRequest = request.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        chain.proceed(newRequest)

    }

    suspend fun getAccessToken() : String {
        val authHeader = "Basic " + Base64.encodeToString(
            "${BuildConfig.SPOTIFY_CLIENT_ID}:${BuildConfig.SPOTIFY_CLIENT_SECRET}".toByteArray(),
            Base64.NO_WRAP
        )
        Log.i("AccessToken", "getAccessToken: ${api.getAccessToken(authHeader).accessToken}")

        return api.getAccessToken(authHeader).accessToken
    }

    private suspend fun saveAccessToken(token : String, time : Long) {
        dataStore.edit { preferences ->
            preferences[accessTokenKey] = token
            preferences[expireKey] = time
        }
    }
}