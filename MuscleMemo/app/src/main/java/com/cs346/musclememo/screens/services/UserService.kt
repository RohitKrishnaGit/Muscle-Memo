package com.cs346.musclememo.screens.services

import com.cs346.musclememo.classes.Users
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

data class LoginRequest(val username: String, val password: String)

public interface UserService {
    @POST("/users/login/{user}/{password}")
    fun getAuthentication(@Body body: LoginRequest): Call<Boolean>
}