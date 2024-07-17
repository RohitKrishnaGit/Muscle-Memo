package com.cs346.musclememo.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cs346.musclememo.classes.User
import com.cs346.musclememo.classes.Workout
import com.cs346.musclememo.utils.AppPreferences

class ProfileScreenViewModel : ViewModel() {
    var user by mutableStateOf(User(0, "Blazefire878", "blazefire878@gmail.com", null, null))
        private set
    var showSettings by mutableStateOf(false)
        private set
    var systemOfMeasurementWeight by mutableStateOf(AppPreferences.systemOfMeasurementWeight ?: "kg")
        private set
    val listOfMeasurementWeight = listOf("kg", "lb")
    var showWeightOptions by mutableStateOf(false)
    var systemOfMeasurementDistance by mutableStateOf(AppPreferences.systemOfMeasurementDistance ?: "km")
        private set
    val listOfMeasurementDistance = listOf("km", "miles")
    var showDistanceOptions by mutableStateOf(false)
        private set
    val listOfTheme = listOf("Auto", "Dark", "Light")
    var showThemeOptions by mutableStateOf(false)
        private set

    fun updateShowThemeOptions(state: Boolean){
        showThemeOptions = state
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