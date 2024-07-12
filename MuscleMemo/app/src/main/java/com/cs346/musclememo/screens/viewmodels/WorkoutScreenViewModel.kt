package com.cs346.musclememo.screens.viewmodels

import com.cs346.musclememo.api.RetrofitInstance
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.Exercise
import com.cs346.musclememo.classes.ExerciseSet
import com.cs346.musclememo.classes.Workout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkoutScreenViewModel : ViewModel() {
    var currentWorkout by mutableStateOf(Workout())
        private set

    private val _exercises = mutableStateListOf<Exercise>()
    val exercises : List<Exercise>  = _exercises

    var workoutVisible by mutableStateOf(false)
        private set


    var summaryVisible by mutableStateOf(false)
        private set

    fun setSummaryScreenVisible (visible: Boolean){
        summaryVisible = visible
    }

    fun setWorkoutScreenVisible(visible: Boolean) {
        workoutVisible = visible
    }
    fun getExercises(){
        RetrofitInstance.exerciseService.getExerciseRef().enqueue(object: Callback<ApiResponse<List<Exercise>>>{
            override fun onResponse(
                call: Call<ApiResponse<List<Exercise>>>,
                response: Response<ApiResponse<List<Exercise>>>
            ) {
                if (response.isSuccessful){
                    response.body()?.data?.let{
                        for (exercise in it){
                            _exercises.add(Exercise(exercise.name, exercise.id))
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Exercise>>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }



    fun setWorkoutName(name : String){
        currentWorkout.setWorkoutName(name)
    }

    fun resetWorkout(){
        currentWorkout.setWorkout()
    }

    fun removeExercise(exerciseIndex: Int){
        currentWorkout.removeExercise(exerciseIndex)
    }

    fun addNewExercise(exercise: Exercise){
        currentWorkout.addNewExercise(exercise)
    }

    fun addSet(exerciseIndex: Int) {
        currentWorkout.addSet(exerciseIndex)
    }

    fun editSet(weight: Int?, reps: Int?, exerciseIndex : Int, setIndex: Int) {
        currentWorkout.exercises[exerciseIndex].sets[setIndex] = ExerciseSet(weight, reps)
    }

    fun removeSet(exerciseIndex: Int, setIndex: Int) {
        currentWorkout.removeSet(exerciseIndex, setIndex)
    }

    fun finishWorkout(){
        // todo: send data to backend
    }

    init {
        // todo: get all normal and custom exercises from backend
        _exercises.add(Exercise("Bench", 0))
        _exercises.add(Exercise("Squat", 1))
        _exercises.add(Exercise("Deadlift", 2))
    }
}