package com.cs346.musclememo.screens.components

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.cs346.musclememo.classes.ExerciseRef
import com.cs346.musclememo.classes.ExerciseSet
import com.cs346.musclememo.utils.AppPreferences
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.round


@Composable
fun TopAppBar(
    icon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    text: String,
    onBackPressed: () -> Unit
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.1f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        IconButton(onClick = onBackPressed) {
            Icon(icon, "")
        }
        Text(text = text)
        Spacer(modifier = Modifier.size(48.dp))
    }
}

@Composable
fun MMDialog(
    showDialog: Boolean,
    title: String,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    body: @Composable () -> Unit,
    confirmButtonText: String = "Confirm",
    cancelButtonText: String = "Cancel",
    errorText: String? = null
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(title) },
            text = {
                Column {
                    body()
                    errorText?.takeIf { it.isNotEmpty() }?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onConfirm()
                }) {
                    Text(confirmButtonText)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(cancelButtonText)
                }
            }
        )
    }
}

@Composable
fun MMButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    shape: Shape = RoundedCornerShape(4.dp),
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    textColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    maxWidth: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = if (maxWidth) modifier.fillMaxWidth() else modifier,
        shape = shape,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor)
    ) {
        Text(text, color = textColor)
    }
}