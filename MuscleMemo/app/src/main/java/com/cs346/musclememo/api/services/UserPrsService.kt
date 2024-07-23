package com.cs346.musclememo.api.services

import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class Records(val userId: String, val pr: String)

interface UserPrsService {
    @GET("/Pr/me")
    fun getAllUserPr(): Call<ApiResponse<Map<String, Int>>>

    @GET("/Pr/me/{exerciseRefId}")
    fun getUserPr(@Path("exerciseRefId") exerciseRefId: String): Call<ApiResponse<Number>>

    @POST("/Pr/me/{exerciseRefId}")
    fun updateUserPr(@Path("exerciseRefId") exerciseRefId: String, @Body pr: Number): Call<ApiResponse<String>>

    @GET("/leaderboard/{exerciseRefId}/{count}")
    fun getTopN(@Path("exerciseRefId") exerciseRefId: String, @Path("count") count: String, @Body pr: Number): Call<ApiResponse<List<Records>>>

    @GET("/leaderboard/me/{exerciseRefId}/{count}")
    fun getTopNFriends(@Path("exerciseRefId") exerciseRefId: String, @Path("count") count: String, @Body pr: Number): Call<ApiResponse<List<Records>>>
}