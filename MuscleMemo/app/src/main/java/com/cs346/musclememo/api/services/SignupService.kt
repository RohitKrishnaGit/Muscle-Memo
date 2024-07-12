package com.cs346.musclememo.screens.services

import com.cs346.musclememo.api.types.ApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class SignupRequest (
    var username: String,
    var password: String,
    var email: String,
    var fullName: String
)



public interface SignupService{
    @POST("/users/register")
    fun createAccount(@Body createAccount: SignupRequest): Call<ApiResponse<String>>
}