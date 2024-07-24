package com.cs346.musclememo.utils

import androidx.compose.animation.AnimatedContentTransitionScope
import com.cs346.musclememo.classes.ExerciseRef
import com.cs346.musclememo.classes.ExerciseSet
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.round

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

fun getWeight(weight: Int?): Int?{
    return if (AppPreferences.systemOfMeasurementWeight == "lbs" && weight != null)
        (weight.times(2.205)).toInt()
    else
        weight
}

fun getDistance(distance: Int?): Int? {
    return if (AppPreferences.systemOfMeasurementWeight == "miles" && distance != null)
        (distance.div(1.609)).toInt()
    else
        distance
}

fun translateWeight(weight: Int?): Int?{
    return if (AppPreferences.systemOfMeasurementWeight == "lbs" && weight != null)
        (weight.div(2.205)).toInt()
    else
        weight
}

fun translateDistance(distance: Int?): Int?{
    return if (AppPreferences.systemOfMeasurementWeight == "miles" && distance != null)
        (distance.times(1.609)).toInt()
    else
        distance
}

fun toHourMinuteSeconds(time: Int): String{
    return String.format(Locale.getDefault(),"%02d:%02d:%02d", time / 3600, (time % 3600) / 60, time % 60)
}

fun epochToDate(
    date: Long,
    time: Boolean = false
): String {
    val instant = Instant.ofEpochMilli(date)
    val zoneId = ZoneId.systemDefault() // Use the system default time zone
    val localDateTime = instant.atZone(zoneId).toLocalDateTime()
    val formatter = if (time) DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' hh:mm a") else DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return localDateTime.format(formatter)
}

fun epochToMonthYear(
    date: Long
): String {
    val instant = Instant.ofEpochMilli(date)
    val zoneId = ZoneId.systemDefault() // Use the system default time zone
    val localDateTime = instant.atZone(zoneId).toLocalDateTime()
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
    return localDateTime.format(formatter)
}

fun displayTime(
    seconds: Int
): String {
    return if (seconds >= 3600)
        String.format(Locale.getDefault(),"%d:%02d h", seconds % 3600, (seconds % 3600)/60)
    else if (seconds >= 60)
        String.format(Locale.getDefault(),"%d:%02d m", seconds / 60, seconds % 60)
    else
        "$seconds s"
}

fun calculateScore(set: ExerciseSet): Int{
    val weight = translateWeight(set.weight)
    val distance = translateDistance(set.distance)
    val duration = set.duration
    val reps = set.reps

    if (set.duration != null){
        return if (weight != null && distance != null)
            weight * distance * distance / duration!!
        else if (weight != null)
            duration!! * weight
        else if (distance != null)
            distance * distance / duration!!
        else
            duration!!
    } else {
        if (set.reps != null){
            return if (weight != null && distance != null)
                reps!! * weight * distance
            else if (weight != null)
                round( weight / (1.0278 - 0.0278 * reps!!) ).toInt()
            else if (distance != null)
                reps!! * distance
            else
                reps!!
        }
    }
    return 0
}

fun displayScore(exerciseRef: ExerciseRef?, score: Int): String{
    if (exerciseRef == null)
        return score.toString()
    return if (exerciseRef.durationVSReps && !exerciseRef.weight && !exerciseRef.distance)
        displayTime(score)
    else
        score.toString()
}

fun convertPrNameToRefName(prName: String): String{
    var refName = prName.replace("__", " (")
    if (refName != prName)
        refName += ")"
    return refName.replace("_", " ")
}

fun convertRefNameToPrName(refName: String): String{
    return refName.replace("(", "_").replace(")", "").replace(" ", "_")
}