package com.cs346.musclememo.api.services

import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.Exercise
import com.cs346.musclememo.classes.ExerciseRef
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

public interface ExerciseService {
    @GET("/exerciseRefs")
    fun getExerciseRef(): Call<ApiResponse<List<ExerciseRef>>>

    @GET("/combinedExerciseRefs/me")
    fun getCombinedExerciseRefs(): Call<ApiResponse<List<ExerciseRef>>>

    @POST("/exercises")
    fun createExercise(@Body exercise: Exercise): Call<ApiResponse<Boolean>>
}