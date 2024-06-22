package com.cs346.musclememo.genericcomponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.cs346.musclememo.classes.Colors

@Composable
fun MMButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    shape: Shape = RoundedCornerShape(4.dp),
    backgroundColor: Color = Colors.PRIMARY.color,
    maxWidth: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = if (maxWidth) modifier.fillMaxWidth() else modifier,
        shape = shape,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor)
    ) {
        Text(text)
    }
}