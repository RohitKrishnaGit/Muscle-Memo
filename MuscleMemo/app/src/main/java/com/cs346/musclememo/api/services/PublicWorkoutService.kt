package com.cs346.musclememo.api.services

import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.User
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

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
    val date: Long = 6,
    val experience: String? = null,
    val gender: String? = null,
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
    fun createPublicWorkout(@Body body: CreatePublicWorkout): Call<ApiResponse<Any>>

    @GET("/publicWorkouts/allJoined/me")
    fun getJoinedPublicWorkouts(): Call<ApiResponse<List<PublicWorkout>>>

    @POST("/publicWorkouts/filter/me")
    fun filterPublicWorkout(@Body body: FilterPublicWorkout): Call<ApiResponse<List<PublicWorkout>>>

    @GET("/publicWorkouts/me")
    fun fetchMyPublicWorkouts(): Call<ApiResponse<List<PublicWorkout>>>

    @POST("publicWorkoutRequests/me/sendPublicWorkoutRequest")
    fun sendPublicWorkoutRequest(@Body body: WorkoutRequestBody): Call<ApiResponse<String>>

    @GET("/publicWorkoutRequests/me/incomingPublicWorkoutRequests/{workoutId}")
    fun getPublicWorkoutRequests(@Path("workoutId") workoutId: String): Call<ApiResponse<List<WorkoutRequest>>>

    @POST("/publicWorkoutRequests/me/{requestId}/acceptPublicWorkoutRequest")
    fun acceptPublicWorkoutRequest(@Path("requestId") requestId: String): Call<ApiResponse<String>>

    @POST("/publicWorkoutRequests/me/{requestId}/rejectPublicWorkoutRequest")
    fun rejectPublicWorkoutRequest(@Path("requestId") requestId: String): Call<ApiResponse<String>>

    @DELETE("/publicWorkouts/me/{id}")
    fun deletePublicWorkout(@Path("id") id: String): Call<ApiResponse<String>>
}
