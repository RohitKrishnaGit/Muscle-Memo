package com.cs346.musclememo.api.services

import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.User
import com.cs346.musclememo.classes.Workout
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val accessToken: String, val refreshToken: String)

public interface UserService {
    @POST("/users/login")
    fun getAuthentication(@Body body: LoginRequest): Call<ApiResponse<LoginResponse>>

    @PUT("/users/gender/me")
    fun updateGender (@Body gender: String): Call<ApiResponse<User>>

    @PUT("/users/experience/me")
    fun updateExperience (@Body experience: String): Call<ApiResponse<User>>

}