package com.cs346.musclememo.api.services

import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val accessToken: String, val refreshToken: String)

interface UserService {
    @POST("/users/login")
    fun getAuthentication(@Body body: LoginRequest): Call<ApiResponse<LoginResponse>>

    @DELETE("/users/logoutAll")
    fun signoutAllDevices(): Call<ApiResponse<String>>

    @PUT("/users/update/me")
    fun updateGender (@Body user: User): Call<ApiResponse<User>>
}