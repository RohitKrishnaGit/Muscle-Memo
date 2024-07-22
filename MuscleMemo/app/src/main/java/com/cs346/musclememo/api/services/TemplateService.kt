package com.cs346.musclememo.api.services

import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.Template
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class CreateTemplateClass (val name: String, val userId: String = "me")


interface TemplateService {
    @GET("/templates/me")
    fun getTemplates(): Call<ApiResponse<List<Template>>>

    @POST("/templates")
    fun createTemplate(@Body template: CreateTemplateClass): Call<ApiResponse<Template>>

}