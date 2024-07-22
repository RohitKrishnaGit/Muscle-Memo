package com.cs346.musclememo.api.services

import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.ExerciseRef
import retrofit2.Call
import retrofit2.http.GET

interface TemplateService {
    @GET("/templates/me")
    fun getTemplates(): Call<ApiResponse<List<ExerciseRef>>>

}