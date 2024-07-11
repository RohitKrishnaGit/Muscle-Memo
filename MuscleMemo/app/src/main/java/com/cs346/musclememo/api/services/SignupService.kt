package com.cs346.musclememo.screens.services

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class SignupRequest (
    var username: String,
    var password: String,
    var email: String,
    var fullName: String
)

data class SignupResponse (
    var response: String
)

public interface SignupService{
    @POST("/users/register")
    fun createAccount(@Body createAccount: SignupRequest): Call<SignupResponse>
}