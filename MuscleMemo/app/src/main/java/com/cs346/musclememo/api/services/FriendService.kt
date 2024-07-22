package com.cs346.musclememo.api.services

import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.Friend
import com.cs346.musclememo.classes.User
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path


public interface  FriendService {
//    @POST("/workouts")
//    fun createWorkout(@Body workout: Workout): Call<ApiResponse<Workout>>

    @GET("/users/me/incomingFriendRequests")
    fun getIncomingFriendRequestsByUserId (): Call<ApiResponse<List<Friend>>>

    @GET("/users/me/friends")
    fun getFriendByUserId (): Call<ApiResponse<List<Friend>>>

    @POST("/users/me/removeFriend")
    fun removeFriendByUserId (@Body friendId: Any): Call<ApiResponse<Void>>

    @POST("/users/me/acceptFriendRequest")
    fun acceptFriendRequest (@Body friendId: Any): Call<ApiResponse<List<Friend>>>

    @POST("/users/me/rejectFriendRequest")
    fun rejectFriendRequest (@Body friendId: Any): Call<ApiResponse<List<Friend>>>

    @POST("/users/me/sendFriendRequest")
    fun sendFriendRequest (@Body friendId: Any): Call<ApiResponse<Any>>

    @GET("/users/username/{username}")
    fun getUserIdByUsername (@Path("username") username: String): Call<ApiResponse<Int>>

    @GET("/users/{id}")
    fun getFriendById (@Path("id") id: Int): Call<ApiResponse<User>>
}
