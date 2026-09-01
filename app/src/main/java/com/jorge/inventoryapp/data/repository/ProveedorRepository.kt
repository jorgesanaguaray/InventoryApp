package com.jorge.inventoryapp.data.repository

import com.jorge.inventoryapp.data.remote.api.ApiService
import com.jorge.inventoryapp.data.remote.dto.ProveedorDto

class ProveedorRepository(
    private val apiService: ApiService
) {

    suspend fun obtenerProveedores():
            List<ProveedorDto> {

        return apiService.obtenerProveedores()
    }
}