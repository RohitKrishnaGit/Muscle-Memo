package com.cs346.musclememo.genericcomponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MMButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonText: String,
    buttonShape: Shape = RoundedCornerShape(4.dp),
    buttonSize: Dp = 40.dp,
    backgroundColor: Color = Colors.PRIMARY.color
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .size(buttonSize),
        shape = buttonShape,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor)
    ) {
        Text(buttonText)
    }
}