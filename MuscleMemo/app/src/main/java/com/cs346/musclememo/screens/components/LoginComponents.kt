package com.cs346.musclememo.screens.components

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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                IconButton(
                    onClick = { setVisible() }
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back to previous screen")
                }
                Text(text = title)
                Spacer(modifier = Modifier.size(48.dp))
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