package com.jorge.inventoryapp.data.remote.dto

data class InventarioDto(

    val id: Int,

    val productoId: Int,

    val productoNombre: String,

    val proveedorId: Int,

    val proveedorNombre: String,

    val numeroLote: String,

    val precio: Double,

    val stock: Int
)