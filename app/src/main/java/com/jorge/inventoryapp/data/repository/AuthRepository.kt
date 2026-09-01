package com.jorge.inventoryapp.data.repository

import com.jorge.inventoryapp.data.remote.api.ApiService
import com.jorge.inventoryapp.data.remote.dto.LoginRequest
import com.jorge.inventoryapp.data.remote.dto.LoginResponse
import com.jorge.inventoryapp.data.local.SessionManager

class AuthRepository(
    private val apiService: ApiService
) {

    suspend fun login(
        email: String,
        password: String
    ): LoginResponse {

        val request =
            LoginRequest(
                email = email.trim(),
                password = password
            )

        val response =
            apiService.login(request)

        SessionManager.saveToken(
            response.token
        )

        return response
    }

}