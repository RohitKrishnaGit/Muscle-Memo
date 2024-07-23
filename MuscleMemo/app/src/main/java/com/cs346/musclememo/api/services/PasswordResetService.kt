package com.cs346.musclememo.api.services

import com.cs346.musclememo.api.types.ApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

data class PasswordResetAttemptRequest (val email: String)
data class PasswordResetRequest(val password: String)

interface PasswordResetService{
    @POST("/users/reset-password/request")
    fun requestPasswordReset(@Body body: PasswordResetAttemptRequest): Call<ApiResponse<String>>

    @POST("/users/reset-password/confirm/{code}")
    fun verifyCode(@Path("code") code: String, @Body body: PasswordResetAttemptRequest): Call<ApiResponse<String>>

    @POST("/users/reset-password/{code}")
    fun resetPassword(@Path("code") code: String, @Body body: PasswordResetRequest): Call<ApiResponse<String>>
}