package com.cs346.musclememo.api.types

data class ApiResponse<T>(val error: Boolean, val data: T?, val message: String?)