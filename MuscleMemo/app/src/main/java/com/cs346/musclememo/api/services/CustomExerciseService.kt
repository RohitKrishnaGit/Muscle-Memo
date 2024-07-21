package com.cs346.musclememo.api.services

import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.ExerciseRef
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

public interface CustomExerciseService {
    @POST("/customExerciseRefs/me")
    fun createExercise(@Body exerciseRef: ExerciseRef): Call<ApiResponse<ExerciseRef>>

    @PUT("/customExerciseRefs/me/{id}")
    fun updateExercise(@Path("id") id: String, @Body exerciseRef: ExerciseRef): Call<ApiResponse<ExerciseRef>>

    @DELETE("/customExerciseRefs/me/{id}")
    fun deleteExercise(@Path("id") id: String): Call<ApiResponse<String>>
}