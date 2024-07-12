package com.cs346.musclememo.api.types

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import retrofit2.Response

data class ApiResponse<T>(val error: Boolean, val data: T?, val message: String?)

inline fun Response<*>.parseErrorBody(): ApiResponse<*>?
{
    return errorBody()?.let {
        var mJson: JsonElement;
        try {
            mJson = JsonParser.parseString(it.string())
            val gson = Gson()
            val errorResponse: ApiResponse<*> = gson.fromJson(mJson, ApiResponse::class.java);
            return errorResponse
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}