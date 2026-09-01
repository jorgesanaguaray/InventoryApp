package com.jorge.inventoryapp.data.remote.api

import com.jorge.inventoryapp.data.local.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class UnauthorizedInterceptor :
    Interceptor {

    override fun intercept(
        chain: Interceptor.Chain
    ): Response {

        val response =
            chain.proceed(
                chain.request()
            )

        if (
            response.code == 401
        ) {

            SessionManager
                .clearToken()
        }

        return response
    }
}