package com.jorge.inventoryapp.ui.inventario

import com.jorge.inventoryapp.data.remote.dto.InventarioDto
import com.jorge.inventoryapp.data.remote.dto.ProveedorDto

data class InventarioUiState(

    val inventarios: List<InventarioDto> =
        emptyList(),

    val proveedores: List<ProveedorDto> =
        emptyList(),

    val isLoading: Boolean = false,

    val isSaving: Boolean = false,

    val errorMessage: String? = null,

    val successMessage: String? = null,

    val showFormDialog: Boolean = false,

    val inventarioEditando:
    InventarioDto? = null,

    val inventarioPendienteEliminar:
    InventarioDto? = null,

    val proveedorSeleccionadoId:
    Int? = null,

    val numeroLoteFormulario:
    String = "",

    val precioFormulario:
    String = "",

    val stockFormulario:
    String = ""
)