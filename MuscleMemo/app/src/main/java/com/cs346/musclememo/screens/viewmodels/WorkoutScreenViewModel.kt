package com.cs346.musclememo.screens.viewmodels

import com.cs346.musclememo.api.RetrofitInstance
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.Exercise
import com.cs346.musclememo.classes.ExerciseRef
import com.cs346.musclememo.classes.ExerciseSet
import com.cs346.musclememo.classes.Workout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkoutScreenViewModel : ViewModel() {
    var currentWorkout by mutableStateOf(Workout())
        private set
    private val _exerciseRefs = mutableStateListOf<ExerciseRef>()
    val exerciseRefs : List<ExerciseRef>  = _exerciseRefs
    var workoutVisible by mutableStateOf(false)
        private set
    var summaryVisible by mutableStateOf(false)
        private set

    var chooseWorkoutVisible by mutableStateOf(false)
        private set

    fun onBackPressed(){
        if (chooseWorkoutVisible){
            chooseWorkoutVisible = false
        }
    }

    fun updateChooseWorkoutVisible (visible: Boolean){
        chooseWorkoutVisible = visible
    }

    fun setSummaryScreenVisible (visible: Boolean){
        summaryVisible = visible
    }

    fun setWorkoutScreenVisible(visible: Boolean) {
        workoutVisible = visible
    }


    fun createWorkout(workout: Workout){
        val apiService = RetrofitInstance.workoutService
        val call = apiService.createWorkout(workout)

        val exercises = workout.exercises

        var workoutId: Int

        call.enqueue(object : Callback<ApiResponse<Workout>> {
            override fun onResponse(call: Call<ApiResponse<Workout>>, response: Response<ApiResponse<Workout>>) {
                if (response.isSuccessful) {
                    // Handle successful response
                    val createdWorkout = response.body()?.data
                    println(createdWorkout)
                    createdWorkout?.let {
                        workoutId = it.id
                        for (exercise in exercises){ //populate exercises
                            val newExercise = Exercise(exerciseSet = exercise.exerciseSet, workoutId = workoutId, templateId = exercise.templateId, exerciseRefId = exercise.exerciseRef.id, customExerciseRefId = exercise.customExerciseRef?.id)
                            createExercises(newExercise)
                        }
                        println(workoutId)
                    }
                    println("Workout created successfully" )
                } else {
                    // Handle error response
                    println("Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<Workout>>, t: Throwable) {
                // Handle failure
                println("Failure: ${t.message}")
            }

        })

    }

    private fun createExercises(exercise: Exercise){
        val apiService = RetrofitInstance.exerciseService
        val call = apiService.createExercise(exercise)
        call.enqueue(object: Callback<ApiResponse<Boolean>>{
            override fun onResponse(call: Call<ApiResponse<Boolean>>, response: Response<ApiResponse<Boolean>>) {
                if (response.isSuccessful) {
                    // Handle successful response
                    val createdWorkout = response.body()
                    println("Exercise created successfully" )
                } else {
                    // Handle error response
                    println("Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<Boolean>>, t: Throwable) {
                println("Failure: ${t.message}")
            }

        })
    }


    fun getExercises(){
        RetrofitInstance.exerciseService.getExerciseRef().enqueue(object: Callback<ApiResponse<List<ExerciseRef>>>{
            override fun onResponse(
                call: Call<ApiResponse<List<ExerciseRef>>>,
                response: Response<ApiResponse<List<ExerciseRef>>>
            ) {
                if (response.isSuccessful){
                    response.body()?.data?.let{
                        for (exercise in it){
                            _exerciseRefs.add(ExerciseRef(exercise.name, exercise.id, exercise.durationVSReps, exercise.weight, exercise.distance))
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<ExerciseRef>>>, t: Throwable) {
                t.printStackTrace()

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

    fun addNewExercise(exerciseRef: ExerciseRef){
        currentWorkout.addNewExercise(exerciseRef)
    }

    fun addSet(exerciseIndex: Int) {
        currentWorkout.addSet(exerciseIndex)
    }

    fun editSet(weight: Int?, reps: Int?, exerciseIndex : Int, setIndex: Int) {
        currentWorkout.exercises[exerciseIndex].exerciseSet[setIndex] = ExerciseSet(weight, reps)
    }

    fun removeSet(exerciseIndex: Int, setIndex: Int) {
        currentWorkout.removeSet(exerciseIndex, setIndex)
    }

    fun finishWorkout(){
        createWorkout(currentWorkout)
    }

    init {
        getExercises()
    }


    private var workoutIndex = 0
    private val workoutOrder: List<WorkoutState> = listOf(
        WorkoutState.NEW_WORKOUT,
        WorkoutState.CURRENT_WORKOUT
    )
    var workoutScreenData by mutableStateOf(createWorkoutScreenData())

    private fun createWorkoutScreenData(): WorkoutScreenData {
        return WorkoutScreenData(workoutIndex, workoutOrder[workoutIndex])
    }

    enum class WorkoutState {
        NEW_WORKOUT,
        CURRENT_WORKOUT
    }

    data class WorkoutScreenData (
        val screenIndex: Int,
        val screen: WorkoutState
    )
}