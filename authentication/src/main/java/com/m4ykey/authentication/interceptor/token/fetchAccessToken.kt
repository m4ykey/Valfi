package com.m4ykey.authentication.interceptor.token

import com.m4ykey.authentication.api.AuthApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun fetchAccessToken(
    api: AuthApi,
    clientId : String,
    clientSecret : String
) : String {
    return try {
        withContext(Dispatchers.IO) {
            val response = api.getAccessToken(
                token = generateToken(clientId, clientSecret),
                grantType = "client_credentials"
            )
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