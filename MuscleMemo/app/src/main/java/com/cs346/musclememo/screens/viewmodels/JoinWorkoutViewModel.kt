package com.cs346.musclememo.screens.viewmodels

import com.cs346.musclememo.api.RetrofitInstance
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cs346.musclememo.api.services.CreatePublicWorkout
import com.cs346.musclememo.api.services.ExerciseDataSet
import com.cs346.musclememo.api.services.ExerciseRequest
import com.cs346.musclememo.api.services.CreateWorkoutRequest
import com.cs346.musclememo.api.services.CreateWorkoutResponse
import com.cs346.musclememo.api.services.GetWorkoutResponse
import com.cs346.musclememo.api.services.CreateTemplateClass
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


class JoinWorkoutViewModel: ViewModel() {
    var createWorkoutVisible by mutableStateOf(false)
        private set

    var publicWorkoutTab by mutableStateOf("Search")

    var createWorkoutName by mutableStateOf("")
        private set

    var createWorkoutExperience by mutableStateOf("")
        private set

    var experienceExpanded by mutableStateOf(false)
        private set

    var createWorkoutDescription by mutableStateOf("")
        private set

    var showCreateError by mutableStateOf(false)
        private set

    fun updateCreateWorkoutVisible(visible: Boolean) {
        createWorkoutVisible = visible
    }

    fun updateWorkoutTab(tab: String) {
        publicWorkoutTab = tab
    }

    fun updateCreateWorkoutName(name: String) {
        createWorkoutName = name
    }

    fun updateCreateWorkoutExperience(experience: String) {
        createWorkoutExperience = experience
    }

    fun updateExperienceExpanded(expanded: Boolean) {
        experienceExpanded = expanded
    }

    fun updateCreateWorkoutDescription(experience: String) {
        createWorkoutDescription = experience
    }

    fun validateForm(): Boolean {
        val isValid = createWorkoutName.isNotBlank() && createWorkoutDescription.isNotBlank()
        showCreateError = !isValid
        return isValid
    }

    fun createWorkout() {
        val apiService = RetrofitInstance.publicWorkoutService
        val apiBody = CreatePublicWorkout(
            name = createWorkoutName,
            experience = createWorkoutExperience,
            description = createWorkoutDescription
        )
        val call = apiService.createPublicWorkout(apiBody)

        call.enqueue(object : Callback<ApiResponse<Int>> {
            override fun onResponse(call: Call<ApiResponse<Int>>, response: Response<ApiResponse<Int>>) {
                if (response.isSuccessful) {
                    println(response.body())
                } else {
                    println("Failed to get chat history: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<Int>>, t: Throwable) {
                println("Failed to get chat history: ${t.message}")
            }
        })
    }

    fun clearCreateForm() {
        createWorkoutName = ""
        createWorkoutExperience = ""
        createWorkoutDescription = ""
    }
}