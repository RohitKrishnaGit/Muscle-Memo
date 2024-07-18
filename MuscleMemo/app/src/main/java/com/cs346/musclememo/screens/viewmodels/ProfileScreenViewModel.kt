package com.cs346.musclememo.screens.viewmodels

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cs346.musclememo.api.RetrofitInstance
import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.User
import com.cs346.musclememo.utils.AppPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileScreenViewModel : ViewModel() {
    var user by mutableStateOf(User(0, "Blazefire878", "blazefire878@gmail.com", null, null, "Male", "Intermediate"))
        private set
    var showSettings by mutableStateOf(false)
        private set
    val listOfMeasurementWeight = listOf("kg", "lb")
    var showWeightOptions by mutableStateOf(false)
    val listOfMeasurementDistance = listOf("km", "miles")
    var showDistanceOptions by mutableStateOf(false)
        private set
    val listOfTheme = listOf("Auto", "Dark", "Light")
    var showThemeOptions by mutableStateOf(false)
        private set
    var newProfilePicture by mutableStateOf<Uri?>(null)
        private set
    var newUsername by mutableStateOf("")
        private set
    var newGender by mutableStateOf("")
        private set
    var newCustomGender by mutableStateOf("")
        private set
    var genderExpanded by mutableStateOf(false)
        private set
    var newExperience by mutableFloatStateOf(0.0f)
        private set
    var editEnabled by mutableStateOf(false)
        private set

    private fun getExperience(value: Float): String{
        return if (value == 1.0f)
            "Professional"
        else if (value == 0.5f)
            "Intermediate"
        else
            "Novice"
    }

    fun getSliderPos(experience: String): Float {
        return if (experience == "Professional")
            1.0f
        else if (experience == "Intermediate")
            0.5f
        else
            0.0f
    }

    private fun customGender(): String{
        if (user.gender != "Male" && user.gender != "Female" && user.gender != "Rather Not Say")
            return "Custom"
        else
            return user.gender
    }

    fun refreshNewUser(){
        newUsername = user.username
        newGender = customGender()
        newCustomGender = if (customGender() == "Custom") user.gender else ""
        newProfilePicture = user.profilePicture
        newExperience = getSliderPos(user.experience)
    }

    fun getNewUser(): User {
        return User(
            id = user.id,
            username = newUsername,
            email = user.email,
            workouts = user.workouts,
            profilePicture = newProfilePicture,
            gender = if (newGender == "Custom") newCustomGender else newGender,
            experience = getExperience(newExperience)
        )
    }

    fun updateEditEnabled (state: Boolean){
        editEnabled = state
    }

    fun updateNewExperience (value: Float){
        newExperience = value
    }

    fun updateNewGender(gender: String){
        newGender = gender
    }

    fun updateNewCustomGender(gender: String){
        newCustomGender = gender
    }

    fun updateGenderExpanded(state: Boolean){
        genderExpanded = state
    }

    fun updateNewUsername (username: String){
        newUsername = username
    }

    fun updateShowThemeOptions(state: Boolean){
        showThemeOptions = state
    }

    fun updateNewProfilePicture (uri: Uri?){
        newProfilePicture = uri
    }

    fun getTheme(): String {
        if (AppPreferences.darkMode == true)
            return "Dark"
        else if (AppPreferences.darkMode == false)
            return "Light"
        else
            return "Auto"
    }

    fun updateTheme(theme: String){
        if (theme == "Auto")
            AppPreferences.darkMode = null
        else if (theme == "Dark")
            AppPreferences.darkMode = true
        else
            AppPreferences.darkMode = false
    }

    fun updateShowWeightOptions(state: Boolean){
        showWeightOptions = state
    }

    fun updateSystemWeight(unit: String){
        AppPreferences.systemOfMeasurementWeight = unit
        println(AppPreferences.systemOfMeasurementWeight )
    }

    fun updateShowDistanceOptions (state: Boolean){
        showDistanceOptions = state
    }

    fun updateSystemDistance (unit: String){
        AppPreferences.systemOfMeasurementDistance = unit
    }

    fun updateShowSettings(show: Boolean){
        showSettings = show
    }

    fun updateUser(){
        user = getNewUser()
        refreshNewUser()
        //TODO: call endpoints from here with attributes in user
    }

    fun signoutAllDevices (){
        RetrofitInstance.userService.signoutAllDevices().enqueue(object:
        Callback<ApiResponse<String>>{
            override fun onResponse(call: Call<ApiResponse<String>>, response: Response<ApiResponse<String>>) {
                return
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                return
            }
        })
    }

    init {
        //TODO: get user's name and details
    }
}