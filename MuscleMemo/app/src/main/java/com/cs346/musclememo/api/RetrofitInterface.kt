package com.cs346.musclememo.api

interface RetrofitInterface {
    val baseUrl: String
        get() = "http://10.0.2.2:3000/"
}