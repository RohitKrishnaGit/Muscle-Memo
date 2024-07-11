package com.cs346.musclememo.api

import com.cs346.musclememo.utils.AppPreferences
import com.cs346.musclememo.api.services.TokenRequest
import com.cs346.musclememo.api.services.TokenService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.util.concurrent.atomic.AtomicBoolean

class TokenAuthenticator: Authenticator {
    // AtomicBoolean in order to avoid race condition
    private var tokenRefreshInProgress: AtomicBoolean = AtomicBoolean(false)
    private var request: Request? = null

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            request = null

            // Checking if a token refresh call is already in progress or not
            // The first request will enter the if block
            // Later requests will enter the else block
            if (!tokenRefreshInProgress.get()) {
                tokenRefreshInProgress.set(true)
                // Refreshing token
                if(!refreshToken()) {
                    tokenRefreshInProgress.set(false)
                    return@runBlocking null
                }
                request = buildRequest(response.request().newBuilder())
                tokenRefreshInProgress.set(false)
            } else {
                // Waiting for the ongoing request to finish
                // So that we don't refresh our token multiple times
                waitForRefresh(response)
            }

            // return null to stop retrying once responseCount returns 3 or above.
            if (responseCount(response) >= 3) {
                null
            } else request
        }
    }

    // Refresh your token here and save them.
    private suspend fun refreshToken(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = TokenService.instance.getAuthentication(TokenRequest(AppPreferences.refreshToken!!)).execute()
                if(response.isSuccessful) {
                    AppPreferences.accessToken = response.body()?.let { it.accessToken }
                    return@withContext true
                } else {
                    // Invalid refresh token
                    AppPreferences.accessToken = null
                    AppPreferences.refreshToken = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                AppPreferences.accessToken = null
                AppPreferences.refreshToken = null
            }
            return@withContext false
        }
    }

    // Queuing the requests with delay
    private suspend fun waitForRefresh(response: Response) {
        while (tokenRefreshInProgress.get()) {
            delay(100)
        }
        request = buildRequest(response.request().newBuilder())
    }

    private fun responseCount(response: Response?): Int {
        var result = 1
        while (response?.priorResponse() != null && result <= 3) {
            result++
        }
        return result
    }

    // Build a new request with new access token
    private fun buildRequest(requestBuilder: Request.Builder): Request {
        return requestBuilder.apply {
            header(HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_VALUE)
            AppPreferences.accessToken?.let { header(HEADER_AUTHORIZATION, it) }
        }.build()
    }

    companion object {
        const val HEADER_AUTHORIZATION = "x-access-token"
        const val HEADER_CONTENT_TYPE = "Content-Type"
        const val HEADER_CONTENT_TYPE_VALUE = "application/json"
    }
}