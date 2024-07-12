package com.cs346.musclememo.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cs346.musclememo.classes.Exercise
import com.cs346.musclememo.classes.ExerciseIteration
import com.cs346.musclememo.classes.ExerciseSet
import com.cs346.musclememo.classes.User
import com.cs346.musclememo.classes.Workout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryScreenViewModel : ViewModel() {

    var user by mutableStateOf<User?>(null)

    private val _workouts = mutableStateListOf<Workout>()
    val workouts : List<Workout> = _workouts

    init {
        // todo: grab user history
        user = User(
            "starfy84",
            "Dereck Tu",
            "starfy84@gmail.com",
            mutableListOf<Workout>(
                Workout(
                    "Test Workout",
                    mutableListOf<ExerciseIteration>(
                        ExerciseIteration(
                            Exercise("Deadlift", 1),
                            mutableListOf<ExerciseSet>(ExerciseSet(10, 10))
                        )
                    )
                )
            )
        )
    }

    fun getWorkoutsByUserId(userId: Int){
        val apiService = RetrofitInstance.workoutService
        val call = apiService.getWorkoutByUserId(userId)

        call.enqueue(object : Callback<List<Workout>> {
            override fun onResponse(call: Call<List<Workout>>, response: Response<List<Workout>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        for (workout in it) {
                            _workouts.add(workout)
                        }
                    }
                }
            }
                override fun onFailure(call: Call<List<Workout>>, t: Throwable) {
                    println("Failure: ${t.message}")
                }
        })
        }
}