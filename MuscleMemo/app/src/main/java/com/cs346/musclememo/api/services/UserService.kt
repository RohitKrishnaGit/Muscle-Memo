package com.cs346.musclememo.api.services

import com.cs346.musclememo.api.types.ApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val accessToken: String, val refreshToken: String)

public interface UserService {
    @POST("/users/login")
    fun getAuthentication(@Body body: LoginRequest): Call<ApiResponse<LoginResponse>>
}