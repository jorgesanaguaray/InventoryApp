package com.jorge.inventoryapp.ui.productos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorge.inventoryapp.data.remote.api.RetrofitClient
import com.jorge.inventoryapp.data.remote.dto.ProductoDto
import com.jorge.inventoryapp.data.repository.ProductoRepository
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProductosViewModel : ViewModel() {

    private val repository =
        ProductoRepository(
            RetrofitClient.apiService
        )

    private val _uiState =
        MutableStateFlow(
            ProductosUiState()
        )

    val uiState:
            StateFlow<ProductosUiState> =
        _uiState.asStateFlow()

    init {

        cargarProductos()
    }

    fun cargarProductos() {

        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    isLoading = true,
                    errorMessage = null
                )

            try {

                val productos =
                    repository
                        .obtenerProductos()

                _uiState.value =
                    _uiState.value.copy(
                        productos =
                            productos,
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

    fun abrirCrearProducto() {

        _uiState.value =
            _uiState.value.copy(
                showFormDialog =
                    true,
                productoEditando =
                    null,
                nombreFormulario =
                    "",
                descripcionFormulario =
                    "",
                activoFormulario =
                    true,
                errorMessage =
                    null,
                successMessage =
                    null
            )
    }

    fun abrirEditarProducto(
        producto: ProductoDto
    ) {

        _uiState.value =
            _uiState.value.copy(
                showFormDialog =
                    true,
                productoEditando =
                    producto,
                nombreFormulario =
                    producto.nombre,
                descripcionFormulario =
                    producto.descripcion,
                activoFormulario =
                    producto.activo,
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
                productoEditando =
                    null,
                errorMessage =
                    null
            )
    }

    fun onNombreChanged(
        nombre: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                nombreFormulario =
                    nombre,
                errorMessage =
                    null
            )
    }

    fun onDescripcionChanged(
        descripcion: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                descripcionFormulario =
                    descripcion,
                errorMessage =
                    null
            )
    }

    fun onActivoChanged(
        activo: Boolean
    ) {

        _uiState.value =
            _uiState.value.copy(
                activoFormulario =
                    activo
            )
    }

    fun guardarProducto() {

        val state =
            _uiState.value

        if (
            state
                .nombreFormulario
                .isBlank()
        ) {

            _uiState.value =
                state.copy(
                    errorMessage =
                        "El nombre es obligatorio."
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

                val productoEditando =
                    _uiState.value
                        .productoEditando

                if (
                    productoEditando ==
                    null
                ) {

                    repository
                        .crearProducto(
                            nombre =
                                _uiState
                                    .value
                                    .nombreFormulario,
                            descripcion =
                                _uiState
                                    .value
                                    .descripcionFormulario
                        )

                    _uiState.value =
                        _uiState.value.copy(
                            successMessage =
                                "Producto creado correctamente."
                        )

                } else {

                    repository
                        .actualizarProducto(
                            id =
                                productoEditando.id,
                            nombre =
                                _uiState
                                    .value
                                    .nombreFormulario,
                            descripcion =
                                _uiState
                                    .value
                                    .descripcionFormulario,
                            activo =
                                _uiState
                                    .value
                                    .activoFormulario
                        )

                    _uiState.value =
                        _uiState.value.copy(
                            successMessage =
                                "Producto actualizado correctamente."
                        )
                }

                _uiState.value =
                    _uiState.value.copy(
                        isSaving =
                            false,
                        showFormDialog =
                            false,
                        productoEditando =
                            null
                    )

                cargarProductos()

            } catch (
                exception: Exception
            ) {

                manejarError(
                    exception
                )
            }
        }
    }

    fun solicitarEliminarProducto(
        producto: ProductoDto
    ) {

        _uiState.value =
            _uiState.value.copy(
                productoPendienteEliminar =
                    producto
            )
    }

    fun cancelarEliminarProducto() {

        _uiState.value =
            _uiState.value.copy(
                productoPendienteEliminar =
                    null
            )
    }

    fun confirmarEliminarProducto() {

        val producto =
            _uiState.value
                .productoPendienteEliminar
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

                repository
                    .eliminarProducto(
                        producto.id
                    )

                _uiState.value =
                    _uiState.value.copy(
                        isSaving =
                            false,
                        productoPendienteEliminar =
                            null,
                        successMessage =
                            "Producto eliminado correctamente."
                    )

                cargarProductos()

            } catch (
                exception: Exception
            ) {

                _uiState.value =
                    _uiState.value.copy(
                        productoPendienteEliminar =
                            null
                    )

                manejarError(
                    exception
                )
            }
        }
    }

    private fun manejarError(
        exception: Exception
    ) {

        Log.e(
            "ProductosViewModel",
            "Error gestionando productos",
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
                            "No se pudo realizar la operación. El producto puede tener inventario asociado."

                        401 ->
                            "La sesión no es válida."

                        404 ->
                            "El producto no existe."

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