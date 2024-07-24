package com.cs346.musclememo.classes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class Template(
    var id: Int = 0,
    val initialName: String = "New Template",
        var exercises: MutableList<ExerciseIteration> = mutableStateListOf<ExerciseIteration>(),
) {
    var name by mutableStateOf(initialName)

    fun setTemplate(template: Template = Template()) {
        id = template.id
        name = template.name
        exercises = template.exercises
    }

    fun addExercise(exerciseRef: ExerciseRef) {
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
}