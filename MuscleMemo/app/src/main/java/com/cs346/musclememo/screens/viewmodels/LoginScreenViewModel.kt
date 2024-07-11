package com.cs346.musclememo.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.cs346.musclememo.api.RetrofitInstance
import androidx.lifecycle.ViewModel
import com.cs346.musclememo.utils.AppPreferences
import com.cs346.musclememo.api.services.LoginRequest
import com.cs346.musclememo.api.services.LoginResponse
import com.cs346.musclememo.screens.services.SignupRequest
import com.cs346.musclememo.screens.services.SignupResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginScreenViewModel : ViewModel() {
    var signupVisible by mutableStateOf(false)
        private set

    fun setSignupScreenVisible (visible: Boolean){
        signupVisible = visible
    }

    var loginVisible by mutableStateOf(false)
        private set

    fun setLoginScreenVisible (visible: Boolean){
        loginVisible = visible
    }

    fun loginAttempt(username: String, password: String, onSuccess: () -> Unit, onFailure: () -> Unit): Unit{
        RetrofitInstance.userService.getAuthentication(LoginRequest(username, password)).enqueue(object:
            Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (!response.isSuccessful) {
                    onFailure()
                    return
                }
                response.body()?.let {
                    AppPreferences.accessToken = it.accessToken
                    AppPreferences.refreshToken = it.refreshToken
                    println(it.refreshToken)
                    onSuccess()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                t.printStackTrace()
                onFailure()
            }

        })
    }

    fun createAccountAttempt(username: String, password: String, email: String, fullName: String, onSuccess: () -> Unit, onFailure: (error: String) -> Unit): Unit{
        RetrofitInstance.signupService.createAccount(SignupRequest(username, password, email, fullName)).enqueue(object:
            Callback<SignupResponse>{
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (!response.isSuccessful) {
                    onFailure(response.errorBody()?.string() ?: "Something went wrong")
                    return
                }
                response.body()?.let {
                    onSuccess()
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                t.printStackTrace()
                onFailure("Could not reach server")
            }

        })
    }
}