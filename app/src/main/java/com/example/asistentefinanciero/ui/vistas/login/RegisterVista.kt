package com.example.asistentefinanciero.ui.vistas.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

import com.example.asistentefinanciero.viewmodel.AuthViewModel

@Composable
fun RegisterVista(
    viewModel: AuthViewModel,
    onRegistroExitoso: () -> Unit,
    onIrALogin: () -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }

    val cargando by viewModel.cargando.collectAsState()
    val errorMensaje by viewModel.errorMensaje.collectAsState()

    // Limpiar error al entrar a la vista
    LaunchedEffect(Unit) {
        viewModel.limpiarError()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Crear Cuenta",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !cargando,
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña (mínimo 6 caracteres)") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            enabled = !cargando,
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmarContrasena,
            onValueChange = { confirmarContrasena = it },
            label = { Text("Confirmar contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            enabled = !cargando,
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))

        // Mostrar error si existe
        errorMensaje?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                when {
                    correo.isBlank() -> {
                        viewModel.setError("Ingrese su correo electrónico")
                    }
                    contrasena.isBlank() -> {
                        viewModel.setError("Ingrese su contraseña")
                    }
                    confirmarContrasena.isBlank() -> {
                        viewModel.setError("Confirme su contraseña")
                    }
                    contrasena != confirmarContrasena -> {
                        viewModel.setError("Las contraseñas no coinciden")
                    }
                    contrasena.length < 6 -> {
                        viewModel.setError("La contraseña debe tener al menos 6 caracteres")
                    }
                    else -> {
                        viewModel.registrar(correo, contrasena) {
                            onRegistroExitoso()
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !cargando
        ) {
            if (cargando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Registrarse")
            }
        }

        Spacer(Modifier.height(16.dp))

        TextButton(
            onClick = {
                viewModel.limpiarError()
                onIrALogin()
            }
        ) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}