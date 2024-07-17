package com.cs346.musclememo.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs346.musclememo.screens.components.ChooseExperience
import com.cs346.musclememo.screens.components.ChooseGender
import com.cs346.musclememo.screens.components.ChooseProfilePicture
import com.cs346.musclememo.screens.components.DisplayProfile
import com.cs346.musclememo.screens.components.DropdownSetting
import com.cs346.musclememo.screens.components.EditSurface
import com.cs346.musclememo.screens.components.SignoutButton
import com.cs346.musclememo.screens.viewmodels.ProfileScreenViewModel
import com.cs346.musclememo.utils.AppPreferences

@Composable
fun ProfileScreen(
    signOut: () -> Unit
) {
    val viewModel = viewModel<ProfileScreenViewModel>()

    DisplayMe(
        viewModel = viewModel,
        signOut = signOut
    )
}

@Composable
fun DisplayMe(
    viewModel: ProfileScreenViewModel,
    signOut: () -> Unit
){
    SettingsDialog(
        show = viewModel.showSettings,
        signOut = signOut,
        viewModel = viewModel
    )

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserSettings(viewModel = viewModel)
        DisplayProfile(
            user = viewModel.user,
            me = true,
            edit = viewModel.editEnabled,
            editProfilePicture = {
                EditSurface {
                    ChooseProfilePicture (
                        viewModel::updateNewProfilePicture
                    )
                }
            },
            editUsername = {
                EditSurface {
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
                EditSurface {
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
                EditSurface {
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
fun SettingsDialog(
    show: Boolean,
    signOut: () -> Unit,
    viewModel: ProfileScreenViewModel
){
    if (show){
        Dialog(onDismissRequest = {viewModel.updateShowSettings(false)}) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .padding(24.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ){
                    Text(text = "Settings", fontSize = 30.sp)
                    DropdownSetting(
                        title = "Unit of Distance",
                        text = AppPreferences.systemOfMeasurementDistance ?: "km",
                        setText = viewModel::updateSystemDistance,
                        expanded = viewModel.showDistanceOptions,
                        options = viewModel.listOfMeasurementDistance,
                        onExpandedChange = viewModel::updateShowDistanceOptions
                    )

                    DropdownSetting(
                        title = "Unit of Weight",
                        text = AppPreferences.systemOfMeasurementWeight ?: "kg",
                        setText = viewModel::updateSystemWeight,
                        expanded = viewModel.showWeightOptions,
                        options = viewModel.listOfMeasurementWeight,
                        onExpandedChange = viewModel::updateShowWeightOptions
                    )

                    DropdownSetting(
                        title = "App Theme",
                        text = viewModel.getTheme(),
                        setText = viewModel::updateTheme,
                        expanded = viewModel.showThemeOptions,
                        options = viewModel.listOfTheme,
                        onExpandedChange = viewModel::updateShowThemeOptions
                    )

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
                    SignoutButton(
                        onClick = {
                            // TODO: Add sign out call
                            viewModel.updateShowSettings(false)
                            signOut()
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
        IconButton(onClick = { viewModel.updateEditEnabled(!viewModel.editEnabled) }) {
            Icon(imageVector = if (!viewModel.editEnabled) Icons.Default.Edit else Icons.Default.Close, "Edit Profile", modifier = Modifier.size(40.dp))
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = {
            viewModel.updateShowSettings(true)
        }) {
            Icon(imageVector = Icons.Default.Settings, "Settings", modifier = Modifier.size(40.dp))
        }
    }
}