package com.cs346.musclememo.screens.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs346.musclememo.api.RetrofitInstance
import com.cs346.musclememo.api.types.ApiResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.cs346.musclememo.classes.Friend
import com.cs346.musclememo.classes.Workout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendsScreenViewModel : ViewModel() {
    private val _friends = mutableStateListOf<Friend>()
    val friends : List<Friend> = _friends

    init {
        getFriendByUserId(1)
    }

    fun getFriendByUserId(userId: Int) {
        val apiService = RetrofitInstance.friendService
        val call = apiService.getFriendByUserId(userId)

        call.enqueue(object : Callback<ApiResponse<List<Friend>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Friend>>>,
                response: Response<ApiResponse<List<Friend>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        for (friend in it) {
                            println(friend)
                            _friends.add(friend)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Friend>>>, t: Throwable) {
                println("Failure: ${t.message}")
            }
        })
    }
}