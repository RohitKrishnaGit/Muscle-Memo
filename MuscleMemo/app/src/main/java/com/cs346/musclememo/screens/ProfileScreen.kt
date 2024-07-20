package com.cs346.musclememo.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs346.musclememo.screens.components.ChooseExperience
import com.cs346.musclememo.screens.components.ChooseGender
import com.cs346.musclememo.screens.components.ChooseProfilePicture
import com.cs346.musclememo.screens.components.DisplayProfile
import com.cs346.musclememo.screens.components.DropdownSetting
import com.cs346.musclememo.screens.components.EditSurface
import com.cs346.musclememo.screens.components.SignoutButton
import com.cs346.musclememo.screens.components.TopAppBar
import com.cs346.musclememo.screens.viewmodels.ProfileScreenViewModel
import com.cs346.musclememo.utils.AppPreferences

@Composable
fun ProfileScreen(
    signOut: () -> Unit
) {
    val viewModel = viewModel<ProfileScreenViewModel>()

    BackHandler (viewModel.showSettings) {
        viewModel.updateShowSettings(false)
    }

    DisplayMe(
        viewModel = viewModel
    )

    SettingsScreen(
        show = viewModel.showSettings,
        signOut = signOut,
        viewModel = viewModel
    )
}

@Composable
fun DisplayMe(
    viewModel: ProfileScreenViewModel
){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserSettings(viewModel = viewModel)
        DisplayProfile(
            user = if (viewModel.editEnabled) viewModel.getNewUser() else viewModel.user,
            me = true,
            editProfilePicture = {
                EditSurface(edit = viewModel.editEnabled, spacerSize = 20.dp, showBackground = false){
                    ChooseProfilePicture(
                        viewModel::updateNewProfilePicture
                    )
                }
            },
            editUsername = {
                EditSurface (
                    edit = viewModel.editEnabled
                ) {
                    OutlinedTextField(
                        value = viewModel.newUsername,
                        onValueChange = viewModel::updateNewUsername,
                        label = {
                            Text(text = "Username")
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        modifier = Modifier.fillMaxWidth(1f)
                    )
                }
            },
            editGender = {
                EditSurface (
                    edit = viewModel.editEnabled
                ) {
                    ChooseGender(
                        gender = viewModel.newGender,
                        customGender = viewModel.newCustomGender,
                        expanded = viewModel.genderExpanded,
                        onExpandedChange = viewModel::updateGenderExpanded,
                        updateGender = viewModel::updateNewGender,
                        updateCustomGender = viewModel::updateNewCustomGender
                    )
                }
            },
            editExperience = {
                EditSurface (
                    edit = viewModel.editEnabled,
                    spacerSize = 30.dp
                ){
                    ChooseExperience(
                        sliderPosition = viewModel.newExperience,
                        updateSliderPosition = viewModel::updateNewExperience
                        )
                }
            }
        )
    }
}

@Composable
fun SettingsScreen(
    show: Boolean,
    signOut: () -> Unit,
    viewModel: ProfileScreenViewModel
){
        AnimatedVisibility(
            visible = show,
            enter = slideInHorizontally(initialOffsetX = { it }),
            exit = slideOutHorizontally(targetOffsetX = { it })
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ){
                    TopAppBar(icon = Icons.AutoMirrored.Filled.ArrowBack, text = "Settings") {
                        viewModel.updateShowSettings(false)
                    }
                    Column (
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    ){
                        DropdownSetting(
                            title = "Unit of Distance",
                            text = AppPreferences.systemOfMeasurementDistance ?: "km",
                            setText = viewModel::updateSystemDistance,
                            expanded = viewModel.showDistanceOptions,
                            options = viewModel.listOfMeasurementDistance,
                            onExpandedChange = viewModel::updateShowDistanceOptions
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        DropdownSetting(
                            title = "Unit of Weight",
                            text = AppPreferences.systemOfMeasurementWeight ?: "kg",
                            setText = viewModel::updateSystemWeight,
                            expanded = viewModel.showWeightOptions,
                            options = viewModel.listOfMeasurementWeight,
                            onExpandedChange = viewModel::updateShowWeightOptions
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        DropdownSetting(
                            title = "App Theme",
                            text = viewModel.getTheme(),
                            setText = viewModel::updateTheme,
                            expanded = viewModel.showThemeOptions,
                            options = viewModel.listOfTheme,
                            onExpandedChange = viewModel::updateShowThemeOptions
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        SignoutButton(
                            onClick = {
                                viewModel.updateShowSettings(false)
                                signOut()
                            },
                        ) {
                            Text(
                                text = "Sign Out",
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        SignoutButton(
                            onClick = {
                                viewModel.updateShowSettings(false)
                                signOut()
                                viewModel.signoutAllDevices()
                            }
                        ) {
                            Text(
                                text = "Sign Out From All Devices",
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }
        }
    }



@Composable
fun UserSettings(
    viewModel: ProfileScreenViewModel
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.1f)
    ) {
        IconButton(onClick = {
            if (!viewModel.editEnabled)
                viewModel.refreshNewUser()
            if (viewModel.editEnabled)
                viewModel.updateUser()
            viewModel.updateEditEnabled(!viewModel.editEnabled)
        }) {
            Icon(imageVector = if (!viewModel.editEnabled) Icons.Default.Edit else Icons.Default.Done, "Edit Profile")
        }
        if (viewModel.editEnabled){
            Spacer(modifier = Modifier.width(5.dp))
            IconButton(onClick = { 
                viewModel.updateEditEnabled(false)
            }){
                Icon(Icons.Default.Close, "Discard Changes")
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = {
            viewModel.updateShowSettings(true)
        }) {
            Icon(imageVector = Icons.Default.Settings, "Settings")
        }
    }
}