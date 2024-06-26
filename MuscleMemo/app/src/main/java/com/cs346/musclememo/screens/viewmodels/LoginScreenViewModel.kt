package com.cs346.musclememo.screens.viewmodels

import RetrofitInstance
import android.util.Log
import androidx.lifecycle.ViewModel
import com.cs346.musclememo.classes.Users
import com.cs346.musclememo.screens.services.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginScreenViewModel : ViewModel() {
    fun loginAttempt(username: String, password: String, onSuccess: () -> Unit, onFailure: () -> Unit): Unit{
        // todo: implement account login

        RetrofitInstance.userService.getAuthentication(user = username, password = password).enqueue(object:
            Callback<Boolean>{
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (!response.isSuccessful) {
                    onFailure()
                    return
                }
                response.body()?.let {
                    if (it) {
                        onSuccess()
                    } else {
                        onFailure()
                    }
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                onFailure()
            }

        })
    }
}