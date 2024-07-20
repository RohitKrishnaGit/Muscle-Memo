package com.cs346.musclememo.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cs346.musclememo.api.RetrofitInstance
import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.ExerciseRef
import com.cs346.musclememo.classes.ExerciseIteration
import com.cs346.musclememo.classes.ExerciseSet
import com.cs346.musclememo.classes.User
import com.cs346.musclememo.classes.Workout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryScreenViewModel : ViewModel() {

    private val _workouts = mutableStateListOf<Workout>()
    val workouts : List<Workout> = _workouts
    var currentWorkout: Workout? = null
        private set
    var showCurrentWorkout by mutableStateOf(false)
        private set

    fun updateShowCurrentWorkout(state: Boolean){
        showCurrentWorkout = state
    }

    fun updateCurrentWorkout(workout: Workout){
        currentWorkout = workout
    }

    fun onBackPressed(){
        if (showCurrentWorkout) {
            showCurrentWorkout = false
            currentWorkout = null
        }
    }

    fun convertDate(date: String): String {
        //TODO: proper conversion
        return "Saturday, July 20th"
    }

    init {
        getWorkoutsByUserId()
    }

    private fun getWorkoutsByUserId(){
        val apiService = RetrofitInstance.workoutService
        val call = apiService.getWorkoutByUserId()

        call.enqueue(object : Callback<ApiResponse<List<Workout>>> {
            override fun onResponse(call: Call<ApiResponse<List<Workout>>>, response: Response<ApiResponse<List<Workout>>>) {
                if (response.isSuccessful) {
                    //println(response.body())
                    response.body()?.data?.let {
                        for (workout in it) {
                            println(workout)
                            _workouts.add(workout)
                        }
                    }
                }
            }
                override fun onFailure(call: Call<ApiResponse<List<Workout>>>, t: Throwable) {
                    println("Failure: ${t.message}")
                }
        })
        }
}