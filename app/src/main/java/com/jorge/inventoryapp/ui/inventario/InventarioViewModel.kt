package com.jorge.inventoryapp.ui.inventario

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorge.inventoryapp.data.remote.api.RetrofitClient
import com.jorge.inventoryapp.data.remote.dto.InventarioDto
import com.jorge.inventoryapp.data.repository.InventarioRepository
import com.jorge.inventoryapp.data.repository.ProveedorRepository
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class InventarioViewModel :
    ViewModel() {

    private val inventarioRepository =
        InventarioRepository(
            RetrofitClient.apiService
        )

    private val proveedorRepository =
        ProveedorRepository(
            RetrofitClient.apiService
        )

    private val _uiState =
        MutableStateFlow(
            InventarioUiState()
        )

    val uiState:
            StateFlow<InventarioUiState> =
        _uiState.asStateFlow()

    private var productoIdActual:
            Int? = null

    fun cargarDatos(
        productoId: Int
    ) {

        productoIdActual =
            productoId

        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    isLoading =
                        true,
                    errorMessage =
                        null
                )

            try {

                val proveedores =
                    proveedorRepository
                        .obtenerProveedores()

                val inventarios =
                    inventarioRepository
                        .obtenerPorProducto(
                            productoId
                        )

                _uiState.value =
                    _uiState.value.copy(
                        inventarios =
                            inventarios,
                        proveedores =
                            proveedores,
                        isLoading =
                            false
                    )

            } catch (
                exception: Exception
            ) {

                manejarError(
                    exception
                )
            }
        }
    }

    fun abrirCrearInventario() {

        val primerProveedor =
            _uiState.value
                .proveedores
                .firstOrNull()

        if (
            primerProveedor ==
            null
        ) {

            _uiState.value =
                _uiState.value.copy(
                    errorMessage =
                        "Debe registrar un proveedor primero."
                )

            return
        }

        _uiState.value =
            _uiState.value.copy(
                showFormDialog =
                    true,
                inventarioEditando =
                    null,
                proveedorSeleccionadoId =
                    primerProveedor.id,
                numeroLoteFormulario =
                    "",
                precioFormulario =
                    "",
                stockFormulario =
                    "",
                errorMessage =
                    null,
                successMessage =
                    null
            )
    }

    fun abrirEditarInventario(
        inventario: InventarioDto
    ) {

        _uiState.value =
            _uiState.value.copy(
                showFormDialog =
                    true,
                inventarioEditando =
                    inventario,
                proveedorSeleccionadoId =
                    inventario.proveedorId,
                numeroLoteFormulario =
                    inventario.numeroLote,
                precioFormulario =
                    inventario.precio
                        .toString(),
                stockFormulario =
                    inventario.stock
                        .toString(),
                errorMessage =
                    null,
                successMessage =
                    null
            )
    }

    fun cerrarFormulario() {

        _uiState.value =
            _uiState.value.copy(
                showFormDialog =
                    false,
                inventarioEditando =
                    null,
                errorMessage =
                    null
            )
    }

    fun onProveedorChanged(
        proveedorId: Int
    ) {

        _uiState.value =
            _uiState.value.copy(
                proveedorSeleccionadoId =
                    proveedorId,
                errorMessage =
                    null
            )
    }

    fun onNumeroLoteChanged(
        numeroLote: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                numeroLoteFormulario =
                    numeroLote,
                errorMessage =
                    null
            )
    }

    fun onPrecioChanged(
        precio: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                precioFormulario =
                    precio,
                errorMessage =
                    null
            )
    }

    fun onStockChanged(
        stock: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                stockFormulario =
                    stock,
                errorMessage =
                    null
            )
    }

    fun guardarInventario() {

        val state =
            _uiState.value

        val productoId =
            productoIdActual
                ?: return

        val proveedorId =
            state
                .proveedorSeleccionadoId

        if (
            proveedorId ==
            null
        ) {

            mostrarError(
                "Seleccione un proveedor."
            )

            return
        }

        if (
            state
                .numeroLoteFormulario
                .isBlank()
        ) {

            mostrarError(
                "El número de lote es obligatorio."
            )

            return
        }

        val precio =
            state
                .precioFormulario
                .toDoubleOrNull()

        if (
            precio == null ||
            precio <= 0
        ) {

            mostrarError(
                "Ingrese un precio válido mayor a cero."
            )

            return
        }

        val stock =
            state
                .stockFormulario
                .toIntOrNull()

        if (
            stock == null ||
            stock < 0
        ) {

            mostrarError(
                "Ingrese un stock válido."
            )

            return
        }

        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    isSaving =
                        true,
                    errorMessage =
                        null,
                    successMessage =
                        null
                )

            try {

                val inventarioEditando =
                    _uiState.value
                        .inventarioEditando

                if (
                    inventarioEditando ==
                    null
                ) {

                    inventarioRepository
                        .crearInventario(
                            productoId =
                                productoId,
                            proveedorId =
                                proveedorId,
                            numeroLote =
                                state
                                    .numeroLoteFormulario,
                            precio =
                                precio,
                            stock =
                                stock
                        )

                    _uiState.value =
                        _uiState.value.copy(
                            successMessage =
                                "Inventario creado correctamente."
                        )

                } else {

                    inventarioRepository
                        .actualizarInventario(
                            id =
                                inventarioEditando.id,
                            productoId =
                                productoId,
                            proveedorId =
                                proveedorId,
                            numeroLote =
                                state
                                    .numeroLoteFormulario,
                            precio =
                                precio,
                            stock =
                                stock
                        )

                    _uiState.value =
                        _uiState.value.copy(
                            successMessage =
                                "Inventario actualizado correctamente."
                        )
                }

                _uiState.value =
                    _uiState.value.copy(
                        isSaving =
                            false,
                        showFormDialog =
                            false,
                        inventarioEditando =
                            null
                    )

                cargarDatos(
                    productoId
                )

            } catch (
                exception: Exception
            ) {

                manejarError(
                    exception
                )
            }
        }
    }

    fun solicitarEliminarInventario(
        inventario: InventarioDto
    ) {

        _uiState.value =
            _uiState.value.copy(
                inventarioPendienteEliminar =
                    inventario
            )
    }

    fun cancelarEliminarInventario() {

        _uiState.value =
            _uiState.value.copy(
                inventarioPendienteEliminar =
                    null
            )
    }

    fun confirmarEliminarInventario() {

        val inventario =
            _uiState.value
                .inventarioPendienteEliminar
                ?: return

        val productoId =
            productoIdActual
                ?: return

        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    isSaving =
                        true,
                    errorMessage =
                        null,
                    successMessage =
                        null
                )

            try {

                inventarioRepository
                    .eliminarInventario(
                        inventario.id
                    )

                _uiState.value =
                    _uiState.value.copy(
                        isSaving =
                            false,
                        inventarioPendienteEliminar =
                            null,
                        successMessage =
                            "Inventario eliminado correctamente."
                    )

                cargarDatos(
                    productoId
                )

            } catch (
                exception: Exception
            ) {

                _uiState.value =
                    _uiState.value.copy(
                        inventarioPendienteEliminar =
                            null
                    )

                manejarError(
                    exception
                )
            }
        }
    }

    private fun mostrarError(
        message: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                errorMessage =
                    message
            )
    }

    private fun manejarError(
        exception: Exception
    ) {

        Log.e(
            "InventarioViewModel",
            "Error gestionando inventario",
            exception
        )

        val message =
            when (
                exception
            ) {

                is HttpException -> {

                    when (
                        exception.code()
                    ) {

                        400 ->
                            "Los datos enviados no son válidos."

                        401 ->
                            "La sesión no es válida."

                        404 ->
                            "El registro solicitado no existe."

                        else ->
                            "Error del servidor (${exception.code()})."
                    }
                }

                is IOException ->
                    "No se pudo conectar con el servidor."

                else ->
                    "Ocurrió un error inesperado."
            }

        _uiState.value =
            _uiState.value.copy(
                isLoading =
                    false,
                isSaving =
                    false,
                errorMessage =
                    message
            )
    }
}