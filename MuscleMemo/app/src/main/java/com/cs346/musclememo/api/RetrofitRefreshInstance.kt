package com.cs346.musclememo.api

import com.cs346.musclememo.api.services.TokenService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitRefreshInstance: RetrofitInterface {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val tokenService: TokenService by lazy {
        retrofit.create(TokenService::class.java)
    }
}