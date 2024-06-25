package com.cs346.musclememo.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ProfileScreenViewModel : ViewModel() {
    var name by mutableStateOf("")
        private set

    init {
        // todo: get user's name and details
    }
}