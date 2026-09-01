package com.jorge.inventoryapp.data.remote.dto

data class ProductoDto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val activo: Boolean
)