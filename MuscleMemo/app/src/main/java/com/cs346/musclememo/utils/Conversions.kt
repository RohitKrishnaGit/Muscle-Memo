package com.cs346.musclememo.utils

object Conversions {
    @JvmStatic
    fun sliderToExperience(value: Float): String = when (value) {
        1.0f -> "Professional"
        0.5f -> "Intermediate"
        else -> "Novice"
    }

    @JvmStatic
    fun experienceToSlider(value: String): Float = when (value) {
        "Professional" -> 1.0f
        "Intermediate" -> 0.5f
        else -> 0.0f
    }
}