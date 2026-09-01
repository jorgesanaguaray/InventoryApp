package com.jorge.inventoryapp.ui.productos

import com.jorge.inventoryapp.data.remote.dto.ProductoDto

data class ProductosUiState(

    val productos: List<ProductoDto> =
        emptyList(),

    val isLoading: Boolean = false,

    val isSaving: Boolean = false,

    val errorMessage: String? = null,

    val successMessage: String? = null,

    val showFormDialog: Boolean = false,

    val productoEditando: ProductoDto? = null,

    val productoPendienteEliminar: ProductoDto? = null,

    val nombreFormulario: String = "",

    val descripcionFormulario: String = "",

    val activoFormulario: Boolean = true
)