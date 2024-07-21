package com.cs346.musclememo.screens.viewmodels

import com.cs346.musclememo.api.RetrofitInstance
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

    var tempWorkoutName by mutableStateOf("")

    var newExerciseRef by mutableStateOf(ExerciseRef(
        name = "",
        id = -1,
        durationVSReps = true,
        weight = true,
        distance = false,
        isCustom = true,
    ))
        private set

    private val _exerciseRefs = mutableStateListOf<ExerciseRef>()
    val exerciseRefs : List<ExerciseRef> = _exerciseRefs

    var workoutVisible by mutableStateOf(false)
        private set

    // whether the choose exercise screen is visible or not
    var addExerciseVisible by mutableStateOf(false)
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

    // controls the header for choosing an exercise screen
    var isExerciseSearchMode by mutableStateOf(false)
        private set

    // text for the search bar
    var exerciseSearchText by mutableStateOf("")

    // controls whether the exercise list is sorted alphabetically or reverse alphabetically
    var isSortedAlphabetically by mutableStateOf(true)

    var showChangeWorkoutNameDialog by mutableStateOf(false)
        private set

    var showCancelWorkoutDialog by mutableStateOf(false)
        private set

    var showDeleteExerciseDialog by mutableStateOf(false)
        private set

    var showAddNewCustomExerciseDialog by mutableStateOf(false)
        private set

    var showEditCustomExerciseDialog by mutableStateOf(false)
        private set

    var showDeleteCustomExerciseDialog by mutableStateOf(false)
        private set

    var dialogErrorMessage by mutableStateOf("")
        private set

    var dialogButtonsEnabled by mutableStateOf(true)
        private set

    fun updateShowChangeWorkoutNameDialog(visible: Boolean){
        showChangeWorkoutNameDialog = visible
    }

    fun updateShowCancelWorkoutDialog(visible: Boolean){
        showCancelWorkoutDialog = visible
    }

    fun updateShowRemoveExerciseDialog(visible: Boolean){
        showDeleteExerciseDialog = visible
    }

    fun updateShowAddNewCustomExerciseDialog(visible: Boolean){
        showAddNewCustomExerciseDialog = visible
    }

    fun updateShowEditCustomExerciseDialog(visible: Boolean){
        showEditCustomExerciseDialog = visible
    }

    fun updateShowDeleteCustomExerciseDialog(visible: Boolean){
        showDeleteCustomExerciseDialog = visible
    }

    fun updateDialogErrorMessage(message: String){
        dialogErrorMessage = message
    }

    fun updateDialogButtonsEnabled(enabled: Boolean){
        dialogButtonsEnabled = enabled
    }

    fun updateNewExerciseRef(exerciseRef: ExerciseRef){
        newExerciseRef = exerciseRef
    }

    fun resetNewExerciseRef() {
        newExerciseRef = ExerciseRef(
            name = "",
            id = -1,
            durationVSReps = true,
            weight = true,
            distance = false,
            isCustom = true
        )
    }

    fun setSummaryScreenVisible (visible: Boolean){
        summaryVisible = visible
    }

    fun setWorkoutScreenVisible(visible: Boolean) {
        workoutVisible = visible
    }

    fun setAddExerciseScreenVisible(visible: Boolean) {
        addExerciseVisible = visible
    }

    fun updateExerciseSearchMode(isSearchMode: Boolean){
        isExerciseSearchMode = isSearchMode
    }

    fun updateExerciseSearchText(newText: String) {
        exerciseSearchText = newText
    }

    fun toggleSort() {
        isSortedAlphabetically = !isSortedAlphabetically
    }

    val filteredExercises: List<ExerciseRef>
        get() = _exerciseRefs
            .filter { it.name.contains(exerciseSearchText, ignoreCase = true) }
            .sortedWith(
                if (isSortedAlphabetically)
                    compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                else
                    compareByDescending(String.CASE_INSENSITIVE_ORDER) { it.name }
            )

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

    fun fetchCombinedExercises(){
        _exerciseRefs.clear()
        RetrofitInstance.exerciseService.getCombinedExerciseRefs().enqueue(object: Callback<ApiResponse<List<ExerciseRef>>>{
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

    fun createCustomExercise(newExerciseRef: ExerciseRef){
        if (newExerciseRef.name == "") {
            dialogErrorMessage = "Please enter a name for the exercise"
            return
        }

        RetrofitInstance.customExerciseService.createExercise(newExerciseRef).enqueue(object: Callback<ApiResponse<ExerciseRef>>{
            override fun onResponse(
                call: Call<ApiResponse<ExerciseRef>>,
                response: Response<ApiResponse<ExerciseRef>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        _exerciseRefs.add(it)
                    }

                    showAddNewCustomExerciseDialog = false
                } else {
                    dialogErrorMessage = "There was an error adding the exercise"
                }

                dialogButtonsEnabled = true
            }

            override fun onFailure(call: Call<ApiResponse<ExerciseRef>>, t: Throwable) {
                println("Failure: ${t.message}")
            }
        })
    }

    fun deleteCustomExercise(customExerciseRef: ExerciseRef) {
        RetrofitInstance.customExerciseService.deleteExercise(customExerciseRef.id.toString()).enqueue(object : Callback<ApiResponse<String>> {
            override fun onResponse(
                call: Call<ApiResponse<String>>,
                response: Response<ApiResponse<String>>
            ) {
                if (response.isSuccessful) {
                    _exerciseRefs.removeIf { it.id == customExerciseRef.id }
                    showDeleteCustomExerciseDialog = false
                } else {
                    dialogErrorMessage = "There was an error deleting the exercise"
                }

                dialogButtonsEnabled = true
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                println("Failure: ${t.message}")
            }
        })
    }

    fun updateCustomExercise(customExerciseRef: ExerciseRef) {
        if (customExerciseRef.name == "") {
            dialogErrorMessage = "Please enter a name for the exercise"
            return
        }

        RetrofitInstance.customExerciseService.updateExercise(customExerciseRef.id.toString(), customExerciseRef)
            .enqueue(object : Callback<ApiResponse<ExerciseRef>> {
            override fun onResponse(
                call: Call<ApiResponse<ExerciseRef>>,
                response: Response<ApiResponse<ExerciseRef>>
            ) {
                if (response.isSuccessful) {
                    _exerciseRefs.removeIf { it.id == customExerciseRef.id }
                    response.body()?.data?.let {
                        _exerciseRefs.add(it)
                    }

                    showEditCustomExerciseDialog = false
                } else {
                    dialogErrorMessage = "There was an error updating the exercise"
                }

                dialogButtonsEnabled = true
            }

            override fun onFailure(call: Call<ApiResponse<ExerciseRef>>, t: Throwable) {
                println("Failure: ${t.message}")
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
        fetchCombinedExercises()
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