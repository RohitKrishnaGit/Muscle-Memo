package com.cs346.musclememo.screens.viewmodels

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cs346.musclememo.classes.User
import com.cs346.musclememo.utils.AppPreferences

class ProfileScreenViewModel : ViewModel() {
    var user by mutableStateOf(User(0, "Blazefire878", "blazefire878@gmail.com", null, null, "Male", "Novice"))
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




    init {
        // todo: get user's name and details
    }
}