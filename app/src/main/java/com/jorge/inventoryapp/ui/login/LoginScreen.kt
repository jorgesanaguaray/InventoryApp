package com.jorge.inventoryapp.ui.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoginScreen(

    viewModel: LoginViewModel,

    modifier: Modifier = Modifier
) {

    val state by
    viewModel.uiState
        .collectAsStateWithLifecycle()

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .padding(24.dp)
                .imePadding(),
        contentAlignment =
            Alignment.Center
    ) {

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .widthIn(
                        max = 480.dp
                    )
                    .verticalScroll(
                        rememberScrollState()
                    ),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(
                text =
                    "Inventory App",
                style =
                    MaterialTheme
                        .typography
                        .headlineMedium
            )

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )

            OutlinedTextField(
                value =
                    state.email,
                onValueChange =
                    viewModel::onEmailChanged,
                modifier =
                    Modifier.fillMaxWidth(),
                label = {
                    Text("Correo")
                },
                singleLine = true,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Email
                    )
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            OutlinedTextField(
                value =
                    state.password,
                onValueChange =
                    viewModel::onPasswordChanged,
                modifier =
                    Modifier.fillMaxWidth(),
                label = {
                    Text("Contraseña")
                },
                singleLine = true,
                visualTransformation =
                    PasswordVisualTransformation()
            )

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            state.errorMessage?.let {
                    message ->

                Text(
                    text = message,
                    color =
                        MaterialTheme
                            .colorScheme
                            .error
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )
            }

            Button(
                onClick =
                    viewModel::login,
                enabled =
                    !state.isLoading,
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                if (
                    state.isLoading
                ) {

                    CircularProgressIndicator()

                } else {

                    Text(
                        "Iniciar sesión"
                    )
                }
            }
        }
    }
}