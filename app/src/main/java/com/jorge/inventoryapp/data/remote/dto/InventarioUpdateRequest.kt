package com.jorge.inventoryapp.data.remote.dto

data class InventarioUpdateRequest(
    val productoId: Int,
    val proveedorId: Int,
    val numeroLote: String,
    val precio: Double,
    val stock: Int
)