package com.cs346.musclememo.api.services

import com.cs346.musclememo.classes.Workout
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path


public interface  WorkoutService {
    @POST("/workouts")
    fun createWorkout(@Body workout: Workout): Call<Boolean>

    @GET("/workouts/{userId}")
    fun getWorkoutByUserId (@Path("userId") userId: Int): Call<List<Workout>>
}