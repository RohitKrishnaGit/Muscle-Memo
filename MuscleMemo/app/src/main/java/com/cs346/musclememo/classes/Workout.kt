package com.cs346.musclememo.classes

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap

// a workout consists a list of exercises.
// each element is a map where the key is the exerciseId and the value is a list of sets of
data class Workout(
    var workoutName: MutableState<String> = mutableStateOf("New Workout"),
    var exercises: MutableList<SnapshotStateMap<Int, SnapshotStateList<Exercise>>> =
        mutableStateListOf(
            mutableStateMapOf(
            )
        )
) {
    fun setWorkout(workout: Workout = Workout()) {
        workoutName.value = workout.workoutName.value
        exercises = workout.exercises
    }

    fun addNewExercise(exerciseId: Int) {
        exercises.add(
            mutableStateMapOf(
                exerciseId to mutableStateListOf<Exercise>(
                    Exercise(mutableStateOf(null), mutableStateOf(null))
                )
            )
        )
    }

    fun removeExercise(exerciseIndex: Int) {
        exercises.removeAt(exerciseIndex)
    }

    fun addNewSet(exerciseIndex: Int) {
        val exercise = exercises[exerciseIndex].values.first()
        exercise.add(Exercise(mutableStateOf(null), mutableStateOf(null)))
    }

    fun removeSet(exerciseIndex: Int, setIndex: Int) {
        val exercise = exercises[exerciseIndex].values.first()
        exercise.removeAt(setIndex)
    }

    fun setWorkoutName(name: String) {
        workoutName.value = name
    }

    fun getWorkoutName(): String {
        return workoutName.value
    }
}