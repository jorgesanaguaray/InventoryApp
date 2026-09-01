package com.jorge.inventoryapp.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorge.inventoryapp.data.local.SessionManager
import com.jorge.inventoryapp.data.remote.api.RetrofitClient
import com.jorge.inventoryapp.data.repository.AuthRepository
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel : ViewModel() {

    private val repository =
        AuthRepository(
            RetrofitClient.apiService
        )

    private val _uiState =
        MutableStateFlow(
            LoginUiState()
        )

    val uiState:
            StateFlow<LoginUiState> =
        _uiState.asStateFlow()

    fun onEmailChanged(
        email: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                email = email,
                errorMessage = null
            )
    }

    fun onPasswordChanged(
        password: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                password = password,
                errorMessage = null
            )
    }

    fun login() {

        val state =
            _uiState.value

        if (
            state.email.isBlank() ||
            state.password.isBlank()
        ) {

            _uiState.value =
                state.copy(
                    errorMessage =
                        "Ingrese correo y contraseña."
                )

            return
        }

        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    isLoading = true,
                    errorMessage = null
                )

            try {

                repository.login(
                    email =
                        _uiState.value.email,
                    password =
                        _uiState.value.password
                )

                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false
                    )

            } catch (
                exception: HttpException
            ) {

                Log.e(
                    "LoginViewModel",
                    "Error HTTP durante login",
                    exception
                )

                val message =
                    when (
                        exception.code()
                    ) {

                        401 ->
                            "Correo o contraseña incorrectos."

                        else ->
                            "Error del servidor (${exception.code()})."
                    }

                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        errorMessage =
                            message
                    )

            } catch (
                exception: IOException
            ) {

                Log.e(
                    "LoginViewModel",
                    "Error de conexión durante login",
                    exception
                )

                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        errorMessage =
                            "No se pudo conectar con el servidor."
                    )

            } catch (
                exception: Exception
            ) {

                Log.e(
                    "LoginViewModel",
                    "Error inesperado durante login",
                    exception
                )

                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        errorMessage =
                            "Ocurrió un error inesperado."
                    )
            }
        }
    }

    fun limpiarFormulario() {

        _uiState.value =
            LoginUiState()
    }

    fun cerrarSesion() {

        SessionManager
            .clearToken()

        _uiState.value =
            LoginUiState()
    }
}