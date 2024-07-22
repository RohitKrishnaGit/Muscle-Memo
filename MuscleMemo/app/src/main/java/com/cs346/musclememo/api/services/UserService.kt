package com.cs346.musclememo.api.services

import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val accessToken: String, val refreshToken: String)
data class LogoutRequest(val refreshToken: String, val firebaseToken: String)
data class FirebaseToken(var firebaseTokens: String = "")

interface UserService {
    @POST("/users/login")
    fun getAuthentication(@Body body: LoginRequest): Call<ApiResponse<LoginResponse>>

    @POST("/users/logout/me")
    fun logout(@Body body: LogoutRequest): Call<ApiResponse<String>>

    @DELETE("/users/logoutAll")
    fun logoutAllDevices(): Call<ApiResponse<String>>

    @PUT("/users/update/me")
    fun updateUser (@Body user: User): Call<ApiResponse<User>>

    @PUT("/users/update/firebase/me")
    fun updateUserTokens(@Body token: FirebaseToken): Call<ApiResponse<User>>

    @GET("/users/me")
    fun getMyUser(): Call<ApiResponse<User>>
}