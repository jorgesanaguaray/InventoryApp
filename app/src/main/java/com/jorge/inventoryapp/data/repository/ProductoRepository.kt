package com.jorge.inventoryapp.data.repository

import com.jorge.inventoryapp.data.remote.api.ApiService
import com.jorge.inventoryapp.data.remote.dto.MessageResponse
import com.jorge.inventoryapp.data.remote.dto.ProductoCreateRequest
import com.jorge.inventoryapp.data.remote.dto.ProductoDto
import com.jorge.inventoryapp.data.remote.dto.ProductoUpdateRequest

class ProductoRepository(
    private val apiService: ApiService
) {

    suspend fun obtenerProductos():
            List<ProductoDto> {

        return apiService.obtenerProductos()
    }

    suspend fun crearProducto(
        nombre: String,
        descripcion: String
    ): ProductoDto {

        val request =
            ProductoCreateRequest(
                nombre = nombre.trim(),
                descripcion = descripcion.trim()
            )

        return apiService
            .crearProducto(request)
    }

    suspend fun actualizarProducto(
        id: Int,
        nombre: String,
        descripcion: String,
        activo: Boolean
    ): MessageResponse {

        val request =
            ProductoUpdateRequest(
                nombre = nombre.trim(),
                descripcion = descripcion.trim(),
                activo = activo
            )

        return apiService
            .actualizarProducto(
                id = id,
                request = request
            )
    }

    suspend fun eliminarProducto(
        id: Int
    ): MessageResponse {

        return apiService
            .eliminarProducto(id)
    }
}