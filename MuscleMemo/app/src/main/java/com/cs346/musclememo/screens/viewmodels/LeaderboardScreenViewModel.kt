package com.cs346.musclememo.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cs346.musclememo.api.RetrofitInstance
import com.cs346.musclememo.api.services.Records
import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.LeaderboardEntry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LeaderboardScreenViewModel : ViewModel() {
    val leaderboardEntries = mutableStateListOf<LeaderboardEntry>()
    var exerciseRefId by mutableStateOf("1")

    init {
        // todo: grab leaderboards
        fetchLeaderboardResults()
    }
    fun fetchLeaderboardResults(){

        RetrofitInstance.userPrsService.getTopN(exerciseRefId, "50").enqueue(object:
            Callback<ApiResponse<List<Records>>>{
            override fun onResponse(
                call: Call<ApiResponse<List<Records>>>,
                response: Response<ApiResponse<List<Records>>>
            ) {
                if (response.isSuccessful){
                    leaderboardEntries.clear()
                    response.body()?.data?.let{
                        for (record in it){
                            val entry = LeaderboardEntry(username = record.username, value = record.pr)
                            leaderboardEntries.add(entry)
                        }
                        println("$leaderboardEntries")
                        }
                    }
            }

            override fun onFailure(call: Call<ApiResponse<List<Records>>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }
    fun fetchFriendLeaderboardResults(){
        RetrofitInstance.userPrsService.getTopNFriends(exerciseRefId, "50").enqueue(object:
            Callback<ApiResponse<List<Records>>>{
            override fun onResponse(
                call: Call<ApiResponse<List<Records>>>,
                response: Response<ApiResponse<List<Records>>>
            ) {
                if (response.isSuccessful){
                    leaderboardEntries.clear()
                    response.body()?.data?.let{
                        for (record in it){
                            val entry = LeaderboardEntry(username = record.username, value = record.pr)
                            leaderboardEntries.add(entry)
                        }
                        println("$leaderboardEntries")
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Records>>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}