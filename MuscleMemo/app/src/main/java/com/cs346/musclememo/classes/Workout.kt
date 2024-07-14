package com.cs346.musclememo.classes

import androidx.compose.runtime.mutableStateListOf

// a workout consists a list of exercises.
// each element is a map where the key is the exerciseId and the value is a list of sets of
data class Workout(
    var id: Int = 0,
    var name: String = "New Workout",
    var exercises: MutableList<ExerciseIteration> = mutableStateListOf<ExerciseIteration>(),
    var userId: Int = 1,
) {
    fun setWorkout(workout: Workout = Workout()) {
        id = workout.id
        name = workout.name
        exercises = workout.exercises
        userId = workout.userId
    }

    fun addNewExercise(exerciseRef: ExerciseRef) {
        exercises.add(ExerciseIteration(exerciseRef))
    }

    fun removeExercise(exerciseIndex: Int) {
        exercises.removeAt(exerciseIndex)
    }

    fun addSet(exerciseIndex: Int) {
        val sets = exercises[exerciseIndex].exerciseSet
        sets.add(ExerciseSet())
    }

    fun removeSet(exerciseIndex: Int, setIndex: Int) {
        val sets = exercises[exerciseIndex].exerciseSet
        sets.removeAt(setIndex)
    }

    fun setWorkoutName(newName: String) {
        name = newName
    }
}