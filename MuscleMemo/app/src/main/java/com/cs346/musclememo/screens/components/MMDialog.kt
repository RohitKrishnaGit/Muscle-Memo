package com.cs346.musclememo.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun MMDialog(
    showDialog: Boolean,
    title: String,
    text: String = "",
    initialValue: String,
    onConfirm: (String) -> Unit,
    onDismissRequest: () -> Unit,
    hasInputField: Boolean = true,
    hasText: Boolean = true
) {
    if (showDialog) {
        var inputValue by remember { mutableStateOf(initialValue) }

        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(title) },
            text = {
                Column {
                    if (hasText) Text(text)
                    if (hasInputField) TextField(
                        value = inputValue,
                        onValueChange = { inputValue = it },
                        label = { Text("Input") }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onConfirm(if (hasInputField) inputValue else "")
                    onDismissRequest()
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text("Cancel")
                }
            }
        )
    }
}