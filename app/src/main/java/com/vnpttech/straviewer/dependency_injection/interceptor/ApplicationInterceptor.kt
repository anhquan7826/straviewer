package com.vnpttech.straviewer.dependency_injection.interceptor

import com.vnpttech.straviewer.dependency_injection.app_preferences.AppPreferences
import okhttp3.Interceptor
import okhttp3.Response

class ApplicationInterceptor(private val appPreferences: AppPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = appPreferences.getString("access_token")
        var request = chain.request()
        if (token != null) {
            request = request.newBuilder().addHeader("Authorization", "Bearer $token").build()
        }
        return chain.proceed(request)
    }
}