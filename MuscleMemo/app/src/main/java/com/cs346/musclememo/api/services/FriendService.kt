package com.cs346.musclememo.api.services

import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.Friend
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path


public interface  FriendService {
//    @POST("/workouts")
//    fun createWorkout(@Body workout: Workout): Call<ApiResponse<Workout>>

    @GET("/users/{userId}/friends")
    fun getFriendByUserId (@Path("userId") userId: Int): Call<ApiResponse<List<Friend>>>
}