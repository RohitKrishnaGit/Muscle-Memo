
package com.cs346.musclememo.api


import com.cs346.musclememo.api.services.UserService
import com.cs346.musclememo.screens.services.SignupService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient

import com.cs346.musclememo.api.services.ExerciseService
import com.cs346.musclememo.api.services.WorkoutService
import com.cs346.musclememo.api.services.FriendService
import com.cs346.musclememo.classes.Friend
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance: RetrofitInterface {
    private var gson: Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        .setLenient()
        .create()

    private val retrofit: Retrofit by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .authenticator(TokenAuthenticator())
            .addInterceptor(TokenInterceptor())
            .build()
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    val friendService: FriendService by lazy {
        retrofit.create(FriendService::class.java)
    }

    val workoutService: WorkoutService by lazy {
        retrofit.create(WorkoutService::class.java)
    }

    val exerciseService: ExerciseService by lazy {
        retrofit.create(ExerciseService::class.java)
    }

    val signupService: SignupService by lazy {
        retrofit.create(SignupService::class.java)
    }
}