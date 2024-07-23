package com.cs346.musclememo.screens.viewmodels

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.cs346.musclememo.api.RetrofitInstance
import androidx.lifecycle.ViewModel
import com.cs346.musclememo.api.services.FirebaseToken
import com.cs346.musclememo.api.services.ImageUploadService
import com.cs346.musclememo.utils.AppPreferences
import com.cs346.musclememo.api.services.LoginRequest
import com.cs346.musclememo.api.services.LoginResponse
import com.cs346.musclememo.api.services.PasswordResetAttemptRequest
import com.cs346.musclememo.api.services.PasswordResetRequest
import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.api.types.parseErrorBody
import com.cs346.musclememo.classes.User
import com.cs346.musclememo.screens.services.SignupRequest
import com.cs346.musclememo.utils.Conversions.sliderToExperience
import com.google.firebase.messaging.FirebaseMessaging
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

    // Signup Screen States
    private val signupOrder: List<SignupState> = listOf(
        SignupState.USERNAME_EMAIL,
        SignupState.PASSWORD,
        SignupState.BASIC_INFO,
        SignupState.PFP
    )

    private var signupIndex = 0
    var signupScreenData by mutableStateOf(createSignupScreenData())

    // Login Screen States
    private val loginOrder: List<LoginState> = listOf(
        LoginState.LOGIN,
        LoginState.EMAIL_VERIFICATION,
        LoginState.CODE_VERIFICATION,
        LoginState.RESET_PASSWORD
    )

    private var loginIndex = 0
    var loginScreenData by mutableStateOf(createLoginScreenData())
    var verificationCode by mutableStateOf("")
        private set



    private val specialPattern = "[^A-Za-z0-9]"
    private val digitPattern = "\\d"

    var gender by mutableStateOf("")
        private set

    var customGender by mutableStateOf("")

    var genderExpanded by mutableStateOf(false)
        private set

    var sliderPosition by mutableFloatStateOf(0f)
        private set

    var profilePicture by mutableStateOf<String?>(null)
        private set

    fun updateVerificationCode(code: String){
        verificationCode = code
    }

    fun updateCustomGender(gender: String) {
        customGender = gender
    }

    fun updateProfilePicture(uri: Uri?) {
        if (uri != null){
            val imageUploader = ImageUploadService()
            imageUploader.uploadImage({ res ->
                profilePicture = res
            }, uri)
        }
    }

    fun updateSliderPosition(pos: Float) {
        sliderPosition = pos
    }

    fun updateGenderExpanded(update: Boolean) {
        genderExpanded = update
    }

    fun updateGender(newGender: String) {
        gender = newGender
    }

    fun setLoginScreenVisible(visible: Boolean) {
        loginVisible = visible
    }

    fun setSignupScreenVisible(visible: Boolean) {
        signupVisible = visible
    }

    fun onBackPressed() {
        errorMessage = ""
        if (loginVisible) {
            if (loginIndex == 0){
                loginVisible = false
                email = ""
                password = ""
            } else {
                updateLoginState(false)
                when (loginIndex){
                    1 -> {
                        email = ""
                    }
                    2 -> {
                        verificationCode = ""
                    }
                    3 -> {
                        password = ""
                        passwordCheck = ""
                        verificationCode = ""
                    }
                }
            }
        } else if (signupVisible) {
            if (signupIndex == 0) {
                signupVisible = false
                username = ""
                email = ""
            } else {
                updateSignupState(false)
                when (signupIndex) {
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

    fun updateLoginState(next: Boolean){
        if (next) {
            when (loginIndex) {
                0 -> loginIndex++
                1 -> checkEmail(email, onSuccess = {
                    loginIndex++
                    loginScreenData = createLoginScreenData()
                    sendVerificationCode()
                },
                    onFailure = {
                        errorMessage = "Email does not exist"
                    }
                )
                2 -> {
                    verifyVerificationCode(
                        onSuccess = {
                            email = ""
                            loginIndex++
                            loginScreenData = createLoginScreenData()
                        }
                    )
                }
                3 -> {
                    resetPassword(
                        onSuccess = {
                            loginIndex = 0
                            verificationCode = ""
                            loginScreenData = createLoginScreenData()
                        }
                    )
                }
            }
        }
        else {
            if (loginIndex == 3)
                loginIndex = 0
            else if (loginIndex != 0)
                loginIndex--
        }

        loginScreenData = createLoginScreenData()
    }

    fun updateUsername(name: String) {
        username = name
    }

    fun updatePassword(pass: String) {
        password = pass
    }

    fun updateEmail(address: String) {
        email = address
    }

    fun updatePasswordCheck(password: String) {
        passwordCheck = password
    }

    fun updateSignupState(next: Boolean) {
        if (next) {
            errorMessage = ""
            if (signupScreenData.question == SignupState.USERNAME_EMAIL) {
                if (username == "") {
                    errorMessage = "Please enter a username."
                } else if (email == "") {
                    errorMessage = "Please enter an email."
                } else {
                    checkEmail(email,
                        onSuccess = {
                            errorMessage = "Email already exists"
                        },
                        onFailure = {signupIndex++
                            signupScreenData = createSignupScreenData()
                        })
                }
            } else if (signupScreenData.question == SignupState.PASSWORD) {
                if ((password != "") and containsCapLower() and containsSpecialChar() and containsDigit() and minLength()) {
                    if ((password == passwordCheck))
                        signupIndex++
                    else {
                        errorMessage = "The passwords don't match. Try again."
                        passwordCheck = ""
                    }
                } else {
                    errorMessage = "Please make a valid password."
                    passwordCheck = ""
                }
            } else if (signupScreenData.question == SignupState.BASIC_INFO) {
                if (gender != "") {
                    if ((gender == "Custom") and (customGender == ""))
                        errorMessage = "Please input your custom gender."
                    else
                        signupIndex++
                } else
                    errorMessage = "Please select a gender."
            } else
                signupIndex++
        } else
            signupIndex--
        signupScreenData = createSignupScreenData()
    }

    fun containsCapLower(): Boolean {
        return password.uppercase() != password && password.lowercase() != password
    }

    fun containsSpecialChar(): Boolean {
        return Regex(specialPattern).find(password) != null
    }

    fun containsDigit(): Boolean {
        return Regex(digitPattern).find(password) != null
    }

    fun minLength(): Boolean {
        return password.length >= 8
    }

    private fun createSignupScreenData(): SignupScreenData {
        return SignupScreenData(signupIndex, signupOrder[signupIndex])
    }

    private fun createLoginScreenData(): LoginScreenData {
        return LoginScreenData(loginIndex, loginOrder[loginIndex])
    }

    private fun resetPassword(onSuccess: () -> Unit){
        RetrofitInstance.passwordResetService.resetPassword(verificationCode, PasswordResetRequest(password)).enqueue(
            object : Callback<ApiResponse<String>>{
                override fun onResponse(
                    call: Call<ApiResponse<String>>,
                    response: Response<ApiResponse<String>>
                ) {
                    if (response.isSuccessful){
                        onSuccess()
                    }
                    else {
                        errorMessage = "Please try again."
                    }
                    password = ""
                    passwordCheck = ""
                }

                override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                    t.printStackTrace()
                    errorMessage = "Failed to connect to server"
                }

            }
        )
    }

    private fun verifyVerificationCode(onSuccess: () -> Unit){
        RetrofitInstance.passwordResetService.verifyCode(verificationCode, PasswordResetAttemptRequest(email)).enqueue(
            object : Callback<ApiResponse<String>>{
                override fun onResponse(
                    call: Call<ApiResponse<String>>,
                    response: Response<ApiResponse<String>>
                ) {
                    if (response.isSuccessful){
                        onSuccess()
                    }
                    else
                        errorMessage = response.body()?.message ?: "Code is wrong"
                }

                override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                    t.printStackTrace()
                    errorMessage = "Failed to connect to server"
                }

            }
        )
    }

    private fun sendVerificationCode(){
        RetrofitInstance.passwordResetService.requestPasswordReset(PasswordResetAttemptRequest(email)).enqueue(
            object : Callback<ApiResponse<String>>{
                override fun onResponse(
                    call: Call<ApiResponse<String>>,
                    response: Response<ApiResponse<String>>
                ) {
                    if (!response.isSuccessful)
                        errorMessage = response.body()?.message ?: "Try again"
                }

                override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                    errorMessage = "Failed to connect to server"
                }

            }
        )
    }

    private fun checkEmail(email: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        // official RFC 5322 standard
        val emailRegex = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
        if (Regex(emailRegex).find(email) != null) {
            RetrofitInstance.signupService.checkEmail(email)
                .enqueue(object : Callback<ApiResponse<Boolean>> {
                    override fun onResponse(
                        call: Call<ApiResponse<Boolean>>,
                        response: Response<ApiResponse<Boolean>>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body()?.data == true)
                                onSuccess()
                            else
                                onFailure()
                        } else {
                            errorMessage = "Bad request."
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse<Boolean>>, t: Throwable) {
                        t.printStackTrace()
                        errorMessage = "Failed to connect to server."
                    }
                })
        }
        else
            errorMessage = "Please enter a proper email."
    }

    fun loginAttempt(onSuccess: () -> Unit, onFailure: () -> Unit = {}) {
        RetrofitInstance.userService.getAuthentication(LoginRequest(email, password))
            .enqueue(object :
                Callback<ApiResponse<LoginResponse>> {
                override fun onResponse(
                    call: Call<ApiResponse<LoginResponse>>,
                    response: Response<ApiResponse<LoginResponse>>
                ) {
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
                        sendFireBaseToken()
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

    fun createAccountAttempt(onSuccess: () -> Unit = {}, onFailure: () -> Unit = {}) {
        RetrofitInstance.signupService.createAccount(
            SignupRequest(
                username,
                password,
                email,
                if (gender === "Custom") customGender else gender,
                sliderToExperience(sliderPosition),
                profilePicture,
            )
        ).enqueue(object :
            Callback<ApiResponse<String>> {
            override fun onResponse(
                call: Call<ApiResponse<String>>,
                response: Response<ApiResponse<String>>
            ) {
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

    fun sendFireBaseToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                println("FCM Registration token: $token")
                AppPreferences.firebaseToken = token
                // Send token to your backend server if needed
                RetrofitInstance.userService.updateUserTokens(FirebaseToken(firebaseTokens = token))
                    .enqueue(object: Callback<ApiResponse<User>>{
                        override fun onResponse(
                            call: Call<ApiResponse<User>>,
                            response: Response<ApiResponse<User>>
                        ) {
                            println(response.body())
                        }

                        override fun onFailure(call: Call<ApiResponse<User>>, t: Throwable) {
                            t.printStackTrace()
                        }

                    } )
                //sendTokenToServer(token)
            } else {
                //Log.w(TAG, "Fetching FCM registration token failed", task.exception)
            }
        }

    }

    enum class SignupState {
        USERNAME_EMAIL,
        PASSWORD,
        BASIC_INFO,
        PFP
    }

    data class SignupScreenData(
        val signupIndex: Int,
        val question: SignupState
    )

    enum class LoginState {
        LOGIN,
        EMAIL_VERIFICATION,
        CODE_VERIFICATION,
        RESET_PASSWORD
    }

    data class LoginScreenData(
        val loginIndex: Int,
        val question: LoginState
    )
}