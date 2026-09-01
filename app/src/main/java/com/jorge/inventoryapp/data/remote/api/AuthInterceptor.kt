package com.jorge.inventoryapp.data.remote.api

import com.jorge.inventoryapp.data.local.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(
        chain: Interceptor.Chain
    ): Response {

        val originalRequest =
            chain.request()

        val token =
            SessionManager.getToken()

        val requestBuilder =
            originalRequest.newBuilder()

        if (!token.isNullOrBlank()) {

            requestBuilder.addHeader(
                "Authorization",
                "Bearer $token"
            )
        }

        val newRequest =
            requestBuilder.build()

        return chain.proceed(newRequest)
    }
}