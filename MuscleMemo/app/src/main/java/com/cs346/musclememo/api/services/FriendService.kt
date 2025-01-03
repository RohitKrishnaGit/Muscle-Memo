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

data class ReportUserAction(val reportedUserId: Int, val reason: String)

public interface  FriendService {

    @GET("/users/me/incomingFriendRequests")
    fun getIncomingFriendRequestsByUserId (): Call<ApiResponse<List<Friend>>>

    @GET("/users/me/friends")
    fun getFriendByUserId (): Call<ApiResponse<List<Friend>>>

    @POST("/users/me/removeFriend")
    fun removeFriendByUserId (@Body friendId: Any): Call<ApiResponse<Void>>

    @POST("/users/me/acceptFriendRequest")
    fun acceptFriendRequest (@Body friendId: Any): Call<ApiResponse<String>>

    @POST("/users/me/rejectFriendRequest")
    fun rejectFriendRequest (@Body friendId: Any): Call<ApiResponse<String>>

    @POST("/users/me/sendFriendRequest")
    fun sendFriendRequest (@Body friendId: Any): Call<ApiResponse<Any>>

    @GET("/users/username/{username}")
    fun getUserIdByUsername (@Path("username") username: String): Call<ApiResponse<Int>>

    @GET("/users/{id}")
    fun getFriendById (@Path("id") id: Int): Call<ApiResponse<User>>

    @POST("/users/report")
    fun reportUserById(@Body reportUserAction: ReportUserAction): Call<ApiResponse<String>>

    @GET("/chat/{roomId}")
    fun getChat(@Path("roomId") roomId: String): Call<ApiResponse<List<Message>>>
}
