package com.cs346.musclememo.api.services

import com.cs346.musclememo.api.types.ApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class UpdateUserVisibilityPrRequest(val allowedValue: Boolean)


interface PrVisibilityService {
    @GET("/Pr/visibility/me/{exerciseRefId}")
    fun getUserPr(@Path("exerciseRefId") exerciseRefId: String): Call<ApiResponse<Boolean>>

    @POST("/Pr/visibility/me/{exerciseRefId}")
    fun updateUserPr(@Path("exerciseRefId") exerciseRefId: Int, @Body Pr: UpdateUserVisibilityPrRequest): Call<ApiResponse<String>>
}