package com.jorge.inventoryapp.ui.productos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jorge.inventoryapp.data.remote.dto.ProductoDto

@Composable
fun ProductosScreen(

    viewModel: ProductosViewModel,

    modifier: Modifier = Modifier,

    onVerInventario:
        (ProductoDto) -> Unit,

    onCerrarSesion:
        () -> Unit
) {

    val state by
    viewModel.uiState
        .collectAsStateWithLifecycle()

    Box(
        modifier =
            modifier.fillMaxSize(),
        contentAlignment =
            Alignment.TopCenter
    ) {

        Column(
            modifier =
                Modifier
                    .widthIn(
                        max = 800.dp
                    )
                    .fillMaxSize()
                    .padding(16.dp)
        ) {

            Text(
                text =
                    "Productos",
                style =
                    MaterialTheme
                        .typography
                        .headlineMedium
            )

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 8.dp
                        ),
                horizontalArrangement =
                    Arrangement
                        .SpaceBetween,
                verticalAlignment =
                    Alignment
                        .CenterVertically
            ) {

                TextButton(
                    onClick =
                        onCerrarSesion
                ) {

                    Text(
                        "Cerrar sesión"
                    )
                }

                Button(
                    onClick =
                        viewModel::
                        abrirCrearProducto
                ) {

                    Text(
                        "Nuevo"
                    )
                }
            }

            state.errorMessage?.let {
                    message ->

                Text(
                    text =
                        message,
                    color =
                        MaterialTheme
                            .colorScheme
                            .error,
                    modifier =
                        Modifier.padding(
                            top = 12.dp
                        )
                )
            }

            state.successMessage?.let {
                    message ->

                Text(
                    text =
                        message,
                    modifier =
                        Modifier.padding(
                            top = 12.dp
                        )
                )
            }

            when {

                state.isLoading -> {

                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    32.dp
                                ),
                        horizontalAlignment =
                            Alignment
                                .CenterHorizontally
                    ) {

                        CircularProgressIndicator()

                        Text(
                            text =
                                "Cargando productos...",
                            modifier =
                                Modifier.padding(
                                    top = 12.dp
                                )
                        )
                    }
                }

                state.productos.isEmpty() -> {

                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    32.dp
                                ),
                        horizontalAlignment =
                            Alignment
                                .CenterHorizontally
                    ) {

                        Text(
                            text =
                                "No hay productos registrados."
                        )

                        Text(
                            text =
                                "Presione Nuevo para crear el primero.",
                            style =
                                MaterialTheme
                                    .typography
                                    .bodyMedium,
                            modifier =
                                Modifier.padding(
                                    top = 8.dp
                                )
                        )
                    }
                }

                else -> {

                    LazyColumn(
                        modifier =
                            Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                        contentPadding =
                            PaddingValues(
                                vertical =
                                    16.dp
                            ),
                        verticalArrangement =
                            Arrangement
                                .spacedBy(
                                    12.dp
                                )
                    ) {

                        items(
                            items =
                                state.productos,
                            key = {
                                    producto ->

                                producto.id
                            }
                        ) {
                                producto ->

                            ProductoItem(

                                producto =
                                    producto,

                                onEditar = {

                                    viewModel
                                        .abrirEditarProducto(
                                            producto
                                        )
                                },

                                onEliminar = {

                                    viewModel
                                        .solicitarEliminarProducto(
                                            producto
                                        )
                                },

                                onInventario = {

                                    onVerInventario(
                                        producto
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    state
        .productoPendienteEliminar
        ?.let {
                producto ->

            AlertDialog(

                onDismissRequest =
                    viewModel::
                    cancelarEliminarProducto,

                title = {

                    Text(
                        "Eliminar producto"
                    )
                },

                text = {

                    Text(
                        "¿Está seguro de eliminar ${producto.nombre}?"
                    )
                },

                confirmButton = {

                    TextButton(
                        onClick =
                            viewModel::
                            confirmarEliminarProducto
                    ) {

                        Text(
                            "Eliminar"
                        )
                    }
                },

                dismissButton = {

                    TextButton(
                        onClick =
                            viewModel::
                            cancelarEliminarProducto
                    ) {

                        Text(
                            "Cancelar"
                        )
                    }
                }
            )
        }

    if (
        state.showFormDialog
    ) {

        ProductoFormDialog(

            state =
                state,

            onNombreChanged =
                viewModel::
                onNombreChanged,

            onDescripcionChanged =
                viewModel::
                onDescripcionChanged,

            onActivoChanged =
                viewModel::
                onActivoChanged,

            onGuardar =
                viewModel::
                guardarProducto,

            onCerrar =
                viewModel::
                cerrarFormulario
        )
    }
}

@Composable
private fun ProductoItem(

    producto: ProductoDto,

    onInventario: () -> Unit,

    onEditar: () -> Unit,

    onEliminar: () -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        Column(
            modifier =
                Modifier.padding(
                    16.dp
                )
        ) {

            Text(
                text =
                    producto.nombre,
                style =
                    MaterialTheme
                        .typography
                        .titleMedium
            )

            Text(
                text =
                    producto.descripcion,
                modifier =
                    Modifier.padding(
                        top = 4.dp
                    )
            )

            Text(
                text =
                    if (
                        producto.activo
                    ) {
                        "Activo"
                    } else {
                        "Inactivo"
                    },
                modifier =
                    Modifier.padding(
                        top = 8.dp
                    )
            )

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 12.dp
                        ),
                horizontalArrangement =
                    Arrangement.End
            ) {

                TextButton(
                    onClick =
                        onEditar
                ) {

                    Text(
                        "Editar"
                    )
                }

                TextButton(
                    onClick =
                        onEliminar
                ) {

                    Text(
                        "Eliminar"
                    )
                }

                TextButton(
                    onClick =
                        onInventario
                ) {

                    Text(
                        "Inventario"
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductoFormDialog(

    state:
    ProductosUiState,

    onNombreChanged:
        (String) -> Unit,

    onDescripcionChanged:
        (String) -> Unit,

    onActivoChanged:
        (Boolean) -> Unit,

    onGuardar:
        () -> Unit,

    onCerrar:
        () -> Unit
) {

    val esEdicion =
        state.productoEditando != null

    AlertDialog(

        onDismissRequest =
            onCerrar,

        title = {

            Text(
                if (
                    esEdicion
                ) {
                    "Editar producto"
                } else {
                    "Nuevo producto"
                }
            )
        },

        text = {

            Column(
                modifier =
                    Modifier.verticalScroll(
                        rememberScrollState()
                    )
            ) {

                state.errorMessage?.let {
                        message ->

                    Text(
                        text =
                            message,
                        color =
                            MaterialTheme
                                .colorScheme
                                .error,
                        modifier =
                            Modifier.padding(
                                bottom = 8.dp
                            )
                    )
                }

                OutlinedTextField(
                    value =
                        state
                            .nombreFormulario,
                    onValueChange =
                        onNombreChanged,
                    label = {
                        Text(
                            "Nombre"
                        )
                    },
                    modifier =
                        Modifier.fillMaxWidth(),
                    singleLine =
                        true
                )

                OutlinedTextField(
                    value =
                        state
                            .descripcionFormulario,
                    onValueChange =
                        onDescripcionChanged,
                    label = {
                        Text(
                            "Descripción"
                        )
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 12.dp
                            )
                )

                if (
                    esEdicion
                ) {

                    Row(
                        verticalAlignment =
                            Alignment
                                .CenterVertically,
                        modifier =
                            Modifier.padding(
                                top = 12.dp
                            )
                    ) {

                        Checkbox(
                            checked =
                                state
                                    .activoFormulario,
                            onCheckedChange =
                                onActivoChanged
                        )

                        Text(
                            "Producto activo"
                        )
                    }
                }

                if (
                    state.isSaving
                ) {

                    CircularProgressIndicator(
                        modifier =
                            Modifier.padding(
                                top = 12.dp
                            )
                    )
                }
            }
        },

        confirmButton = {

            TextButton(
                onClick =
                    onGuardar,
                enabled =
                    !state.isSaving
            ) {

                Text(
                    "Guardar"
                )
            }
        },

        dismissButton = {

            TextButton(
                onClick =
                    onCerrar,
                enabled =
                    !state.isSaving
            ) {

                Text(
                    "Cancelar"
                )
            }
        }
    )
}