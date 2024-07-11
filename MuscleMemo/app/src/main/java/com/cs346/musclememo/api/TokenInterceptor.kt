package com.cs346.musclememo.api
import com.cs346.musclememo.utils.AppPreferences
import okhttp3.Interceptor
import okhttp3.Response
class TokenInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder().apply {
            AppPreferences.accessToken?.let { header("x-access-token", it) }
            method(original.method(), original.body())
        }.build()

        return chain.proceed(request)
    }
}