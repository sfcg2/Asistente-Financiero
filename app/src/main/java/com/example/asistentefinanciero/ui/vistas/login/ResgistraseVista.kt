package com.example.asistentefinanciero.ui.vistas.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asistentefinanciero.data.model.Usuario
import com.example.asistentefinanciero.data.repository.UsuarioRepository
import com.example.asistentefinanciero.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarseVista(
    onRegistroExitoso: () -> Unit = {},
    onVolverLogin: () -> Unit = {}
) {
    val context = LocalContext.current
    val repository = remember { UsuarioRepository(context) }

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var saldoInicial by remember { mutableStateOf("") }

    var mensajeError by remember { mutableStateOf("") }
    var mostrarError by remember { mutableStateOf(false) }
    var cargando by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Botón volver
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = onVolverLogin) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = TextPrimary
                    )
                }
            }

            // Logo
            Icon(
                imageVector = Icons.Default.AccountBalance,
                contentDescription = "Logo",
                modifier = Modifier.size(80.dp),
                tint = PrimaryPurple
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "ControlCash",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Card con formulario
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = "Crear Cuenta",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPurple,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Campo Nombre
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Nombre completo", color = Color(0xFFB4B4B4)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Correo
                    OutlinedTextField(
                        value = correo,
                        onValueChange = { correo = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Correo", color = Color(0xFFB4B4B4)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Contraseña
                    OutlinedTextField(
                        value = contrasena,
                        onValueChange = { contrasena = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Contraseña", color = Color(0xFFB4B4B4)) },
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Confirmar Contraseña
                    OutlinedTextField(
                        value = confirmarContrasena,
                        onValueChange = { confirmarContrasena = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Confirmar contraseña", color = Color(0xFFB4B4B4)) },
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Saldo Inicial (opcional)
                    OutlinedTextField(
                        value = saldoInicial,
                        onValueChange = { newValue ->
                            // Permitir solo números, punto y coma
                            if (newValue.isEmpty() || newValue.matches(Regex("^[0-9.,]*$"))) {
                                saldoInicial = newValue
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Ej: 100.000 o 100.000,50", color = Color(0xFFB4B4B4)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        supportingText = {
                            Text(
                                "Use punto para miles y coma para decimales",
                                fontSize = 12.sp,
                                color = Color(0xFF666666)
                            )
                        }
                    )

                    if (mostrarError) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = mensajeError,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón Registrar
                    Button(
                        onClick = {
                            mostrarError = false

                            when {
                                nombre.isBlank() -> {
                                    mensajeError = "El nombre es obligatorio"
                                    mostrarError = true
                                }
                                correo.isBlank() -> {
                                    mensajeError = "El correo es obligatorio"
                                    mostrarError = true
                                }
                                !correo.contains("@") -> {
                                    mensajeError = "Ingrese un correo válido"
                                    mostrarError = true
                                }
                                contrasena.length < 6 -> {
                                    mensajeError = "La contraseña debe tener al menos 6 caracteres"
                                    mostrarError = true
                                }
                                contrasena != confirmarContrasena -> {
                                    mensajeError = "Las contraseñas no coinciden"
                                    mostrarError = true
                                }
                                else -> {
                                    cargando = true
                                    // Convertir el saldo del formato colombiano (100.000,50) al formato numérico
                                    val saldoTexto = saldoInicial.trim()
                                    val saldo = if (saldoTexto.isNotEmpty()) {
                                        try {
                                            // Remover los puntos (separadores de miles)
                                            // Reemplazar la coma por punto (separador decimal)
                                            val saldoNormalizado = saldoTexto
                                                .replace(".", "")
                                                .replace(",", ".")
                                            saldoNormalizado.toDouble()
                                        } catch (e: Exception) {
                                            0.0
                                        }
                                    } else {
                                        0.0
                                    }

                                    val nuevoUsuario = Usuario(
                                        nombre = nombre,
                                        correo = correo,
                                        contrasena = contrasena,
                                        saldoInicial = saldo
                                    )

                                    val resultado = repository.registrarUsuario(nuevoUsuario)

                                    resultado.onSuccess {
                                        // Iniciar sesión automáticamente
                                        repository.iniciarSesion(correo, contrasena)
                                        onRegistroExitoso()
                                    }.onFailure { error ->
                                        mensajeError = error.message ?: "Error al registrar"
                                        mostrarError = true
                                    }

                                    cargando = false
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryPurple
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !cargando
                    ) {
                        if (cargando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White
                            )
                        } else {
                            Text(
                                text = "Registrarse",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}