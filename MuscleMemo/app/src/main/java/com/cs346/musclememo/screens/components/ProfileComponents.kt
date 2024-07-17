package com.cs346.musclememo.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.cs346.musclememo.classes.User


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
    me: Boolean
){

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!me)
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        DisplayProfilePicture(model = user.profilePicture, size = 150.dp)
        Spacer(modifier = Modifier.height(20.dp))
        Text(user.username, fontSize = 25.sp)
    }
}

@Composable
fun ConfirmationDialog(

){

}



@Composable
fun SignoutButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit
){
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer),
        modifier = Modifier.fillMaxWidth(0.8f)
    ) {
        content()
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
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title)
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