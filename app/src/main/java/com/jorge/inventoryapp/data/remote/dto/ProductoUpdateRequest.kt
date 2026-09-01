package com.jorge.inventoryapp.data.remote.dto

data class ProductoUpdateRequest(
    val nombre: String,
    val descripcion: String,
    val activo: Boolean
)