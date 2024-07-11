package com.cs346.musclememo.api.services

import com.cs346.musclememo.classes.Exercise
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

public interface ExerciseRefService {
    @GET("/exerciseRefs")
    fun getExerciseRef(): Call<List<Exercise>>
}