package com.cs346.musclememo.screens.viewmodels

import com.cs346.musclememo.api.RetrofitInstance
import androidx.lifecycle.ViewModel
import com.cs346.musclememo.utils.AppPreferences
import com.cs346.musclememo.api.services.LoginRequest
import com.cs346.musclememo.api.services.LoginResponse
import com.cs346.musclememo.api.types.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginScreenViewModel : ViewModel() {
    fun loginAttempt(username: String, password: String, onSuccess: () -> Unit, onFailure: () -> Unit): Unit{
        // todo: implement account login

        RetrofitInstance.userService.getAuthentication(LoginRequest(username, password)).enqueue(object:
            Callback<ApiResponse<LoginResponse>>{
            override fun onResponse(call: Call<ApiResponse<LoginResponse>>, response: Response<ApiResponse<LoginResponse>>) {
                if (!response.isSuccessful) {
                    onFailure()
                    return
                }
                response.body()?.data?.let {
                    AppPreferences.accessToken = it.accessToken
                    AppPreferences.refreshToken = it.refreshToken
                    println(it.refreshToken)
                    onSuccess()
                }
            }

            override fun onFailure(call: Call<ApiResponse<LoginResponse>>, t: Throwable) {
                t.printStackTrace()
                onFailure()
            }

        })
    }
}