package com.example.vuey.feature_album.data.remote.token

import android.content.SharedPreferences
import android.util.Base64
import androidx.core.content.edit
import com.example.vuey.BuildConfig.SPOTIFY_CLIENT_ID
import com.example.vuey.BuildConfig.SPOTIFY_CLIENT_SECRET
import com.example.vuey.feature_album.data.remote.api.AuthApi
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class SpotifyInterceptor @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val authApi: AuthApi
) : Interceptor {

    private val accessTokenKey = "access_token"
    private val expireTimeKey = "expire_time"

    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        val request = chain.request()

        var accessToken = sharedPreferences.getString(accessTokenKey, null)
        var expireTime = sharedPreferences.getLong(expireTimeKey, 0L)

        if (accessToken == null || System.currentTimeMillis() > expireTime) {
            accessToken = getAccessToken()
            expireTime = System.currentTimeMillis() + 3600 * 1000 // 3600 seconds in milliseconds (1hour)
            saveAccessToken(accessToken, expireTime)
        }

        val newRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        chain.proceed(newRequest)
    }

    private fun saveAccessToken(accessToken: String, expireTime: Long) {
        sharedPreferences.edit {
            putString(accessTokenKey, accessToken)
            putLong(expireTimeKey, expireTime)
        }
    }

    suspend fun getAccessToken(): String {
        val authHeader = "Basic " + Base64.encodeToString(
            "${SPOTIFY_CLIENT_ID}:${SPOTIFY_CLIENT_SECRET}".toByteArray(),
            Base64.NO_WRAP
        )

        val response = authApi.getAccessToken(authHeader, "client_credentials")

        return response.accessToken
    }
}