package com.jorge.inventoryapp.data.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SessionManager {

    private val _token =
        MutableStateFlow<String?>(null)

    val token:
            StateFlow<String?> =
        _token.asStateFlow()

    fun saveToken(
        newToken: String
    ) {
        _token.value =
            newToken
    }

    fun getToken():
            String? {

        return _token.value
    }

    fun clearToken() {
        _token.value =
            null
    }
}