package com.cs346.musclememo.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cs346.musclememo.classes.Exercise
import com.cs346.musclememo.classes.ExerciseIteration
import com.cs346.musclememo.classes.ExerciseSet
import com.cs346.musclememo.classes.User
import com.cs346.musclememo.classes.Workout

class HistoryScreenViewModel : ViewModel() {

    var user by mutableStateOf<User?>(null)

    init {
        // todo: grab user history
        user = User(
            "starfy84",
            "Dereck Tu",
            "starfy84@gmail.com",
            mutableListOf<Workout>(
                Workout(
                    "Test Workout",
                    mutableListOf<ExerciseIteration>(
                        ExerciseIteration(
                            Exercise("Deadlift", 1),
                            mutableListOf<ExerciseSet>(ExerciseSet(10, 10))
                        )
                    )
                )
            )
        )
    }
}