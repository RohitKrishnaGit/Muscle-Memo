package com.cs346.musclememo.classes

import android.graphics.Color
import androidx.compose.ui.graphics.Color as ComposeColor

enum class Colors(val color: ComposeColor) {
    PRIMARY(ComposeColor(Color.rgb(104,84,164))),
    SECONDARY(ComposeColor(Color.rgb(72, 59, 110))),
    SUCCESS(ComposeColor(Color.rgb(40, 167, 69))),
    DANGER(ComposeColor(Color.rgb(220, 53, 69))),
    LIGHTGRAY(ComposeColor(Color.rgb(245, 245, 245))),
}