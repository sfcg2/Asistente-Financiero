package com.example.asistentefinanciero.ui.vistas.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asistentefinanciero.R
import com.example.asistentefinanciero.ui.theme.*
import com.example.asistentefinanciero.viewmodel.AuthViewModel

@Composable
fun RegisterVista(
    viewModel: AuthViewModel,
    onRegistroExitoso: () -> Unit,
    onIrALogin: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }

    val cargando by viewModel.cargando.collectAsState()
    val errorMensaje by viewModel.errorMensaje.collectAsState()

    // Limpiar error al entrar a la vista
    LaunchedEffect(Unit) {
        viewModel.limpiarError()
    }

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
            Spacer(modifier = Modifier.height(20.dp))

            // Botón volver
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = {
                    viewModel.limpiarError()
                    onIrALogin()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = TextPrimary
                    )
                }
            }

            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo), // Usa el nombre que le diste
                contentDescription = "Logo SVG",
                modifier = Modifier.size(80.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box{
                Text(
                    text = "Control",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "             Cash",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextPrimary
                )
            }

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
                        onValueChange = {
                            nombre = it
                            viewModel.limpiarError()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Nombre", color = Color(0xFFB4B4B4)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = PrimaryPurple
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !cargando,
                        singleLine = true,
                        isError = errorMensaje != null
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Correo
                    OutlinedTextField(
                        value = correo,
                        onValueChange = {
                            correo = it
                            viewModel.limpiarError()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Correo", color = Color(0xFFB4B4B4)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = PrimaryPurple
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !cargando,
                        singleLine = true,
                        isError = errorMensaje != null
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Contraseña
                    OutlinedTextField(
                        value = contrasena,
                        onValueChange = {
                            contrasena = it
                            viewModel.limpiarError()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Contraseña", color = Color(0xFFB4B4B4)) },
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = PrimaryPurple
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !cargando,
                        singleLine = true,
                        isError = errorMensaje != null
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Confirmar Contraseña
                    OutlinedTextField(
                        value = confirmarContrasena,
                        onValueChange = {
                            confirmarContrasena = it
                            viewModel.limpiarError()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Confirmar contraseña", color = Color(0xFFB4B4B4)) },
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = PrimaryPurple
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !cargando,
                        singleLine = true,
                        isError = errorMensaje != null
                    )

                    // Mostrar error si existe
                    if (errorMensaje != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMensaje ?: "",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón Registrar
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
                                    viewModel.registrar(nombre, correo, contrasena) {
                                        onRegistroExitoso()
                                    }
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

            Spacer(modifier = Modifier.height(24.dp))

            // Link Iniciar sesión
            TextButton(
                onClick = {
                    viewModel.limpiarError()
                    onIrALogin()
                }
            ) {
                Text(
                    text = "¿Ya tienes cuenta? Inicia sesión",
                    color = TextPrimary,
                    fontSize = 16.sp
                )
            }
        }
    }
}