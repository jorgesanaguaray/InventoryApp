package com.jorge.inventoryapp.data.remote.dto

data class LoginResponse(
    val token: String,
    val nombre: String,
    val email: String
)