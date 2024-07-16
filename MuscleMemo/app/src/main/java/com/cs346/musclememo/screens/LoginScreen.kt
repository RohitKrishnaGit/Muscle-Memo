package com.cs346.musclememo.screens

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs346.musclememo.R
import com.cs346.musclememo.screens.components.DisplayProfilePicture
import com.cs346.musclememo.screens.components.InputSheet
import com.cs346.musclememo.screens.components.PasswordTextField
import com.cs346.musclememo.screens.components.SignupSheet
import com.cs346.musclememo.screens.components.ValidPasswordRequirement
import com.cs346.musclememo.screens.viewmodels.LoginScreenViewModel
import com.cs346.musclememo.screens.components.getTransitionDirection


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
){
    AnimatedVisibility(
        visible = viewModel.loginVisible,
        enter = slideInHorizontally(initialOffsetX = { it }),
        exit = slideOutHorizontally(targetOffsetX = { it })
    ) {
        InputSheet(
            title = "Log in",
            setVisible = viewModel::onBackPressed
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Text(text = "Welcome Back", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                // Email
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = viewModel.email,
                    onValueChange = {
                        viewModel.updateEmail(it)
                    },
                    label = {
                        Text(text = "Email")
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
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

                })
                // Login Button
                if (viewModel.errorMessage != "") {
                    Spacer(modifier = Modifier.height(4.dp))
                    //Invalid Login
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(viewModel.errorMessage)
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = {
                        viewModel.loginAttempt(onSuccess = onSuccessLogin)
                    }) {
                        Text(text = "Log in", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
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
                LoginScreenViewModel.LoginState.USERNAME_EMAIL -> {
                    UsernameEmailSignup(viewModel)
                }

                LoginScreenViewModel.LoginState.PASSWORD -> {
                    PasswordSignup(viewModel)
                }

                LoginScreenViewModel.LoginState.BASIC_INFO -> {
                    BasicInformationSignup(viewModel)
                }
                
                LoginScreenViewModel.LoginState.PFP -> {
                    ChoosePFPSignup(viewModel)
                }
            }
        }
    }
}

@Composable
fun UsernameEmailSignup(
    viewModel: LoginScreenViewModel
) {
    SignupSheet(
        title = "Create an Account",
        setVisible = viewModel::onBackPressed,
        next = {
        viewModel.updateLoginState(
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
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth(1f)
        )
        if (viewModel.errorMessage != ""){
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = viewModel.errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }

}

@Composable
fun PasswordSignup(
    viewModel: LoginScreenViewModel
) {
    SignupSheet(
        title = "Choose a Password",
        setVisible = viewModel::onBackPressed,
        next = {
            viewModel.updateLoginState(
                true
            )
        }) {
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

        Spacer(modifier = Modifier.height(15.dp))

        PasswordTextField(
            password = viewModel.passwordCheck,
            onValueChange = { viewModel.updatePasswordCheck(it) },
            modifier = Modifier.fillMaxWidth(1f),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        if (viewModel.errorMessage != ""){
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = viewModel.errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicInformationSignup(
    viewModel: LoginScreenViewModel
){
    SignupSheet(
        title = "Tell Us About Yourself",
        setVisible = viewModel::onBackPressed,
        next = {
            viewModel.updateLoginState(true)
        }
    ) {
        Text("What is your gender?")
        Spacer(modifier = Modifier.height(15.dp))
        ExposedDropdownMenuBox(
            expanded = viewModel.genderExpanded,
            onExpandedChange = {viewModel.updateGenderExpanded(it)}
        ) {
            TextField(
                value = viewModel.gender,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = viewModel.genderExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            DropdownMenu(
                expanded = viewModel.genderExpanded,
                onDismissRequest = { viewModel.updateGenderExpanded(false) },
                modifier = Modifier.exposedDropdownSize()
            ) {
                viewModel.genders.forEach{ gender ->
                    DropdownMenuItem(
                        text = { Text(text = gender) },
                        onClick = {
                            viewModel.updateGender(gender)
                            viewModel.updateGenderExpanded(false)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        if (viewModel.gender == "Custom"){
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = viewModel.customGender,
                onValueChange = {
                    viewModel.updateCustomGender(it)
                },
                label = {
                    Text(text = "What's your gender?")
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth(1f)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text("How experienced are you going to the gym?")
        Spacer(modifier = Modifier.height(15.dp))
        Row {
            Slider(
                value = viewModel.sliderPosition,
                onValueChange = {
                    viewModel.updateSliderPosition(it)
                },
                valueRange = 0f..1f,
                steps = 1,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            )
        }
        Row {
            Text(
                text = "Novice",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.8f)
            )
            Text(
                text = "Intermediate",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.8f)
            )
            Text(
                text = "Professional",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.8f)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Don't worry you can change these preferences anytime!", fontSize = 15.sp)
        if (viewModel.errorMessage != ""){
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = viewModel.errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun ChoosePFPSignup(
    viewModel: LoginScreenViewModel
){
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()) {
            viewModel.updateProfilePicture(it)
    }

    SignupSheet(
        title = "Express Yourself (Optional)",
        setVisible = viewModel::onBackPressed ,
        next = { viewModel.createAccountAttempt() },
        last = true
    ) {
            Text(text = "Set a profile picture")
            Spacer(modifier = Modifier.height(40.dp))
            Row (
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                DisplayProfilePicture(model = viewModel.profilePicture)
                Spacer(modifier = Modifier.weight(1f))
            }
        Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = { photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                ) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Outlined.AddAPhoto, "Add a photo")
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Add a picture")
                }
            }
    }
}