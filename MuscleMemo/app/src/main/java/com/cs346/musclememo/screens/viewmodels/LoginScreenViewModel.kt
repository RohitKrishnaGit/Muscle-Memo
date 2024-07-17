package com.cs346.musclememo.screens.viewmodels

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.cs346.musclememo.api.RetrofitInstance
import androidx.lifecycle.ViewModel
import com.cs346.musclememo.utils.AppPreferences
import com.cs346.musclememo.api.services.LoginRequest
import com.cs346.musclememo.api.services.LoginResponse
import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.api.types.parseErrorBody
import com.cs346.musclememo.screens.services.SignupRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginScreenViewModel : ViewModel() {
    var signupVisible by mutableStateOf(false)
        private set
    var loginVisible by mutableStateOf(false)
        private set
    var username by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var passwordCheck by mutableStateOf("")
        private set
    var errorMessage by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set

    // Login Screen States
    private val loginOrder: List<LoginState> = listOf(
        LoginState.USERNAME_EMAIL,
        LoginState.PASSWORD,
        LoginState.BASIC_INFO,
        LoginState.PFP
    )

    private var loginIndex = 0
    var loginScreenData by mutableStateOf(createLoginScreenData())

    private val specialPattern = "[^A-Za-z0-9]"
    private val digitPattern = "\\d"

    var gender by mutableStateOf("")
        private set
    val genders: List<String> = listOf(
        "Male",
        "Female",
        "Rather Not Say",
        "Custom"
    )

    var customGender by mutableStateOf("")

    var genderExpanded by mutableStateOf(false)
        private set

    var sliderPosition by mutableFloatStateOf(0f)
        private set

    var profilePicture by mutableStateOf<Uri?>(null)
        private set

    fun updateCustomGender(gender: String){
        customGender = gender
    }

    fun updateProfilePicture (uri: Uri?){
        profilePicture = uri
    }

    fun updateSliderPosition (pos: Float){
        sliderPosition = pos
    }

    fun updateGenderExpanded(update: Boolean){
        genderExpanded = update
    }

    fun updateGender(newGender: String){
        gender = newGender
    }

    fun setLoginScreenVisible (visible: Boolean){
        loginVisible = visible
    }

    fun setSignupScreenVisible (visible: Boolean){
        signupVisible = visible
    }

    fun onBackPressed(){
        errorMessage = ""
        if (loginVisible) {
            loginVisible = false
            email = ""
            password = ""
        }
        else if (signupVisible) {
            if (loginIndex == 0) {
                signupVisible = false
                username = ""
                email = ""
            }
            else {
                updateLoginState(false)
                when (loginIndex) {
                    1 -> {
                        password = ""
                        passwordCheck = ""
                    }
                    2 -> {
                        gender = ""
                        customGender = ""
                        sliderPosition = 0.0f
                    }
                    3 -> {
                        profilePicture = null
                    }
                }
            }
        }
    }

    fun updateUsername(name : String){
        username = name
    }

    fun updatePassword (pass: String){
        password = pass
    }

    fun updateEmail(address: String){
        email = address
    }

    fun updatePasswordCheck(password : String){
        passwordCheck = password
    }

    fun updateLoginState(next: Boolean){
        if (next) {
            errorMessage = ""
            if (loginScreenData.question == LoginState.USERNAME_EMAIL){
                if (username == ""){
                    errorMessage = "Please enter a username."
                } else if (email == ""){
                    errorMessage = "Please enter an email."
                } else {
                    //TODO email
                    checkEmailExistence(email)

                }
            }
            else if (loginScreenData.question == LoginState.PASSWORD){
                if ((password != "") and containsCapLower() and containsSpecialChar() and containsDigit() and minLength()) {
                    if ((password == passwordCheck))
                        loginIndex++
                    else {
                        errorMessage = "The passwords don't match. Try again."
                        passwordCheck = ""
                    }
                } else {
                    errorMessage = "Please make a valid password."
                    passwordCheck = ""
                }
            } else if (loginScreenData.question == LoginState.BASIC_INFO){
                if (gender != "") {
                    if ((gender == "Custom") and (customGender == ""))
                        errorMessage = "Please input your custom gender."
                    else
                        loginIndex++
                }
                else
                    errorMessage = "Please select a gender."
            }
            else
                loginIndex++
        }
        else
            loginIndex--
        loginScreenData = createLoginScreenData()
    }

    fun containsCapLower() : Boolean{
        return password.uppercase() != password && password.lowercase() != password
    }

    fun containsSpecialChar() : Boolean {
        return Regex(specialPattern).find(password) != null
    }

    fun containsDigit() : Boolean {
        return Regex(digitPattern).find(password) != null
    }

    fun minLength() : Boolean {
        return password.length >= 8
    }

    private fun createLoginScreenData(): LoginScreenData{
        return LoginScreenData(loginIndex, loginOrder[loginIndex])
    }

    fun loginAttempt(onSuccess: () -> Unit, onFailure: () -> Unit = {}){
        RetrofitInstance.userService.getAuthentication(LoginRequest(email, password)).enqueue(object:
            Callback<ApiResponse<LoginResponse>>{
            override fun onResponse(call: Call<ApiResponse<LoginResponse>>, response: Response<ApiResponse<LoginResponse>>) {
                if (!response.isSuccessful) {
                    println(response.parseErrorBody())
                    password = ""
                    errorMessage = "Invalid Credentials. Please try again."
                    onFailure()
                    return
                }
                response.body()?.data?.let {
                    AppPreferences.accessToken = it.accessToken
                    AppPreferences.refreshToken = it.refreshToken
                    onSuccess()
                }
            }

            override fun onFailure(call: Call<ApiResponse<LoginResponse>>, t: Throwable) {
                t.printStackTrace()
                password = ""
                errorMessage = "Invalid Credentials. Please try again."
                onFailure()
            }

        })
    }

    fun createAccountAttempt(onSuccess: () -> Unit = {}, onFailure: () -> Unit = {}){
        RetrofitInstance.signupService.createAccount(SignupRequest(username, password, email, username)).enqueue(object:
            Callback<ApiResponse<String>>{
            override fun onResponse(call: Call<ApiResponse<String>>, response: Response<ApiResponse<String>>) {
                if (!response.isSuccessful) {
                    println(response.body())
                    errorMessage = response.parseErrorBody()?.message ?: "Something went wrong"
                    onFailure()
                    return
                }
                response.body()?.let {
                    signupVisible = false
                    username = ""
                    password = ""
                    email = ""
                    gender = ""
                    customGender = ""
                    sliderPosition = 0.0f
                    loginVisible = true
                    onSuccess()
                }
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                t.printStackTrace()
                errorMessage = "Could not reach server"
                onFailure()
            }
        })
    }

    private fun checkEmailExistence (email: String){

        RetrofitInstance.signupService.checkEmail(email).enqueue(object: Callback<ApiResponse<Boolean>>{
            override fun onResponse(
                call: Call<ApiResponse<Boolean>>,
                response: Response<ApiResponse<Boolean>>
            ) {
                if (response.body()?.data == true){
                    errorMessage = "email already exists";
                }
                else{
                    loginIndex++
                }

            }

            override fun onFailure(call: Call<ApiResponse<Boolean>>, t: Throwable) {
                t.printStackTrace()
                errorMessage = "Failed to reach network";
            }

        })

    }

    enum class LoginState {
        USERNAME_EMAIL,
        PASSWORD,
        BASIC_INFO,
        PFP
    }

    data class LoginScreenData (
        val loginIndex: Int,
        val question: LoginState
    )
}