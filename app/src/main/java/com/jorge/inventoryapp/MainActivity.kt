package com.jorge.inventoryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jorge.inventoryapp.data.local.SessionManager
import com.jorge.inventoryapp.data.remote.dto.ProductoDto
import com.jorge.inventoryapp.ui.inventario.InventarioScreen
import com.jorge.inventoryapp.ui.inventario.InventarioViewModel
import com.jorge.inventoryapp.ui.login.LoginScreen
import com.jorge.inventoryapp.ui.login.LoginViewModel
import com.jorge.inventoryapp.ui.productos.ProductosScreen
import com.jorge.inventoryapp.ui.productos.ProductosViewModel
import com.jorge.inventoryapp.ui.theme.InventoryAppTheme

class MainActivity : ComponentActivity() {

    private val loginViewModel by
    viewModels<LoginViewModel>()

    private val productosViewModel by
    viewModels<ProductosViewModel>()

    private val inventarioViewModel by
    viewModels<InventarioViewModel>()

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            InventoryAppTheme {

                Scaffold(
                    modifier =
                        Modifier.fillMaxSize()
                ) { innerPadding ->

                    val token by
                    SessionManager
                        .token
                        .collectAsStateWithLifecycle()

                    var productoSeleccionado by
                    remember {
                        mutableStateOf<
                                ProductoDto?
                                >(null)
                    }

                    LaunchedEffect(token) {

                        if (token == null) {

                            productoSeleccionado =
                                null

                            loginViewModel
                                .limpiarFormulario()
                        }
                    }

                    if (token != null) {

                        val producto =
                            productoSeleccionado

                        if (producto == null) {

                            ProductosScreen(

                                viewModel =
                                    productosViewModel,

                                modifier =
                                    Modifier.padding(
                                        innerPadding
                                    ),

                                onVerInventario = {
                                        productoElegido ->

                                    productoSeleccionado =
                                        productoElegido
                                },

                                onCerrarSesion = {

                                    loginViewModel
                                        .cerrarSesion()

                                    productoSeleccionado =
                                        null
                                }
                            )

                        } else {

                            InventarioScreen(

                                producto =
                                    producto,

                                viewModel =
                                    inventarioViewModel,

                                modifier =
                                    Modifier.padding(
                                        innerPadding
                                    ),

                                onVolver = {

                                    productoSeleccionado =
                                        null
                                }
                            )
                        }

                    } else {

                        LoginScreen(

                            viewModel =
                                loginViewModel,

                            modifier =
                                Modifier.padding(
                                    innerPadding
                                )
                        )
                    }
                }
            }
        }
    }
}