package com.cs346.musclememo.api.services

import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.User
import com.cs346.musclememo.classes.UserPrs
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PrVisibilityService {
    @GET("/Pr/me/{exerciseRefId}")
    fun getUserPr(@Path("exerciseRefId") exerciseRefId: String): Call<ApiResponse<Boolean>>

    @POST("/Pr/me/{exerciseRefId}")
    fun updateUserPr(@Path("exerciseRefId") exerciseRefId: String, @Body allowedValue: Boolean): Call<ApiResponse<String>>
}