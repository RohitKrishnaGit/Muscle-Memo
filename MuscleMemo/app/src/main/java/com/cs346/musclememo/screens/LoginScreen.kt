package com.cs346.musclememo.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs346.musclememo.R
import com.cs346.musclememo.screens.components.ChooseProfilePicture
import com.cs346.musclememo.screens.components.DisplayProfilePicture
import com.cs346.musclememo.screens.components.ChooseExperience
import com.cs346.musclememo.screens.components.ChooseGender
import com.cs346.musclememo.screens.components.InputSheet
import com.cs346.musclememo.screens.components.PasswordTextField
import com.cs346.musclememo.screens.components.ValidPasswordRequirement
import com.cs346.musclememo.screens.viewmodels.LoginScreenViewModel
import com.cs346.musclememo.utils.getTransitionDirection


@Composable
fun LoginScreen(
    onSuccessLogin: () -> Unit,
    viewModel: LoginScreenViewModel = viewModel<LoginScreenViewModel>()
) {
    BackHandler (viewModel.loginVisible or viewModel.signupVisible) {
        viewModel.onBackPressed()
    }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ){
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        Image(
            painter = painterResource(id = R.drawable.musclememo),
            contentDescription = "Login image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        Text(text = "Track your progress.", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Find friends.", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Improve.", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        Button(
            onClick = { viewModel.setSignupScreenVisible(true) },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text(text = "Sign up", fontSize = 16.sp)
        }
        Button(
            onClick = { viewModel.setLoginScreenVisible(true) },
            colors = ButtonDefaults.outlinedButtonColors(),
            modifier = Modifier.fillMaxWidth(0.7f)) {
            Text(text = "Log in", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
    }
    LoginAccountSheet(viewModel = viewModel, onSuccessLogin = onSuccessLogin)
    CreateAccountSheet(viewModel = viewModel)
}

@Composable
fun LoginAccountSheet(
    viewModel: LoginScreenViewModel,
    onSuccessLogin: () -> Unit
) {
    AnimatedVisibility(
        visible = viewModel.loginVisible,
        enter = slideInHorizontally(initialOffsetX = { it }),
        exit = slideOutHorizontally(targetOffsetX = { it })
    ) {
        AnimatedContent(
            targetState = viewModel.loginScreenData,
            transitionSpec = {
                val animationSpec: TweenSpec<IntOffset> = tween(300)

                val direction = getTransitionDirection(
                    initialIndex = initialState.loginIndex,
                    targetIndex = targetState.loginIndex,
                )

                slideIntoContainer(
                    towards = direction,
                    animationSpec = animationSpec,
                ) togetherWith slideOutOfContainer(
                    towards = direction,
                    animationSpec = animationSpec
                )
            },
            label = "surveyScreenDataAnimation"
        ) { targetState ->
            when (targetState.question) {
                LoginScreenViewModel.LoginState.LOGIN -> {
                    Login(viewModel = viewModel, onSuccessLogin = onSuccessLogin)
                }
                LoginScreenViewModel.LoginState.EMAIL_VERIFICATION -> {
                    EmailVerification(viewModel = viewModel)
                }
                LoginScreenViewModel.LoginState.CODE_VERIFICATION -> {
                    CodeVerification(viewModel = viewModel)
                }
                LoginScreenViewModel.LoginState.RESET_PASSWORD -> {
                    CreatePassword(
                        viewModel = viewModel,
                        title = "Reset Password",
                        next = {
                            viewModel.updateLoginState(true)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CodeVerification(viewModel: LoginScreenViewModel) {
    InputSheet(
        title = "Reset Password",
        subTitle = "Enter your code",
        setVisible = viewModel::onBackPressed,
        errorMessage = viewModel.errorMessage,
        next = { viewModel.updateLoginState(true)}
    ) {
        Text(text = "A code has been sent to your email")
        Spacer(modifier = Modifier.height(5.dp))
        OutlinedTextField(
            value = viewModel.verificationCode,
            onValueChange = { viewModel.updateVerificationCode(it) },
            label = {
                Text(text = "Code")
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun EmailVerification(viewModel: LoginScreenViewModel) {
    InputSheet(
        title = "Reset Password",
        subTitle = "Enter your email",
        setVisible = viewModel::onBackPressed,
        errorMessage = viewModel.errorMessage,
        next = { viewModel.updateLoginState(true)}
    ) {
        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.updateEmail(it) },
            label = {
                Text(text = "Email")
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun Login (
    viewModel: LoginScreenViewModel, 
    onSuccessLogin: () -> Unit
){
    InputSheet(
        title = "Log in",
        subTitle = "Welcome Back",
        setVisible = viewModel::onBackPressed,
        buttonText = "Log in",
        errorMessage = viewModel.errorMessage,
        next = { viewModel.loginAttempt(onSuccess = onSuccessLogin) }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = {
                    viewModel.updateEmail(it)
                },
                label = {
                    Text(text = "Email")
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(1f)
            )

            // Password
            Spacer(modifier = Modifier.height(16.dp))
            PasswordTextField(
                password = viewModel.password,
                onValueChange = { viewModel.updatePassword(it) },
                modifier = Modifier.fillMaxWidth(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Forgot Password?", modifier = Modifier.clickable {
                viewModel.updateLoginState(true)
            })

        }
    }
}


@Composable
fun CreateAccountSheet(
    viewModel: LoginScreenViewModel
) {
    AnimatedVisibility(
        visible = viewModel.signupVisible,
        enter = slideInHorizontally(initialOffsetX = { it }),
        exit = slideOutHorizontally(targetOffsetX = { it })
    ) {
        AnimatedContent(
            targetState = viewModel.signupScreenData,
            transitionSpec = {
                val animationSpec: TweenSpec<IntOffset> = tween(300)

                val direction = getTransitionDirection(
                    initialIndex = initialState.signupIndex,
                    targetIndex = targetState.signupIndex,
                )

                slideIntoContainer(
                    towards = direction,
                    animationSpec = animationSpec,
                ) togetherWith slideOutOfContainer(
                    towards = direction,
                    animationSpec = animationSpec
                )
            },
            label = "surveyScreenDataAnimation"
        ) { targetState ->
            when (targetState.question) {
                LoginScreenViewModel.SignupState.USERNAME_EMAIL -> {
                    UsernameEmailSignup(viewModel)
                }

                LoginScreenViewModel.SignupState.PASSWORD -> {
                    CreatePassword(viewModel = viewModel, title = "Sign up", next = {viewModel.updateSignupState(true)})
                }

                LoginScreenViewModel.SignupState.BASIC_INFO -> {
                    BasicInformationSignup(viewModel)
                }
                
                LoginScreenViewModel.SignupState.PFP -> {
                    ChooseProfilePictureSignup(viewModel)
                }
            }
        }
    }
}

@Composable
fun UsernameEmailSignup(
    viewModel: LoginScreenViewModel
) {
    InputSheet(
        title = "Sign Up",
        subTitle = "Create an Account",
        setVisible = viewModel::onBackPressed,
        errorMessage = viewModel.errorMessage,
        next = {
        viewModel.updateSignupState(
            true
        )
    }) {
        OutlinedTextField(
            value = viewModel.username,
            onValueChange = {
                viewModel.updateUsername(it)
            },
            label = {
                Text(text = "Username")
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(1f)
        )

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = {
                viewModel.updateEmail(it)
            },
            label = {
                Text(text = "Email")
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(1f)
        )

    }

}

@Composable
fun CreatePassword(
    viewModel: LoginScreenViewModel,
    title: String,
    next: () -> Unit
) {
    InputSheet(
        title = title,
        subTitle = "Choose a Password",
        setVisible = viewModel::onBackPressed,
        errorMessage = viewModel.errorMessage,
        next = next
    ) {
        PasswordTextField(
            password = viewModel.password,
            onValueChange = { viewModel.updatePassword(it) },
            modifier = Modifier.fillMaxWidth(1f),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(5.dp))

        ValidPasswordRequirement(text = "Contains at least one uppercase and lowercase letter", valid = viewModel.containsCapLower())

        ValidPasswordRequirement(text = "Contains at least one special character (!, $, %, ...)", valid = viewModel.containsSpecialChar())

        ValidPasswordRequirement(text = "Contains at least one digit (0, 1, 2, ...)", valid = viewModel.containsDigit())

        ValidPasswordRequirement(text = "Minimum 8 characters long", valid = viewModel.minLength())

        Spacer(modifier = Modifier.height(10.dp))

        PasswordTextField(
            password = viewModel.passwordCheck,
            onValueChange = { viewModel.updatePasswordCheck(it) },
            modifier = Modifier.fillMaxWidth(1f),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            text = "Confirm Password"
        )

    }
}

@Composable
fun BasicInformationSignup(
    viewModel: LoginScreenViewModel
){
    InputSheet(
        title = "Sign Up",
        subTitle = "Tell Us About Yourself",
        setVisible = viewModel::onBackPressed,
        errorMessage = viewModel.errorMessage,
        next = {
            viewModel.updateSignupState(true)
        }
    ) {
        Text("What is your gender?")
        Spacer(modifier = Modifier.height(15.dp))
        ChooseGender(
            gender = viewModel.gender,
            customGender = viewModel.customGender,
            expanded = viewModel.genderExpanded,
            onExpandedChange = viewModel::updateGenderExpanded,
            updateGender = viewModel::updateGender,
            updateCustomGender = viewModel::updateCustomGender
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text("How experienced are you going to the gym?")
        Spacer(modifier = Modifier.height(15.dp))
        ChooseExperience(
            sliderPosition = viewModel.sliderPosition,
            updateSliderPosition = viewModel::updateSliderPosition
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Don't worry you can change these preferences anytime!", fontSize = 15.sp)
        if (viewModel.errorMessage != ""){
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = viewModel.errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun ChooseProfilePictureSignup(
    viewModel: LoginScreenViewModel
){
    InputSheet(
        title = "Sign Up",
        subTitle = "Express Yourself",
        setVisible = viewModel::onBackPressed ,
        errorMessage = viewModel.errorMessage,
        next = { viewModel.createAccountAttempt() },
        buttonText = "Sign up"
    ) {
            Text(text = "Set a profile picture (Optional)")
            Spacer(modifier = Modifier.height(40.dp))
            Row (
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                DisplayProfilePicture(model = viewModel.profilePicture)
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(40.dp))
            ChooseProfilePicture (viewModel::updateProfilePicture)
    }
}