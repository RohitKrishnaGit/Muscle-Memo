package com.cs346.musclememo.api

import com.cs346.musclememo.api.services.ExerciseRefService
import com.cs346.musclememo.api.services.UserService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance: RetrofitInterface {

    private val retrofit: Retrofit by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .authenticator(TokenAuthenticator())
            .addInterceptor(TokenInterceptor())
            .build()
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    val exerciseService: ExerciseRefService by lazy {
        retrofit.create(ExerciseRefService::class.java)
    }
}