package com.jorge.inventoryapp.ui.inventario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jorge.inventoryapp.data.remote.dto.InventarioDto
import com.jorge.inventoryapp.data.remote.dto.ProductoDto

@Composable
fun InventarioScreen(

    producto: ProductoDto,

    viewModel: InventarioViewModel,

    modifier: Modifier = Modifier,

    onVolver: () -> Unit
) {

    val state by
    viewModel.uiState
        .collectAsStateWithLifecycle()

    LaunchedEffect(
        producto.id
    ) {

        viewModel.cargarDatos(
            producto.id
        )
    }

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
                    .padding(
                        16.dp
                    )
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement
                        .SpaceBetween,
                verticalAlignment =
                    Alignment
                        .CenterVertically
            ) {

                TextButton(
                    onClick =
                        onVolver
                ) {

                    Text(
                        "← Volver"
                    )
                }

                Button(
                    onClick =
                        viewModel::
                        abrirCrearInventario
                ) {

                    Text(
                        "Nuevo"
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(
                        16.dp
                    )
            )

            Text(
                text =
                    producto.nombre,
                style =
                    MaterialTheme
                        .typography
                        .headlineMedium
            )

            Text(
                text =
                    "Inventario por proveedor y lote",
                modifier =
                    Modifier.padding(
                        top = 4.dp,
                        bottom = 16.dp
                    )
            )

            state.successMessage?.let {
                    message ->

                Text(
                    text =
                        message,
                    modifier =
                        Modifier.padding(
                            bottom = 12.dp
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
                                "Cargando inventario...",
                            modifier =
                                Modifier.padding(
                                    top = 12.dp
                                )
                        )
                    }
                }

                state.errorMessage != null -> {

                    Text(
                        text =
                            state.errorMessage
                                ?: "Error desconocido",
                        color =
                            MaterialTheme
                                .colorScheme
                                .error
                    )
                }

                state.inventarios.isEmpty() -> {

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
                                "Este producto todavía no tiene inventario."
                        )

                        Text(
                            text =
                                "Presione Nuevo para agregar proveedor, lote, precio y stock.",
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
                                    8.dp
                            ),
                        verticalArrangement =
                            Arrangement
                                .spacedBy(
                                    12.dp
                                )
                    ) {

                        items(
                            items =
                                state.inventarios,
                            key = {
                                    inventario ->

                                inventario.id
                            }
                        ) {
                                inventario ->

                            InventarioItem(

                                inventario =
                                    inventario,

                                onEditar = {

                                    viewModel
                                        .abrirEditarInventario(
                                            inventario
                                        )
                                },

                                onEliminar = {

                                    viewModel
                                        .solicitarEliminarInventario(
                                            inventario
                                        )
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (
        state.showFormDialog
    ) {

        InventarioFormDialog(

            state =
                state,

            onProveedorChanged =
                viewModel::
                onProveedorChanged,

            onNumeroLoteChanged =
                viewModel::
                onNumeroLoteChanged,

            onPrecioChanged =
                viewModel::
                onPrecioChanged,

            onStockChanged =
                viewModel::
                onStockChanged,

            onGuardar =
                viewModel::
                guardarInventario,

            onCerrar =
                viewModel::
                cerrarFormulario
        )
    }

    state
        .inventarioPendienteEliminar
        ?.let {
                inventario ->

            AlertDialog(

                onDismissRequest =
                    viewModel::
                    cancelarEliminarInventario,

                title = {

                    Text(
                        "Eliminar inventario"
                    )
                },

                text = {

                    Text(
                        "¿Está seguro de eliminar el lote ${inventario.numeroLote}?"
                    )
                },

                confirmButton = {

                    TextButton(
                        onClick =
                            viewModel::
                            confirmarEliminarInventario
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
                            cancelarEliminarInventario
                    ) {

                        Text(
                            "Cancelar"
                        )
                    }
                }
            )
        }
}

@Composable
private fun InventarioItem(

    inventario:
    InventarioDto,

    onEditar:
        () -> Unit,

    onEliminar:
        () -> Unit
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
                    inventario
                        .proveedorNombre,
                style =
                    MaterialTheme
                        .typography
                        .titleMedium
            )

            Text(
                text =
                    "Lote: ${inventario.numeroLote}"
            )

            Text(
                text =
                    "Precio: $${"%.2f".format(inventario.precio)}"
            )

            Text(
                text =
                    "Stock: ${inventario.stock}"
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
            }
        }
    }
}

@Composable
private fun InventarioFormDialog(

    state:
    InventarioUiState,

    onProveedorChanged:
        (Int) -> Unit,

    onNumeroLoteChanged:
        (String) -> Unit,

    onPrecioChanged:
        (String) -> Unit,

    onStockChanged:
        (String) -> Unit,

    onGuardar:
        () -> Unit,

    onCerrar:
        () -> Unit
) {

    var menuExpanded by
    remember {
        mutableStateOf(
            false
        )
    }

    val proveedorSeleccionado =
        state.proveedores
            .firstOrNull {
                    proveedor ->

                proveedor.id ==
                        state
                            .proveedorSeleccionadoId
            }

    val esEdicion =
        state.inventarioEditando !=
                null

    AlertDialog(

        onDismissRequest =
            onCerrar,

        title = {

            Text(
                if (
                    esEdicion
                ) {
                    "Editar inventario"
                } else {
                    "Nuevo inventario"
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

                Text(
                    "Proveedor"
                )

                Box(
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Button(
                        onClick = {

                            menuExpanded =
                                true
                        },
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Text(
                            proveedorSeleccionado
                                ?.nombre
                                ?: "Seleccionar proveedor"
                        )
                    }

                    DropdownMenu(
                        expanded =
                            menuExpanded,
                        onDismissRequest = {

                            menuExpanded =
                                false
                        }
                    ) {

                        state
                            .proveedores
                            .forEach {
                                    proveedor ->

                                DropdownMenuItem(
                                    text = {

                                        Text(
                                            proveedor.nombre
                                        )
                                    },
                                    onClick = {

                                        onProveedorChanged(
                                            proveedor.id
                                        )

                                        menuExpanded =
                                            false
                                    }
                                )
                            }
                    }
                }

                OutlinedTextField(
                    value =
                        state
                            .numeroLoteFormulario,
                    onValueChange =
                        onNumeroLoteChanged,
                    label = {

                        Text(
                            "Número de lote"
                        )
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 12.dp
                            )
                )

                OutlinedTextField(
                    value =
                        state
                            .precioFormulario,
                    onValueChange =
                        onPrecioChanged,
                    label = {

                        Text(
                            "Precio"
                        )
                    },
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType
                                    .Decimal
                        ),
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 12.dp
                            )
                )

                OutlinedTextField(
                    value =
                        state
                            .stockFormulario,
                    onValueChange =
                        onStockChanged,
                    label = {

                        Text(
                            "Stock"
                        )
                    },
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType
                                    .Number
                        ),
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 12.dp
                            )
                )

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