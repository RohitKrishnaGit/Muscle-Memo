package com.cs346.musclememo.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cs346.musclememo.api.RetrofitInstance
import com.cs346.musclememo.api.services.Records
import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.ExerciseRef
import com.cs346.musclememo.classes.LeaderboardEntry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LeaderboardScreenViewModel : ViewModel() {
    val globalLeaderboardEntries = mutableStateListOf<LeaderboardEntry>()
    val friendsLeaderboardEntries = mutableStateListOf<LeaderboardEntry>()

    val leaderboardEntries: List<LeaderboardEntry>
        get() = if (leaderboardType == "Friends") friendsLeaderboardEntries.toList() else globalLeaderboardEntries.toList()
    val currentExerciseRef by mutableStateOf(ExerciseRef(id = 1))

    var leaderboardType by mutableStateOf("Global")
    var chooseExerciseVisible by mutableStateOf(false)

    fun updateChooseExerciseVisible(state: Boolean){
        chooseExerciseVisible = state
    }
    fun updateLeaderboardType(type: String){
        leaderboardType = type
    }



    fun fetchLeaderboardResults(){
        RetrofitInstance.userPrsService.getTopN(currentExerciseRef.id.toString(), "50").enqueue(object:
            Callback<ApiResponse<List<Records>>>{
            override fun onResponse(
                call: Call<ApiResponse<List<Records>>>,
                response: Response<ApiResponse<List<Records>>>
            ) {
                if (response.isSuccessful){
                    globalLeaderboardEntries.clear()
                    response.body()?.data?.let{ Records ->
                        for (record in Records){
                            val entry = LeaderboardEntry(username = record.username, value = record.pr)
                            globalLeaderboardEntries.add(entry)
                        }
                        globalLeaderboardEntries.sortBy {it.value}
                        println("$globalLeaderboardEntries")
                        }
                    }
            }

            override fun onFailure(call: Call<ApiResponse<List<Records>>>, t: Throwable) {
                t.printStackTrace()
                println("Fetch leaderboard failed")
            }

        })
    }

    fun fetchFriendLeaderboardResults(){
        RetrofitInstance.userPrsService.getTopNFriends(currentExerciseRef.id.toString(), "50").enqueue(object:
            Callback<ApiResponse<List<Records>>>{
            override fun onResponse(
                call: Call<ApiResponse<List<Records>>>,
                response: Response<ApiResponse<List<Records>>>
            ) {
                if (response.isSuccessful){
                    friendsLeaderboardEntries.clear()
                    response.body()?.data?.let{
                        for (record in it){
                            val entry = LeaderboardEntry(username = record.username, value = record.pr)
                            friendsLeaderboardEntries.add(entry)
                        }
                        friendsLeaderboardEntries.sortBy {it.value}
                        println("$friendsLeaderboardEntries")
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Records>>>, t: Throwable) {
                t.printStackTrace()
                println("Fetch friend leaderboard failed")
            }

        })
    }
    init {
        fetchLeaderboardResults()
    }
}