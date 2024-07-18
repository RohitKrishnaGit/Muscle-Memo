package com.cs346.musclememo.screens.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cs346.musclememo.classes.User
import kotlin.math.exp


@Composable
fun DisplayProfilePicture(
    model: Any?,
    size: Dp = 240.dp
){
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        if (model != null) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                model = model,
                contentDescription = null
            )
        } else {
            Icon(Icons.Default.Person, "Default Profile Picture", modifier = Modifier.size(size*0.8f), tint = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}

@Composable
fun DisplayProfile (
    user: User,
    me: Boolean,

    editProfilePicture: @Composable () -> Unit = {},
    editUsername: @Composable () -> Unit = {},
    editExperience: @Composable () -> Unit = {},
    editGender: @Composable () -> Unit = {}
){
    val listState = rememberLazyListState()
    LazyColumn (
        state = listState,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item{
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!me)
                    Spacer(modifier = Modifier.fillMaxHeight(0.1f))

                DisplayProfilePicture(model = user.profilePicture, size = 150.dp)
                editProfilePicture()
                Text(user.username, fontSize = 25.sp)
                editUsername()
                if (user.gender != "Rather Not Say")
                    Text(text = user.gender)
                editGender()
                DisplayExperience(experience = user.experience, size = 250.dp)
                editExperience()
                Column (
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = "Personal Bests", fontSize = 30.sp)
                }
            }
        }
    }
}

@Composable
fun ConfirmationDialog(

){
    //TODO: confirmation for exiting without saving changes, signing out of all devices
}

@Composable
fun EditSurface(
    edit: Boolean?,
    spacerSize: Dp = 10.dp,
    showBackground: Boolean = true,
    content: @Composable () -> Unit
){
    AnimatedVisibility(
        visible = edit ?: false,
//        enter = slideInVertically(initialOffsetY = {it}),
//        exit = slideOutVertically(targetOffsetY = {it})
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (showBackground) MaterialTheme.colorScheme.surfaceContainerLowest else MaterialTheme.colorScheme.background)
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    content()
                }
            }
        }
    }
    if (edit != null && !edit)
        Spacer(modifier = Modifier.height(spacerSize))
}

@Composable
fun DisplayExperience(
    experience: String,
    size: Dp
){
    val containerColor = if (experience == "Professional") MaterialTheme.colorScheme.tertiaryContainer else if (experience == "Intermediate") MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer
    val textColor = if (experience == "Professional") MaterialTheme.colorScheme.onTertiaryContainer else if (experience == "Intermediate") MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimaryContainer
    Box(
        modifier = Modifier
            .width(size)
            .height(size / 6)
            .clip(RoundedCornerShape(16.dp))
            .background(containerColor)
    ){
        Column (
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = experience, color = textColor)
            }
        }
    }
}

@Composable
fun SignoutButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit
){
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ){
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSetting(
    title: String,
    text: String,
    setText: (String) -> Unit,
    expanded: Boolean,
    options: List<String>,
    onExpandedChange: (Boolean) -> Unit
){
    Box(modifier = Modifier.fillMaxWidth()) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, fontSize = 20.sp)
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = onExpandedChange,
                modifier = Modifier.width(90.dp)
            ) {
                val interactionSource = remember { MutableInteractionSource() }

                BasicTextField(
                    value = text,
                    onValueChange = {},
                    interactionSource = interactionSource,
                    readOnly = true,
                    enabled = false,
                    singleLine = true,
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier
                        .menuAnchor()
                ) {
                    TextFieldDefaults.DecorationBox(
                        value = text,
                        innerTextField = it,
                        enabled = false,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                            start = 8.dp,
                            end = 8.dp,
                        ),
                        container = {
                            TextFieldDefaults.ContainerBox(true, false, interactionSource, TextFieldDefaults.colors())
                        },
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { onExpandedChange(false) },
                    modifier = Modifier.exposedDropdownSize()
                ) {
                    options.forEach{option ->
                        DropdownMenuItem(
                            text = { Text(text = option) },
                            onClick = {
                                setText(option)
                                onExpandedChange(false)
                            }
                        )
                    }
                }
            }
        }
    }
}