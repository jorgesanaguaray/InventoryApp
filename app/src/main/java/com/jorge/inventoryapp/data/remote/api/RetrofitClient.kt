package com.jorge.inventoryapp.data.remote.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL =
        "http://127.0.0.1:5133/"

    private val loggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

    private val authInterceptor =
        AuthInterceptor()

    private val unauthorizedInterceptor =
        UnauthorizedInterceptor()

    private val okHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                authInterceptor
            )
            .addInterceptor(
                unauthorizedInterceptor
            )
            .addInterceptor(
                loggingInterceptor
            )
            .build()

    val apiService: ApiService by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(ApiService::class.java)
    }

}