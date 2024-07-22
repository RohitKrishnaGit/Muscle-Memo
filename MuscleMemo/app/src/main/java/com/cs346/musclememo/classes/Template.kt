package com.cs346.musclememo.classes

import androidx.compose.runtime.mutableStateListOf

data class Template(
    var id: Int = 0,
    var name: String = "",
    var exercises: MutableList<ExerciseIteration> = mutableStateListOf<ExerciseIteration>(),
) {
    fun setTemplate(template: Template = Template()) {
        id = template.id
        name = template.name
        exercises = template.exercises
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

    fun setTemplateName(newName: String) {
        name = newName
    }
}