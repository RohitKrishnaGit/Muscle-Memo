package com.cs346.musclememo.screens.services

import com.cs346.musclememo.api.types.ApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class SignupRequest (
    val username: String,
    val password: String,
    val email: String,
    val gender: String,
    val experience: String,
)



interface SignupService{
    @POST("/users/register")
    fun createAccount(@Body createAccount: SignupRequest): Call<ApiResponse<String>>

    @GET("/users/email/{email}")
    fun checkEmail (@Path("email") email: String): Call<ApiResponse<Boolean>>
}