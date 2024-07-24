package com.cs346.musclememo.api.services

import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.Friend
import com.cs346.musclememo.classes.User
import com.cs346.musclememo.screens.viewmodels.Message
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.logging.Filter

data class PublicWorkout(
    val id: Int,
    val name: String,
    val date: Int,
    val description: String,
    val latitude: String,
    val longitude: String,
    val gender: String?,
    val experience: String
)

data class CreatePublicWorkout(
    val name: String,
    val date: Int = 6,
    val experience: String,
    val latitude: String = "10",
    val longitude: String = "10",
    val description: String,
)

data class FilterPublicWorkout(
    val gender: String? = null,
    val experience: String? = null,
    val friendsOnly: Boolean,
    val latitude: String,
    val longitude: String
)

data class WorkoutRequestBody(
    val publicWorkoutId: Int
)

data class WorkoutRequest(
    val id: Int,
    val publicWorkout: PublicWorkout,
    val sender: User
)

public interface  PublicWorkoutService {
    @POST("/publicWorkouts/me")
    fun createPublicWorkout(@Body body: CreatePublicWorkout): Call<ApiResponse<Int>>

//    @GET("/publicWorkouts/me")
//    fun getInvolvedWorkouts(): Call<ApiResponse<List<PublicWorkout>>>

    @POST("/publicWorkouts/filter/me")
    fun filterPublicWorkout(@Body body: FilterPublicWorkout): Call<ApiResponse<List<PublicWorkout>>>

    @GET("/publicWorkouts/me")
    fun fetchMyPublicWorkouts(): Call<ApiResponse<List<PublicWorkout>>>

    @POST("publicWorkoutRequests/me/sendPublicWorkoutRequest")
    fun sendWorkoutRequest(@Body body: WorkoutRequestBody): Call<ApiResponse<String>>

    @GET("/publicWorkoutRequests/me/incomingPublicWorkoutRequests/{workoutId}")
    fun getWorkoutRequests(@Path("workoutId") workoutId: String): Call<ApiResponse<List<WorkoutRequest>>>

    @POST("/publicWorkoutRequests/me/{requestId}/acceptPublicWorkoutRequest")
    fun acceptWorkoutRequest(@Path("requestId") requestId: String): Call<ApiResponse<String>>

    @POST("/publicWorkoutRequests/me/{requestId}/rejectPublicWorkoutRequest")
    fun rejectWorkoutRequest(@Path("requestId") requestId: String): Call<ApiResponse<String>>
}
