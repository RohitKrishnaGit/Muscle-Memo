package com.cs346.musclememo.api.services

import com.cs346.musclememo.api.RetrofitRefreshInstance
import com.cs346.musclememo.api.types.ApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class TokenRequest(val refreshToken: String)

public interface TokenService {
    @POST("/token")
    fun getAuthentication(@Body body: TokenRequest): Call<ApiResponse<String>>

    companion object {
        val instance = RetrofitRefreshInstance.tokenService
    }
}