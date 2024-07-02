package com.m4ykey.data.remote.interceptor.token

import com.m4ykey.data.remote.api.AuthApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun fetchAccessToken(clientId: String, clientSecret: String, api: AuthApi) : String {
    val token = generateToken(clientId, clientSecret)

    return try {
        withContext(Dispatchers.IO) {
            val response = api.getAccessToken(token = token)
            response.access_token ?: throw RuntimeException("Failed to fetch access token")
        }
    } catch (e: IOException) {
        throw RuntimeException("Network error occurred", e)
    } catch (e: HttpException) {
        throw RuntimeException("HTTP error occurred", e)
    } catch (e: Exception) {
        throw RuntimeException("Unexpected error occurred", e)
    }
}