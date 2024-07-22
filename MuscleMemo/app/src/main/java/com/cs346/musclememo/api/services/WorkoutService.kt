package com.cs346.musclememo.api.services

import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.ExerciseRef
import com.cs346.musclememo.classes.Workout
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.GET

data class CreateWorkoutRequest (val name: String, val date: Long, val duration: Int, val userId: String = "me")
data class CreateWorkoutResponse (val workoutId: Int)
data class GetWorkoutExerciseResponse(val id: Int, val exerciseSet: List<ExerciseDataSet>, val exerciseRef: ExerciseRef)
data class GetWorkoutResponse (val workoutId: Int, val name: String, val date: Long, val duration: Int, val exercises: List<GetWorkoutExerciseResponse>)

interface  WorkoutService {
    @POST("/workouts")
    fun createWorkout(@Body createWorkoutRequest: CreateWorkoutRequest): Call<ApiResponse<CreateWorkoutResponse>>

    @GET("/workouts/me")
    fun getWorkoutByUserId (): Call<ApiResponse<List<GetWorkoutResponse>>>
}