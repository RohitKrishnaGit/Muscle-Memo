package com.cs346.musclememo.api.services

import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.ExerciseIteration
import com.cs346.musclememo.classes.Template
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class CreateTemplateRequest (val name: String, val userId: String = "me")
data class GetTemplateResponse (val id: Int,val name: String, val exercises: List<ExerciseIteration>)
data class CreateTemplateResponse (val templateId: Int)


interface TemplateService {
    @GET("/templates/me")
    fun getTemplates(): Call<ApiResponse<List<GetTemplateResponse>>>

    @POST("/templates")
    fun createTemplate(@Body createTemplateRequest: CreateTemplateRequest): Call<ApiResponse<CreateTemplateResponse>>

    @PUT("/templates/update/me/{id}")
    fun updateTemplate(@Path("id") id: Int, @Body name: String): Call<ApiResponse<Template>>

    @DELETE("/templates/me/{id}")
    fun deleteTemplate(@Path("id") id: String): Call<ApiResponse<String>>
}