package com.jorge.inventoryapp.data.repository

import com.jorge.inventoryapp.data.remote.api.ApiService
import com.jorge.inventoryapp.data.remote.dto.InventarioCreateRequest
import com.jorge.inventoryapp.data.remote.dto.InventarioDto
import com.jorge.inventoryapp.data.remote.dto.InventarioUpdateRequest
import com.jorge.inventoryapp.data.remote.dto.MessageResponse

class InventarioRepository(
    private val apiService: ApiService
) {

    suspend fun obtenerPorProducto(
        productoId: Int
    ): List<InventarioDto> {

        return apiService
            .obtenerInventariosPorProducto(
                productoId
            )
    }

    suspend fun crearInventario(
        productoId: Int,
        proveedorId: Int,
        numeroLote: String,
        precio: Double,
        stock: Int
    ): InventarioDto {

        val request =
            InventarioCreateRequest(
                productoId = productoId,
                proveedorId = proveedorId,
                numeroLote = numeroLote.trim(),
                precio = precio,
                stock = stock
            )

        return apiService
            .crearInventario(request)
    }

    suspend fun actualizarInventario(
        id: Int,
        productoId: Int,
        proveedorId: Int,
        numeroLote: String,
        precio: Double,
        stock: Int
    ): MessageResponse {

        val request =
            InventarioUpdateRequest(
                productoId = productoId,
                proveedorId = proveedorId,
                numeroLote = numeroLote.trim(),
                precio = precio,
                stock = stock
            )

        return apiService
            .actualizarInventario(
                id = id,
                request = request
            )
    }

    suspend fun eliminarInventario(
        id: Int
    ): MessageResponse {

        return apiService
            .eliminarInventario(id)
    }
}