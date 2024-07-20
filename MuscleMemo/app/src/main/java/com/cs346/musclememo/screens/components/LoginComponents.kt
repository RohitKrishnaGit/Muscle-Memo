package com.cs346.musclememo.screens.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContentTransitionScope
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PasswordTextField(
    password: String,
    onValueChange: (String) -> Unit,
    text: String = "Password",
    modifier: Modifier,
    keyboardOptions: KeyboardOptions
){
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(value = password, onValueChange = onValueChange, label = {
        Text(text = text)
    },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = keyboardOptions,
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else
                Icons.Filled.VisibilityOff

            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, description)
            }
        },
        modifier = modifier
    )
}

@Composable
fun InputSheet (
    title: String,
    setVisible: () -> Unit,
    content: @Composable() (() -> Unit)
) {
    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column (
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(icon = Icons.AutoMirrored.Filled.ArrowBack, text = title) {
                setVisible()
            }
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)) {
                content()
            }
        }
    }
}

@Composable
fun SignupSheet(
    title: String,
    setVisible: () -> Unit,
    next: () -> Unit,
    last: Boolean = false,
    content: @Composable() (() -> Unit)
) {
    InputSheet(title = "Sign up", setVisible = setVisible) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = title, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))
            content()

            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = next) {
                    Text(text = if (last) "Sign up" else "Next")
                }
            }
        }
    }
}

@Composable
fun ValidPasswordRequirement(
    text: String,
    valid: Boolean
){
    val color = if (valid) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.error
    Spacer(modifier = Modifier.height(10.dp))
    Row (
        modifier = Modifier.fillMaxWidth(),
    ) {
        Icon(if (valid) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank, "Box", tint = color)
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = text, color = color, fontSize = 13.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseGender(
    gender: String,
    customGender: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    updateGender: (String) -> Unit,
    updateCustomGender: (String) -> Unit
){
    val genders: List<String> = listOf(
        "Male",
        "Female",
        "Rather Not Say",
        "Custom"
    )

    Column (
        modifier = Modifier.fillMaxWidth()
    ){
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = onExpandedChange
        ) {
            TextField(
                value = gender,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) },
                modifier = Modifier.exposedDropdownSize()
            ) {
                genders.forEach { gender ->
                    DropdownMenuItem(
                        text = { Text(text = gender) },
                        onClick = {
                            updateGender(gender)
                            onExpandedChange(false)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        if (gender == "Custom") {
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = customGender,
                onValueChange = {
                    updateCustomGender(it)
                },
                label = {
                    Text(text = "What's your gender?")
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth(1f)
            )
        }
    }
}

@Composable
fun ChooseExperience(
    sliderPosition: Float,
    updateSliderPosition: (Float) -> Unit
){
    Column (
        modifier = Modifier.fillMaxWidth()
    ){
        Row {
            Slider(
                value = sliderPosition,
                onValueChange = updateSliderPosition,
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
    }
}

@Composable
fun ChooseProfilePicture(
    updateProfilePicture: (Uri?) -> Unit
){
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()) {
        updateProfilePicture(it)
    }
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

// Source: compose-examples/jetsurvey
fun getTransitionDirection(
    initialIndex: Int,
    targetIndex: Int
): AnimatedContentTransitionScope.SlideDirection {
    return if (targetIndex > initialIndex) {
        // Going forwards in the survey: Set the initial offset to start
        // at the size of the content so it slides in from right to left, and
        // slides out from the left of the screen to -fullWidth
        AnimatedContentTransitionScope.SlideDirection.Left
    } else {
        // Going back to the previous question in the set, we do the same
        // transition as above, but with different offsets - the inverse of
        // above, negative fullWidth to enter, and fullWidth to exit.
        AnimatedContentTransitionScope.SlideDirection.Right
    }
}