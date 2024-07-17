package com.cs346.musclememo.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cs346.musclememo.api.RetrofitInstance
import com.cs346.musclememo.api.services.LoginRequest
import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileScreenViewModel : ViewModel() {
    var name by mutableStateOf("")
        private set

    fun setNewGender(gender: String){
        RetrofitInstance.userService.updateGender(gender).enqueue(object: Callback<ApiResponse<User>> {
            override fun onResponse(
                call: Call<ApiResponse<User>>,
                response: Response<ApiResponse<User>>
            ) {
                if (response.isSuccessful){
                    println("Gender updated successfully");
                }
            }

            override fun onFailure(call: Call<ApiResponse<User>>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    fun setNewExperienceLevel(experience: String){
        RetrofitInstance.userService.updateExperience(experience).enqueue(object: Callback<ApiResponse<User>> {
            override fun onResponse(
                call: Call<ApiResponse<User>>,
                response: Response<ApiResponse<User>>
            ) {
                if (response.isSuccessful){
                    println("Experience updated successfully");
                }
            }

            override fun onFailure(call: Call<ApiResponse<User>>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }


    init {
        // todo: get user's name and details
    }
}