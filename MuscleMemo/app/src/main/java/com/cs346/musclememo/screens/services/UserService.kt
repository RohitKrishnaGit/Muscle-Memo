package com.cs346.musclememo.screens.services

import com.cs346.musclememo.classes.Users
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path


public interface UserService {
    @GET("/users/login/{user}/{password}")
    fun getAuthentication(@Path("user") user: String,
                          @Path("password") password: String
    ): Call<Boolean>
}