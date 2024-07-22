package com.cs346.musclememo.api.services

import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.CustomExerciseRef
import com.cs346.musclememo.classes.Exercise
import com.cs346.musclememo.classes.ExerciseRef
import com.cs346.musclememo.classes.ExerciseSet
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class ExerciseDataSet (val weight: Int?, val reps: Int?, val duration: Int?, val distance: Int?)
data class ExerciseRequest (val workoutId: Int, val exerciseRefId: Int?, val customExerciseRefId: Int?, val exerciseSet: List<ExerciseDataSet>)

interface ExerciseService {
    @GET("/exerciseRefs")
    fun getExerciseRef(): Call<ApiResponse<List<ExerciseRef>>>

    @GET("/combinedExerciseRefs/me")
    fun getCombinedExerciseRefs(): Call<ApiResponse<List<ExerciseRef>>>

    @POST("/exercises")
    fun createExercise(@Body exerciseRequest: ExerciseRequest): Call<ApiResponse<Boolean>>
}