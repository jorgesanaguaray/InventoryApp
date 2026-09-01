package com.jorge.inventoryapp.data.remote.api

import com.jorge.inventoryapp.data.remote.dto.InventarioCreateRequest
import com.jorge.inventoryapp.data.remote.dto.InventarioUpdateRequest
import com.jorge.inventoryapp.data.remote.dto.ProveedorDto
import com.jorge.inventoryapp.data.remote.dto.InventarioDto
import com.jorge.inventoryapp.data.remote.dto.LoginRequest
import com.jorge.inventoryapp.data.remote.dto.LoginResponse
import com.jorge.inventoryapp.data.remote.dto.MessageResponse
import com.jorge.inventoryapp.data.remote.dto.ProductoCreateRequest
import com.jorge.inventoryapp.data.remote.dto.ProductoDto
import com.jorge.inventoryapp.data.remote.dto.ProductoUpdateRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("api/Auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @GET("api/Productos")
    suspend fun obtenerProductos():
            List<ProductoDto>

    @POST("api/Productos")
    suspend fun crearProducto(
        @Body request: ProductoCreateRequest
    ): ProductoDto

    @PUT("api/Productos/{id}")
    suspend fun actualizarProducto(
        @Path("id") id: Int,
        @Body request: ProductoUpdateRequest
    ): MessageResponse

    @DELETE("api/Productos/{id}")
    suspend fun eliminarProducto(
        @Path("id") id: Int
    ): MessageResponse

    @GET(
        "api/Inventarios/producto/{productoId}"
    )
    suspend fun obtenerInventariosPorProducto(
        @Path("productoId")
        productoId: Int
    ): List<InventarioDto>

    @GET("api/Proveedores")
    suspend fun obtenerProveedores():
            List<ProveedorDto>

    @POST("api/Inventarios")
    suspend fun crearInventario(
        @Body request: InventarioCreateRequest
    ): InventarioDto

    @PUT("api/Inventarios/{id}")
    suspend fun actualizarInventario(
        @Path("id") id: Int,
        @Body request: InventarioUpdateRequest
    ): MessageResponse

    @DELETE("api/Inventarios/{id}")
    suspend fun eliminarInventario(
        @Path("id") id: Int
    ): MessageResponse

}