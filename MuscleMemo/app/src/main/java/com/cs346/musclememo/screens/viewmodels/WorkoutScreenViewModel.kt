package com.cs346.musclememo.screens.viewmodels

import com.cs346.musclememo.api.RetrofitInstance
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cs346.musclememo.api.services.ExerciseDataSet
import com.cs346.musclememo.api.services.ExerciseRequest
import com.cs346.musclememo.api.services.CreateWorkoutRequest
import com.cs346.musclememo.api.services.CreateWorkoutResponse
import com.cs346.musclememo.api.services.GetWorkoutResponse
import com.cs346.musclememo.api.services.CreateTemplateRequest
import com.cs346.musclememo.api.services.CreateTemplateResponse
import com.cs346.musclememo.api.services.GetTemplateResponse
import com.cs346.musclememo.api.services.UpdateUserPrRequest
import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.api.types.parseErrorBody
import com.cs346.musclememo.classes.ExerciseIteration
import com.cs346.musclememo.classes.ExerciseRef
import com.cs346.musclememo.classes.ExerciseSet
import com.cs346.musclememo.classes.Template
import com.cs346.musclememo.classes.Workout
import com.cs346.musclememo.utils.AppPreferences
import com.cs346.musclememo.utils.calculateScore
import com.cs346.musclememo.utils.epochToMonthYear
import com.cs346.musclememo.utils.translateDistance
import com.cs346.musclememo.utils.translateWeight
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkoutScreenViewModel: ViewModel() {
    var currentWorkout by mutableStateOf(Workout())
        private set

    var currentTemplate by mutableStateOf(Template())
        private set
    var newExerciseRef by mutableStateOf(ExerciseRef(
        name = "",
        id = -1,
        durationVSReps = true,
        weight = true,
        distance = false,
        isCustom = true,
    ))
        private set
    var selectedExerciseIndex by mutableIntStateOf(-1)
    var selectedExercise by mutableStateOf<ExerciseRef?>(null)
    var selectedTemplate by mutableStateOf<Template?>(null)

    private val _exerciseRefs = mutableStateListOf<ExerciseRef>()
    var templates = mutableStateListOf<Template>()
    val filteredExercises: List<ExerciseRef>
        get() = _exerciseRefs
            .filter { it.name.contains(exerciseSearchText, ignoreCase = true) }
            .sortedWith(
                if (isSortedAlphabetically)
                    compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                else
                    compareByDescending(String.CASE_INSENSITIVE_ORDER) { it.name }
            )

    val groupedWorkouts: Map<String, List<Workout>>
        get() = workouts.value.sortedWith(
            compareByDescending { it.date }
        ).groupBy {
            epochToMonthYear(it.date)
        }
    var workoutVisible by mutableStateOf(false)
        private set
    var templateVisible by mutableStateOf(false)
        private set
    // whether the choose exercise screen is visible or not
    var addExerciseVisible by mutableStateOf(false)
        private set
    // controls the header for choosing an exercise screen
    var isExerciseSearchMode by mutableStateOf(false)
        private set
    // text for the search bar
    var exerciseSearchText by mutableStateOf("")
    // controls whether the exercise list is sorted alphabetically or reverse alphabetically
    var isSortedAlphabetically by mutableStateOf(true)
    private var showCancelWorkoutDialog by mutableStateOf(false)
    private var showCancelTemplateDialog by mutableStateOf(false)
    private var showDeleteWorkoutExerciseDialog by mutableStateOf(false)
    private var showDeleteTemplateExerciseDialog by mutableStateOf(false)
    var showDeleteTemplateDialog by mutableStateOf(false)
        private set
    var showDeleteCustomExerciseDialog by mutableStateOf(false)
        private set
    var showAddNewCustomExerciseDialog by mutableStateOf(false)
        private set
    var showEditCustomExerciseDialog by mutableStateOf(false)
        private set
    var dialogErrorMessage by mutableStateOf<String?>("")
        private set
    var dialogButtonsEnabled by mutableStateOf(true)
        private set
    var currentHistoryWorkout: Workout? = null
        private set
    var showCurrentWorkout by mutableStateOf(false)
        private set
    var workouts = mutableStateOf<List<Workout>>(listOf())
    private var workoutIndex = 0
    private val workoutOrder: List<WorkoutState> = listOf(
        WorkoutState.NEW_WORKOUT,
        WorkoutState.CURRENT_WORKOUT,
        WorkoutState.SUMMARY
    )
    var workoutScreenData by mutableStateOf(createWorkoutScreenData())
    var showChangeWorkoutNameDialog by mutableStateOf(false)
    var showChangeTemplateNameDialog by mutableStateOf(false)
    var tempWorkoutName by mutableStateOf("")
    var tempTemplateName by mutableStateOf("")
    var seconds by mutableIntStateOf(0)
    var isHistoryRefreshing by mutableStateOf(false)

    var mePrs by mutableStateOf<Map<String, Int>?>(null)
    var mePrsId by mutableIntStateOf(0)

    fun resetState(){
        currentWorkout = Workout()
        currentTemplate = Template()
        newExerciseRef = ExerciseRef(
            name = "",
            id = -1,
            durationVSReps = true,
            weight = true,
            distance = false,
            isCustom = true,
        )
        selectedExerciseIndex = -1
        selectedExercise = null
        selectedTemplate = null
        workoutVisible = false
        templateVisible = false
        addExerciseVisible = false
        isExerciseSearchMode = false
        exerciseSearchText = ""
        isSortedAlphabetically = true
        showCancelWorkoutDialog = false
        showCancelTemplateDialog = false
        showDeleteWorkoutExerciseDialog = false
        showDeleteTemplateExerciseDialog = false
        showDeleteTemplateDialog = false
        showDeleteCustomExerciseDialog = false
        showAddNewCustomExerciseDialog = false
        showEditCustomExerciseDialog = false
        dialogErrorMessage = ""
        dialogButtonsEnabled = true
        currentHistoryWorkout = null
        showCurrentWorkout = false
        workouts = mutableStateOf(listOf())
        workoutIndex = 0
        workoutScreenData = createWorkoutScreenData()
        showChangeWorkoutNameDialog = false
        showChangeTemplateNameDialog = false
        tempWorkoutName = ""
        tempTemplateName = ""
        seconds = 0
        isHistoryRefreshing = false

        getWorkoutsByUserId()
        fetchCombinedExercises()
    }

    fun onBackPressed(){
        if (workoutVisible){
            if (workoutScreenData.screen == WorkoutState.NEW_WORKOUT){
                workoutVisible = false
            }
            else if (addExerciseVisible){
                addExerciseVisible = false
            }
        }
        else if (showCurrentWorkout) {
            showCurrentWorkout = false
            currentHistoryWorkout = null
        }
    }
    fun showErrorDialog(): Boolean{
        return showCancelWorkoutDialog || showCancelTemplateDialog
                || showDeleteWorkoutExerciseDialog || showDeleteTemplateExerciseDialog
                || showDeleteCustomExerciseDialog || showDeleteTemplateDialog
    }

    fun getDialogText(): String{
        return if (showCancelWorkoutDialog)
            "Cancel Workout"
        else if (showCancelTemplateDialog)
            "Cancel Template"
        else if (showDeleteWorkoutExerciseDialog || showDeleteTemplateExerciseDialog || showDeleteCustomExerciseDialog)
            "Delete Exercise"
        else if (showDeleteTemplateDialog)
            "Delete Template"
        else
            ""
    }

    fun getDialogBodyText(): String{
        return if (showCancelWorkoutDialog)
            "Are you sure you want to cancel this workout?"
        else if (showCancelTemplateDialog)
            "Are you sure you want to cancel this template?"
        else if (showDeleteWorkoutExerciseDialog || showDeleteTemplateExerciseDialog)
            "Are you sure you want to delete this exercise?"
        else if (showDeleteCustomExerciseDialog)
            "Are you sure you want to delete ${selectedExercise?.name}?"
        else if (showDeleteTemplateDialog)
            "Are you sure you want to delete ${selectedTemplate?.name}?"
        else
            ""
    }

    fun onDialogConfirm(){
        if (showCancelWorkoutDialog){
            workoutVisible = false
            showCancelWorkoutDialog = false
            workoutIndex = 0
            workoutScreenData = createWorkoutScreenData()
        }
        else if (showCancelTemplateDialog){
            templateVisible = false
            showCancelTemplateDialog = false
        }
        else if (showDeleteWorkoutExerciseDialog) {
            if (selectedExerciseIndex != -1) {
                removeWorkoutExercise(selectedExerciseIndex)
                selectedExerciseIndex = -1
            }
            showDeleteWorkoutExerciseDialog = false
        }
        else if (showDeleteTemplateExerciseDialog) {
            if (selectedExerciseIndex != -1) {
                removeTemplateExercise(selectedExerciseIndex)
                selectedExerciseIndex = -1
            }
            showDeleteTemplateExerciseDialog = false
        }
        else if (showDeleteCustomExerciseDialog){
            updateDialogButtonsEnabled(false)
            selectedExercise?.let { deleteCustomExercise(it) }
        }
        else if (showDeleteTemplateDialog){
            updateDialogButtonsEnabled(false)
            selectedTemplate?.let { deleteTemplate(it) }
        }
    }

    fun onDialogDismiss(){
        if (showCancelWorkoutDialog)
            showCancelWorkoutDialog = false
        else if (showCancelTemplateDialog)
            showCancelTemplateDialog = false
        else if (showDeleteWorkoutExerciseDialog){
            selectedExerciseIndex = -1
            showDeleteWorkoutExerciseDialog = false
        }
        else if (showDeleteTemplateExerciseDialog){
            selectedExerciseIndex = -1
            showDeleteTemplateExerciseDialog = false
        }
        else if (showDeleteCustomExerciseDialog){
            selectedExercise = null
            dialogErrorMessage = null
            showDeleteCustomExerciseDialog = false
            dialogButtonsEnabled = true
        }
        else if (showDeleteTemplateDialog){
            selectedTemplate = null
            dialogErrorMessage = null
            showDeleteTemplateDialog = false
            dialogButtonsEnabled = true
        }
    }

    fun updateScreenState(next: Boolean){
        if (next){
            if (workoutIndex == 2) {
                workoutIndex = 0
                workoutVisible = false
            }
            else
                workoutIndex++
        }
        else {
            if (workoutIndex != 0 && workoutIndex != 2)
                workoutIndex--
        }
        workoutScreenData = createWorkoutScreenData()
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

    fun finishTemplate(){
        RetrofitInstance.templateService.createTemplate(
            CreateTemplateRequest(
                name = currentTemplate.name,
            )
        ).enqueue(object:
            Callback<ApiResponse<CreateTemplateResponse>> {
            override fun onResponse(
                call: Call<ApiResponse<CreateTemplateResponse>>,
                response: Response<ApiResponse<CreateTemplateResponse>>
            ) {
                if (response.isSuccessful) {
                    // Handle successful response
                    val templateResponse = response.body()?.data
                    templateResponse?.let {
                        val templateId = it.templateId
                        for (exercise in currentTemplate.exercises) { //populate exercises
                            createTemplateExercises(exercise, templateId)
                        }
                    }
                    templates.add(currentTemplate) // shouldnt do this but whatever.... lol...
                }
            }

            override fun onFailure(call: Call<ApiResponse<CreateTemplateResponse>>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    fun deleteTemplate(template: Template) {
        RetrofitInstance.templateService.deleteTemplate(template.id.toString()).enqueue(object : Callback<ApiResponse<String>> {
            override fun onResponse(
                call: Call<ApiResponse<String>>,
                response: Response<ApiResponse<String>>
            ) {
                if (response.isSuccessful) {
                    templates.remove(template)
                    showDeleteTemplateDialog = false
                } else {
                    dialogErrorMessage = "There was an error deleting the template"
                }

                dialogButtonsEnabled = true
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                println("Failure: ${t.message}")
            }
        })
    }

    fun finishWorkout(){
        currentWorkout.duration = seconds
        currentWorkout.date = System.currentTimeMillis()
        RetrofitInstance.workoutService.createWorkout(
            CreateWorkoutRequest(
                name = currentWorkout.name,
                date = currentWorkout.date,
                duration = currentWorkout.duration
            )
        ).enqueue(object :
            Callback<ApiResponse<CreateWorkoutResponse>> {
            override fun onResponse(
                call: Call<ApiResponse<CreateWorkoutResponse>>,
                response: Response<ApiResponse<CreateWorkoutResponse>>
            ) {
                if (response.isSuccessful) {
                    // Handle successful response
                    val workoutResponse = response.body()?.data
                    workoutResponse?.let {
                        val workoutId = it.workoutId
                        for (exercise in currentWorkout.exercises){ //populate exercises
                            createWorkoutExercises(exercise, workoutId)
                        }
                    }
                    println("Workout created successfully" )
                } else {
                    // Handle error response
                    println("Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<CreateWorkoutResponse>>, t: Throwable) {
                println("Failure: ${t.message}")
            }
        }
        )
    }

    private fun createWorkoutExercises(exercise: ExerciseIteration, workoutId: Int){
        val apiService = RetrofitInstance.exerciseService
        val exerciseDataSet = mutableListOf<ExerciseDataSet>()
        val exercisePR = mePrs?.get(exercise.exerciseRef.name)
        var value = 0
        exercise.exerciseSet.forEach{
            exerciseDataSet.add(ExerciseDataSet(translateWeight(it.weight), it.reps, it.duration, translateDistance(it.distance)))
            if (calculateScore(it) > value)
                value = calculateScore(it)
        }

        if (exercisePR != null && value > exercisePR)
            updatePersonalBest(value, exercise.exerciseRef.id)
        else
            updatePersonalBest(value, exercise.exerciseRef.id)

        val call = apiService.createExercise(ExerciseRequest(
            workoutId = workoutId,
            templateId = null,
            exerciseRefId = if (exercise.exerciseRef.isCustom) null else exercise.exerciseRef.id,
            customExerciseRefId = if (exercise.exerciseRef.isCustom) exercise.exerciseRef.id else null,
            exerciseSet = exerciseDataSet
        ))

        call.enqueue(object: Callback<ApiResponse<Boolean>>{
            override fun onResponse(call: Call<ApiResponse<Boolean>>, response: Response<ApiResponse<Boolean>>) {
                if (response.isSuccessful) {
                    // Handle successful response
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

    private fun createTemplateExercises(exercise: ExerciseIteration, templateId: Int){
        val apiService = RetrofitInstance.exerciseService
        val exerciseDataSet = mutableListOf<ExerciseDataSet>()
        val exercisePR = mePrs?.get(exercise.exerciseRef.name)
        var value = 0
        exercise.exerciseSet.forEach{
            exerciseDataSet.add(ExerciseDataSet(translateWeight(it.weight), it.reps, it.duration, translateDistance(it.distance)))
            if (calculateScore(it) > value)
                value = calculateScore(it)
        }

        if (exercisePR != null && value > exercisePR)
            updatePersonalBest(value, exercise.exerciseRef.id)
        else
            updatePersonalBest(value, exercise.exerciseRef.id)

        val call = apiService.createExercise(ExerciseRequest(
            workoutId = null,
            templateId = templateId,
            exerciseRefId = if (exercise.exerciseRef.isCustom) null else exercise.exerciseRef.id,
            customExerciseRefId = if (exercise.exerciseRef.isCustom) exercise.exerciseRef.id else null,
            exerciseSet = exerciseDataSet
        ))
        call.enqueue(object: Callback<ApiResponse<Boolean>>{
            override fun onResponse(call: Call<ApiResponse<Boolean>>, response: Response<ApiResponse<Boolean>>) {
                if (response.isSuccessful) {
                    // Handle successful response
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

    private fun updatePersonalBest(value: Int, exerciseRefIndex: Int){
        RetrofitInstance.userPrsService.updateUserPr(exerciseRefIndex.toString(), UpdateUserPrRequest(value)).enqueue(object: Callback<ApiResponse<String>>{
            override fun onResponse(
                call: Call<ApiResponse<String>>,
                response: Response<ApiResponse<String>>
            ) {
                println("PR $exerciseRefIndex updated")
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                t.printStackTrace()
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
                            _exerciseRefs.add(ExerciseRef(exercise.name, exercise.id, exercise.durationVSReps, exercise.weight, exercise.distance, exercise.isCustom))
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<ExerciseRef>>>, t: Throwable) {
                t.printStackTrace()

            }
        })
    }

    fun fetchTemplates() {
        templates.clear()
        RetrofitInstance.templateService.getTemplates().enqueue(object: Callback<ApiResponse<List<GetTemplateResponse>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<GetTemplateResponse>>>,
                response: Response<ApiResponse<List<GetTemplateResponse>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        it.forEach { template->
                            val newExercises = mutableListOf<ExerciseIteration>()

                            template.exercises.forEach{ exercise ->
                                val newSets = mutableListOf<ExerciseSet>()
                                exercise.exerciseSet.forEach { set ->

                                    newSets.add(ExerciseSet(
                                        initialReps = set.reps,
                                        initialDuration = set.duration,
                                        initialWeight = set.weight,
                                        initialDistance = set.distance
                                    ))
                                }
                                newExercises.add(
                                    ExerciseIteration(
                                        exerciseRef = exercise.exerciseRef,
                                        exerciseSet = newSets
                                    )
                                )
                            }

                            templates.add(
                                Template(
                                    id = template.id,
                                    initialName = template.name,
                                    exercises = newExercises
                                )
                            )
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<GetTemplateResponse>>>, t: Throwable) {
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

    private fun deleteCustomExercise(customExerciseRef: ExerciseRef) {
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

    fun getWorkoutsByUserId(onSuccess: () -> Unit = {}){
        val apiService = RetrofitInstance.workoutService
        val call = apiService.getWorkoutByUserId()

        call.enqueue(object : Callback<ApiResponse<List<GetWorkoutResponse>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<GetWorkoutResponse>>>,
                response: Response<ApiResponse<List<GetWorkoutResponse>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        val newWorkouts = mutableListOf<Workout>()
                        it.forEach { workout->
                            val newExercises = mutableListOf<ExerciseIteration>()
                            workout.exercises.forEach{ exercise ->
                                val newSets = mutableListOf<ExerciseSet>()
                                exercise.exerciseSet.forEach { set ->
                                    newSets.add(ExerciseSet(
                                        initialReps = set.reps,
                                        initialDuration = set.duration,
                                        initialWeight = set.weight,
                                        initialDistance = set.distance
                                    ))
                                }
                                newExercises.add(
                                    ExerciseIteration(
                                        exerciseRef = exercise.exerciseRef,
                                        exerciseSet = newSets
                                    )
                                )
                            }

                            newWorkouts.add(
                                Workout(
                                    id = workout.id,
                                    initialName = workout.name,
                                    date = workout.date,
                                    duration = workout.duration,
                                    exercises = newExercises
                                )
                            )
                            println(workout.id)
                        }
                        workouts.value = newWorkouts
                        onSuccess()
                    }
                }
                else{
                    println(response.parseErrorBody())
                }
            }

            override fun onFailure(
                call: Call<ApiResponse<List<GetWorkoutResponse>>>,
                t: Throwable
            ) {
                println("Failed to get workouts")
            }
        })
    }

    fun deleteWorkout(id: Int, onSuccess: () -> Unit){
        RetrofitInstance.workoutService.deleteWorkout(id).enqueue(object: Callback<ApiResponse<String>>{
            override fun onResponse(
                call: Call<ApiResponse<String>>,
                response: Response<ApiResponse<String>>
            ) {
                getWorkoutsByUserId()
                onSuccess()
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
            }
        })
    }

    private fun getPersonalBests(){
        RetrofitInstance.userPrsService.getAllUserPr().enqueue(object :
            Callback<ApiResponse<Map<String, Int>>>{
            override fun onResponse(
                call: Call<ApiResponse<Map<String, Int>>>,
                response: Response<ApiResponse<Map<String, Int>>>
            ) {
                if (response.isSuccessful){
                    mePrs = response.body()?.data
                    mePrsId = mePrs?.get("id")!!
                    mePrs = mePrs!!.filter { it.key != "id" && it.value != 0 }
                    println(mePrs)
                }
            }

            override fun onFailure(call: Call<ApiResponse<Map<String, Int>>>, t: Throwable) {
                t.printStackTrace()
            }

        }
        )
    }

    fun refreshHistory(){
        isHistoryRefreshing = true
        getWorkoutsByUserId(
            onSuccess = {
                isHistoryRefreshing = false
                println("Done")
            }
        )
    }

    fun updateTempWorkoutName(name: String){
        tempWorkoutName = name
    }
    fun updateTempTemplateName(name: String){
        tempTemplateName = name
    }
    fun updateShowChangeWorkoutNameDialog (state: Boolean){
        showChangeWorkoutNameDialog = state
    }
    fun updateShowChangeTemplateNameDialog (state: Boolean){
        showChangeTemplateNameDialog = state
    }
    fun setWorkoutName(){
        if (tempWorkoutName.length < 15) {
            currentWorkout.name = tempWorkoutName
            tempWorkoutName = ""
            showChangeWorkoutNameDialog = false
        } else {
            dialogErrorMessage = "Workout name must be less than 15 characters"
        }
    }
    fun setTemplateName(){
        if (tempTemplateName.length < 15) {
            currentTemplate.name = tempTemplateName
            tempTemplateName = ""
            showChangeTemplateNameDialog = false
        } else {
            dialogErrorMessage = "Template name must be less than 15 characters"
        }
    }
    private fun createWorkoutScreenData(): WorkoutScreenData {
        return WorkoutScreenData(workoutIndex, workoutOrder[workoutIndex])
    }

    fun resetWorkout(){
        currentWorkout.setWorkout()
        seconds = 0
    }
    private fun removeWorkoutExercise(exerciseIndex: Int){
        currentWorkout.removeExercise(exerciseIndex)
    }
    fun addWorkoutExercise(exerciseRef: ExerciseRef){
        currentWorkout.addExercise(exerciseRef)
    }
    fun addWorkoutSet(exerciseIndex: Int) {
        currentWorkout.addSet(exerciseIndex)
    }
    fun removeWorkoutSet(exerciseIndex: Int, setIndex: Int) {
        currentWorkout.removeSet(exerciseIndex, setIndex)
    }

    fun loadTemplateToWorkout(template: Template) {
        currentWorkout.setWorkout(
            Workout(
                initialName = template.name,
                exercises = template.exercises
            )
        )
    }

    fun resetTemplate(){
        currentTemplate.setTemplate()
    }
    private fun removeTemplateExercise(exerciseIndex: Int){
        currentTemplate.removeExercise(exerciseIndex)
    }
    fun addTemplateExercise(exerciseRef: ExerciseRef){
        currentTemplate.addExercise(exerciseRef)
    }
    fun addTemplateSet(exerciseIndex: Int) {
        currentTemplate.addSet(exerciseIndex)
    }
    fun removeTemplateSet(exerciseIndex: Int, setIndex: Int) {
        currentTemplate.removeSet(exerciseIndex, setIndex)
    }

    fun updateShowCurrentHistoryWorkout(state: Boolean){
        showCurrentWorkout = state
    }
    fun updateCurrentHistoryWorkout(workout: Workout){
        currentHistoryWorkout = workout
    }
    fun updateShowCancelWorkoutDialog(visible: Boolean){
        showCancelWorkoutDialog = visible
    }
    fun updateShowCancelTemplateDialog(visible: Boolean){
        showCancelTemplateDialog = visible
    }
    fun updateShowRemoveWorkoutExerciseDialog(visible: Boolean){
        showDeleteWorkoutExerciseDialog = visible
    }
    fun updateShowRemoveTemplateExerciseDialog(visible: Boolean){
        showDeleteTemplateExerciseDialog = visible
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
    fun updateShowDeleteTemplateDialog(visible: Boolean){
        showDeleteTemplateDialog = visible
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

    fun setWorkoutScreenVisible(visible: Boolean) {
        workoutVisible = visible
    }
    fun setTemplateScreenVisible(visible: Boolean) {
        templateVisible = visible
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

    enum class WorkoutState {
        NEW_WORKOUT,
        CURRENT_WORKOUT,
        SUMMARY
    }

    data class WorkoutScreenData (
        val screenIndex: Int,
        val screen: WorkoutState
    )

    init {
        if (AppPreferences.accessToken != null) {
            getWorkoutsByUserId()
            fetchCombinedExercises()
            getPersonalBests()
        }
    }
}